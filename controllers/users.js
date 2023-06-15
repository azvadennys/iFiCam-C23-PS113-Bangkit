const admin = require('firebase-admin');
const axios = require('axios');
const { Storage } = require('@google-cloud/storage');
const path = require('path');
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
  
const getUserById = async (req, res) => {
    try {
        const userId = req.params.id;
  
        const userSnapshot = await admin.firestore().collection('users').doc(userId).get();
        if (!userSnapshot.exists) {
            return res.status(404).json({ message: 'User not found' });
        }
  
        const userData = userSnapshot.data();
        if (userData.profile_photo) {
            const bucketName = 'capstone-ificam';
            const signedURL = await generateSignedURL(bucketName, userData.profile_photo);
            userData.profile_photo = signedURL;
        }
  
        return res.json(userData);
    } catch (error) {
        return res.status(500).json({ message: 'Error retrieving user' });
    }
};

const createNewUser = async (req, res) => {
    try {
        const userId = req.params.id;
        const email = req.body.email;
        const name = req.body.name;
        const createdAt = new Date().toLocaleString('en-US', { timeZone: 'Asia/Jakarta' }).replace(/(\d+)\/(\d+)/, '$2/$1');
        const signedIn = '';
        const profile_photo = '';
        const historyId = [];
        const storyId = [];
  
        const userSnapshot = await admin.firestore().collection('users').doc(userId).get();
        if (userSnapshot.exists) {
            return res.status(404).json({ message: 'User already exists' });
        }
  
        if (!userId || !email || !name) {
            return res.status(500).json({ message: 'Error creating user' });
        }
  
        const newUser = {
            uid: userId,
            email,
            name,
            createdAt,
            signedIn,
            profile_photo,
            historyId,
            storyId
        };
  
        await admin.firestore().collection('users').doc(newUser.uid).set(newUser);
        return res.status(201).json(newUser);
    } catch (error) {
        return res.status(500).json({ message: 'Error creating user' });
    }
};

const updateUser = async (req, res) => {
    try {
        const userId = req.params.id;
        const name = req.body.name;
  
        const userSnapshot = await admin.firestore().collection('users').doc(userId).get();
        if (!userSnapshot.exists) {
            return res.status(500).json({ message: 'Error updating user' });
        }
  
        const userData = userSnapshot.data();
  
        if (userData.profile_photo) {
            const fileDict = userData.profile_photo;
            const bucketName = 'capstone-ificam';
            const bucket = storage.bucket(bucketName);
            const file = bucket.file(fileDict);
    
            const replaceName = name.replace(/\s+/g, '_').replace(/[^a-zA-Z0-9_.-]/g, '');
            const newFileName = 'profile_' + replaceName;
            const newDict = `Users/${replaceName}/Profile/${newFileName}${path.extname(fileDict)}`;
    
            await admin.firestore().collection('users').doc(userId).update({
                name: name,
                profile_photo: newDict,
            });
  
            await file.move(newDict);
  
            return res.status(200).json({ message: 'User updated' });
        } else {
        await admin.firestore().collection('users').doc(userId).update({
            name: name,
        });
  
            return res.status(200).json({ message: 'User updated' });
        }
    } catch (error) {
        return res.status(500).json({ message: 'Error updating user' });
    }
};

const uploadProfilePhoto = async (req, res) => {
    try {
        const userId = req.params.id;
  
        const userSnapshot = await admin.firestore().collection('users').doc(userId).get();
        if (!userSnapshot.exists) {
            return res.status(404).json({ message: 'User not found' });
        }
  
        const user = userSnapshot.data();
        const name = user.name;
  
        if (!req.file) {
            return res.status(400).send('No file uploaded.');
        }
  
        const bucketName = 'capstone-ificam';
        const originalFileName = req.file.originalname;
        const newFileName = 'profile_' + name;
        const fileDict = `Users/${name}/Profile/${newFileName}${path.extname(originalFileName)}`;
    
        const bucket = storage.bucket(bucketName);
        const file = bucket.file(fileDict);
    
        const stream = file.createWriteStream({
            metadata: {
                contentType: req.file.mimetype,
            },
            resumable: false,
        });
  
        stream.on('error', (err) => {
            return res.status(500).json({ message: 'Error upload photo' });
        });
  
        stream.on('finish', async () => {
            await admin.firestore().collection('users').doc(userId).update({ profile_photo: fileDict });
            return res.status(200).json({ message: 'User updated, image uploaded' });
        });
  
        stream.end(req.file.buffer);
    } catch (error) {
        return res.status(500).json({ message: 'Error retrieving user' });
    }
};

// Delete a user by ID
const deleteUser = async (req, res) => {
    try {
        const userId = req.params.id;
        const userDoc = await admin.firestore().collection('users').doc(userId).get();

        if (userDoc.exists) {
            const profilePhoto = userDoc.data().profile_photo;
            const bucketName = 'capstone-ificam';
            const bucket = storage.bucket(bucketName);
            const file = bucket.file(profilePhoto);
            await file.delete();

            const history = userDoc.data().historyId;
            const historyPromises = history.map(async element => {
                const host = req.protocol + '://' + req.get('host') + '/api/histories/' + userId;
                const body = { historyid: element };

                try {
                    await axios.delete(host, { data: body });
                } catch (error) {
                    return res.status(500).json({ message: 'Error deleting histories' });
                }
            });
            await Promise.all(historyPromises);

            const story = userDoc.data().storyId;
            const storyPromises = story.map(async element => {
                const host = req.protocol + '://' + req.get('host') + '/api/stories/' + userId;
                const body = { storyid: element };

                try {
                    await axios.delete(host, { data: body });
                } catch (error) {
                    return res.status(500).json({ message: 'Error deleting stories' });
                }
            });
            await Promise.all(storyPromises);
        }

        await admin.auth().deleteUser(userId);

        await admin.firestore().collection('users').doc(userId).delete();

        return res.json({ message: 'User deleted successfully' });

    } catch (error) {
        return res.status(500).json({ message: 'Error deleting user' });
    }
};

module.exports = {
    getUserById,
    createNewUser,
    updateUser,
    uploadProfilePhoto,
    deleteUser
};
