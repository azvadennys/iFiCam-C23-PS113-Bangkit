const services = require('../helper/service');
const cheerio = require('cheerio');

const baseUrl = 'https://www.masakapahariini.com';
const { v4: uuidv4 } = require('uuid');
const path = require('path');
const admin = require('firebase-admin');
const { Storage } = require('@google-cloud/storage');
const storage = new Storage({
    projectId: 'capstone-ificam',
    keyFilename: './capstone-ificam-c063b710519c.json',
});

const generateSignedURL = async (bucketName, fileDict) => {
    const options = {
        version: 'v4',
        action: 'read',
        expires: Date.now() + 1440 * 60 * 1000, // URL expires in 15 minutes
    };
  
    const [url] = await storage.bucket(bucketName).file(fileDict).getSignedUrl(options);
    return url;
};

const addNewRecipes = async (req, res) => {
    try {
        const recipeId = uuidv4();
        const { fishId, title, linkUrl, difficulty, duration } = req.body;
        const createdAt = new Date().toLocaleString('en-US', { timeZone: 'Asia/Jakarta' }).replace(/(\d+)\/(\d+)/, '$2/$1');
        
        const image_file = req.file;
        if (!image_file) {
            return res.status(400).send('No file uploaded.');
        }

        const fishDoc = await admin.firestore().collection('fishs').doc(fishId).get();
        const fishData = fishDoc.data();

        const recipeDoc = await admin.firestore().collection('recipes').doc(recipeId).get();
        if (recipeDoc.exists) {
            return res.status(404).json({ message: 'Recipes Id already exists' });
        } else if (!fishId || !title || !linkUrl || !difficulty || !duration) {
            return res.status(500).json({ message: 'Error creating Recipe' });
        }

        const originalFileName = image_file.originalname;
        const newFileName = `recipe_${fishData.name.replace(/\s+/g, '_').replace(/[^a-zA-Z0-9_.-]/g, '')}_${recipeId}`;
        const fileDict = `Recipes/${fishId}/${newFileName}${path.extname(originalFileName)}`;

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
            return res.status(500).send('Internal Server Error');
        });

        stream.on('finish', async () => {
            const newRecipe = {
                fishId: fishId,
                recipeId: recipeId,
                title: title,
                linkUrl: linkUrl,
                difficulty: difficulty,
                duration: duration,
                createdAt: createdAt,
                photoUrl: fileDict,
            }

            try {
                await admin.firestore().collection('recipes').doc(recipeId).set(newRecipe);
                return res.status(201).json(newRecipe);
            } catch (error) {
                return res.status(500).json({ message: 'Error creating recipe' });
            }
        });
        stream.end(req.file.buffer);
    } catch (error) {
        console.log(error)
        return res.status(500).json({ message: 'Error creating recipe' });
    }
}

const getAllFishRecipes = async (req, res) => {
    try {
        const snapshots = await admin.firestore().collection('recipes').get();
        const fishRecipes = [];

        const promises = snapshots.docs.map(async (doc) => {
            const recipe = doc.data();

            const bucketName = 'capstone-ificam';
            recipe.photoUrl = await generateSignedURL(bucketName, recipe.photoUrl);
            fishRecipes.push(recipe)
        });
  
        await Promise.all(promises);

        return res.status(200).json({
            status: "Succes",
            recipes: fishRecipes
        });

    } catch (error) {
        console.log(error)
        return res.status(500).json({ message: 'Error get recipes' });
    }
};

const getSpecFishRecipes = async (req, res) => {
    try {
        const query = req.params.id;
        const snapshots = await admin.firestore().collection('recipes').get();
        const fishRecipes = [];

        const promises = snapshots.docs.map(async (doc) => {
            const recipe = doc.data();
            if (recipe.fishId == query) {
                const bucketName = 'capstone-ificam';
                recipe.photoUrl = await generateSignedURL(bucketName, recipe.photoUrl);
                fishRecipes.push(recipe)
            }
        });
  
        await Promise.all(promises);

        return res.status(200).json({
            fishId: query,
            recipes: fishRecipes
        });

    } catch (error) {
        return res.status(500).json({ message: 'Error get recipes' });
    }
};

module.exports = {
    getAllFishRecipes,
    getSpecFishRecipes,
    addNewRecipes
};