const admin = require('firebase-admin');
const { v4: uuidv4 } = require('uuid');
const { Storage } = require('@google-cloud/storage');
const path = require('path');
const axios = require('axios');
const fs = require('fs');
const { promisify } = require('util');

const storage = new Storage({
    projectId: 'capstone-ificam',
    keyFilename: './capstone-ificam-c063b710519c.json',
});

const getPrediction = async (req, res) => {
    const userId = req.params.id;
    const historyId = uuidv4();
    const createdAt = new Date().toLocaleString('en-US', { timeZone: 'Asia/Jakarta' }).replace(/(\d+)\/(\d+)/, '$2/$1');

    try {
        const imageFile = req.file;

        if (!imageFile) {
            return res.status(400).send('Image Not Found');
        }

        const imagePath = `./temp/${imageFile.originalname}`;
        await promisify(fs.writeFile)(imagePath, imageFile.buffer);

        const formData = {
            image: fs.createReadStream(imagePath),
        };

        const mlServer = 'https://ificam-ml-e6adlj4jeq-et.a.run.app/predict';

        const options = {
            url: mlServer,
            formData,
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        };

        const response = await axios.post(options.url, formData, {
            headers: options.headers,
        });
        
        const prediction = response.data;

        const userDoc = await admin.firestore().collection('users').doc(userId).get();
        if (!userDoc.exists) {
            return res.status(404).send('User Not Found');
        }

        const userData = userDoc.data();
        const originalFileName = imageFile.originalname;
        const sanitizedUserName = userData.name.replace(/\s+/g, '_').replace(/[^a-zA-Z0-9_.-]/g, '');

        const fileDict = `Users/${sanitizedUserName}/History/${sanitizedUserName}_${historyId}${path.extname(originalFileName)}`;
        const bucketName = 'capstone-ificam';
        const bucket = storage.bucket(bucketName);
        const file = bucket.file(fileDict);
        const stream = file.createWriteStream({
            metadata: {
                contentType: req.file.mimetype,
            },
            resumable: false,
        });

        stream.on('error', (err) => {
            console.error(err);
            return res.status(500).send('Internal Server Error');
        });

        await new Promise((resolve, reject) => {
            fs.createReadStream(imagePath)
                .pipe(stream)
                .on('error', reject)
                .on('finish', resolve);
        });

        if (parseFloat(prediction.accuracy) >= 80) {
            const newFileName = prediction.class.replace(/\s+/g, '_').replace(/[^a-zA-Z0-9_.-]/g, '');
            const fileDict80 = `Fishs/Upper80/${newFileName}/${newFileName}_${historyId}${path.extname(originalFileName)}`;
            const file80 = bucket.file(fileDict80);
            const stream = file80.createWriteStream({
                metadata: {
                    contentType: req.file.mimetype,
                },
                resumable: false,
            });

            stream.on('error', (err) => {
                console.error(err);
                return res.status(500).send('Internal Server Error');
            });

            await new Promise((resolve, reject) => {
                fs.createReadStream(imagePath)
                    .pipe(stream)
                    .on('error', reject)
                    .on('finish', resolve);
            });
        }

        userData.historyId.push(historyId);

        const newHistory = {
            userId,
            historyId,
            fishId: prediction.class_id,
            photoUrl: fileDict,
            predictionValue: prediction.class,
            name: prediction.class,
            predictionAccuracy: prediction.accuracy,
            createdAt,
        };

        await admin.firestore().collection('histories').doc(historyId).set(newHistory);
        await admin.firestore().collection('users').doc(userId).update({
            historyId: userData.historyId,
        });

        const fishData = await admin.firestore().collection('fishs').doc(prediction.class_id).get();
        const optionsUrl = {
            version: 'v4',
            action: 'read',
            expires: Date.now() + 1440 * 60 * 1000, // URL expires in 15 minutes
        };

        // Generate a signed URL
        const [url] = await storage.bucket(bucketName).file(fileDict).getSignedUrl(optionsUrl);
        const host = `${req.protocol}://${req.get('host')}/api/recipes/${fishData.data().id}`;

        try {
            const response = await axios.get(host);
            const recipes = response.data.recipes;

            const predictionResults = {
                fishName: fishData.data().name,
                description: fishData.data().description,
                nutrition: fishData.data().nutrition,
                predictionAccuracy: prediction.accuracy,
                photoUrl: url,
                recipes,
            };

            fs.unlinkSync(imagePath);
            return res.status(200).send(predictionResults);

        } catch (error) {
            console.error(error);
            return res.status(500).send('Error getting recipes');
        }
    } catch (error) {
        console.error(error);
        return res.status(500).send('Internal Server Error');
    }
};

module.exports = {
    getPrediction,
};