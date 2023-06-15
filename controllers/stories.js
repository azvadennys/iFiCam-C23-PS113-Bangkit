const admin = require('firebase-admin');
const { v4: uuidv4 } = require('uuid');
const { Storage } = require('@google-cloud/storage');
const path = require('path');
const storage = new Storage({
  projectId: 'capstone-ificam',
  keyFilename: './capstone-ificam-c063b710519c.json',
});
const bucketName = 'capstone-ificam';

const generateSignedURL = async (bucketName, fileDict) => {
    const options = {
        version: 'v4',
        action: 'read',
        expires: Date.now() + 1440 * 60 * 1000, // URL expires in 15 minutes
    };
  
    const [url] = await storage.bucket(bucketName).file(fileDict).getSignedUrl(options);
    return url;
};

function parseDateString(dateString) {
    const [date, time] = dateString.split(", ");
    const [day, month, year] = date.split("/");
    const [timeWithoutPeriod, period] = time.split(" ");
    const [hour, minute, second] = timeWithoutPeriod.split(":");
  
    return new Date(year, month - 1, day, hour % 12 + (period === "PM" ? 12 : 0) + 7, minute, second);
}


const getStoryByUserId = async (req, res) => {
    try {
        const userId = req.params.id;
        const doc = await admin.firestore().collection('users').doc(userId).get();

        if (!doc.exists) {
            return res.status(404).json({ message: 'User not found' });
        }

        const userData = doc.data();
        const storyId = userData.storyId;
        const userStories = [];

        if (storyId.length > 0) {
            const promises = storyId.map(async (element) => {
                const storyDoc = await admin.firestore().collection('stories').doc(element).get();
                const story = storyDoc.data();
                const options = {
                version: 'v4',
                action: 'read',
                expires: Date.now() + 1440 * 60 * 1000, // URL expires in 15 minutes
                };
                const [url] = await storage.bucket(bucketName).file(story.photoUrl).getSignedUrl(options);
                story.photoUrl = url;
                userStories.push(story);
            });

            await Promise.all(promises);
        }

        const data = {
            uid: userId,
            stories: userStories,
        };

        data.stories.sort((a, b) => parseDateString(b.createdAt) - parseDateString(a.createdAt));

        return res.status(200).json(data);
    } catch (error) {
        return res.status(500).json({ message: 'Error retrieving user' });
    }
};

const getAllUserStories = async (req, res) => {
    try {
        const snapshots = await admin.firestore().collection('stories').get();
        const currentTime = new Date().toLocaleString('en-US', { timeZone: 'Asia/Jakarta' }).replace(/(\d+)\/(\d+)/, '$2/$1');
        const dayMinusOne = parseInt(currentTime.split('/')[0]) - 1;
        const twentyFourHoursAgo = currentTime.replace(/^\d+/, `${dayMinusOne}`);

        const updatedStories = [];
        const promises = snapshots.docs.map(async (doc) => {
            const story = doc.data();
            if (parseDateString(story.createdAt) >= parseDateString(twentyFourHoursAgo) && 
                parseDateString(story.createdAt) <= parseDateString(currentTime)) {
                story.photoUrl = await generateSignedURL(bucketName, story.photoUrl);

                const userSnapshot = await admin.firestore().collection('users').doc(story.userId).get();
                if (!userSnapshot.exists) {
                    return res.status(404).json({ message: 'User not found' });
                }
        
                const userData = userSnapshot.data();
                if (userData.profile_photo) {
                    const bucketName = 'capstone-ificam';
                    const signedURL = await generateSignedURL(bucketName, userData.profile_photo);
                    userData.profile_photo = signedURL;
                }

                story.userName = userData.name;
                story.userPhoto = userData.profile_photo

                updatedStories.push(story);
            }
        });
  
        await Promise.all(promises);

        const results = {
            stories: updatedStories,
        };
        
        results.stories.sort((a, b) => parseDateString(b.createdAt) - parseDateString(a.createdAt));

        return res.status(200).json(results);
    } catch (error) {
        return res.status(500).json({ message: 'Error collecting story' });
    }
};

const createNewStory = async (req, res) => {
    try {
        const storyId = uuidv4();
        const userId = req.params.id;
        const { name, description, address, latitude, longitude } = req.body;
        const createdAt = new Date().toLocaleString('en-US', { timeZone: 'Asia/Jakarta' }).replace(/(\d+)\/(\d+)/, '$2/$1');

        const image_file = req.file;
        if (!image_file) {
            return res.status(400).send('No file uploaded.');
        }

        const userDoc = await admin.firestore().collection('users').doc(userId).get();
        const userData = userDoc.data();

        const storyDoc = await admin.firestore().collection('stories').doc(storyId).get();
        if (storyDoc.exists) {
            return res.status(404).json({ message: 'Stories Id already exists' });
        } else if (!userId || !name || !description || !address || !latitude || !longitude) {
            return res.status(500).json({ message: 'Error creating story' });
        }

        const originalFileName = image_file.originalname;
        const newFileName = `story_${userData.name.replace(/\s+/g, '_').replace(/[^a-zA-Z0-9_.-]/g, '')}_${storyId}`;
        const fileDict = `Users/${userData.name.replace(/\s+/g, '_').replace(/[^a-zA-Z0-9_.-]/g, '')}/Stories/${newFileName}${path.extname(originalFileName)}`;

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
            userData.storyId.push(storyId);
            const newStory = {
                storyId: storyId,
                userId: userId,
                name: name,
                description: description,
                address: address,
                latitude: latitude,
                longitude: longitude,
                createdAt: createdAt,
                photoUrl: fileDict,
        };

            try {
                await admin.firestore().collection('stories').doc(newStory.storyId).set(newStory);
                await admin.firestore().collection('users').doc(userId).update({
                    storyId: userData.storyId,
                });

                return res.status(201).json(newStory);
            } catch (error) {
                return res.status(500).json({ message: 'Error creating Story' });
            }
        });

        stream.end(req.file.buffer);
    } catch (error) {
        return res.status(500).json({ message: 'Error creating Story' });
    }
};

const deleteStory = async (req, res) => {
    try {
        const userId = req.params.id;
        const storyId = req.body.storyid;

        const userDoc = await admin.firestore().collection('users').doc(userId).get();
        if (!userDoc.exists) {
            return res.status(404).json({ message: 'User not found' });
        }

        const userData = userDoc.data();
        const storyData = userData.storyId;

        if (storyData.length > 0) {
            const promises = storyData.map(async (element) => {
                if (storyId == element) {
                const storyDoc = await admin.firestore().collection('stories').doc(element).get();
                const storyPhoto = storyDoc.data().photoUrl;

                const bucket = storage.bucket(bucketName);
                const file = bucket.file(storyPhoto);

                await file.delete();

                await admin.firestore().collection('stories').doc(storyId).delete();

                userData.storyId = userData.storyId.filter((item) => item !== storyId);
                await admin.firestore().collection('users').doc(userId).update({
                    storyId: userData.storyId,
                });
                }
            });

            await Promise.all(promises);
        }

        return res.json({ message: 'Story deleted successfully' });
    } catch (error) {
        return res.status(500).json({ message: 'Error deleting story' });
    }
};

module.exports = {
    getStoryByUserId,
    getAllUserStories,
    createNewStory,
    deleteStory,
};