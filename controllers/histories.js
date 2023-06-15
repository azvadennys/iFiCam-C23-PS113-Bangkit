const admin = require('firebase-admin');
const { Storage } = require('@google-cloud/storage');
const axios = require('axios');
const storage = new Storage({
  projectId: 'capstone-ificam',
  keyFilename: './capstone-ificam-c063b710519c.json',
});

function parseDateString(dateString) {
    const [date, time] = dateString.split(", ");
    const [day, month, year] = date.split("/");
    const [timeWithoutPeriod, period] = time.split(" ");
    const [hour, minute, second] = timeWithoutPeriod.split(":");
  
    return new Date(year, month - 1, day, hour % 12 + (period === "PM" ? 12 : 0) + 7, minute, second);
}

const getDetailedHistoryByUserAndHistoryId = async (req, res) => {
    try {
        const userId = req.params.id;
        const historyId = req.params.histId;
        const userDoc = await admin.firestore().collection('users').doc(userId).get();

        if (!userDoc.exists) {
            return res.status(404).json({ message: 'User not found' });
        }

        const historyDoc = await admin.firestore().collection('histories').doc(historyId).get();
        const history = historyDoc.data();
        const options = {
            version: 'v4',
            action: 'read',
            expires: Date.now() + 1440 * 60 * 1000, // URL expires in 15 minutes
        };

        const bucketName = 'capstone-ificam';
        const [url] = await storage.bucket(bucketName).file(history.photoUrl).getSignedUrl(options);

        history.photoUrl = url;
        const fishData = await admin.firestore().collection('fishs').doc(history.fishId).get();
                
        const host = `${req.protocol}://${req.get('host')}/api/recipes/${fishData.data().id}`;
                
        const response = await axios.get(host);
        const recipes = response.data.recipes;
                
        history.fishName = fishData.data().name;
        history.description = fishData.data().description;
        history.nutrition = fishData.data().nutrition;
        history.recipes = recipes;

        const data = {
            uid: userId,
            detailedHistory: history,
        };

        return res.status(200).json(data);
    } catch (error) {
        console.log(error)
        return res.status(500).json({ message: 'Error retrieving user' });
    }
    
}

const getHistoryByUserId = async (req, res) => {
    try {
        const userId = req.params.id;
        const userDoc = await admin.firestore().collection('users').doc(userId).get();

        if (!userDoc.exists) {
            return res.status(404).json({ message: 'User not found' });
        }

        const userData = userDoc.data();
        const historyId = userData.historyId;
        const userHistories = [];

        if (historyId.length > 0) {
            const promises = historyId.map(async (element) => {
                const historyDoc = await admin.firestore().collection('histories').doc(element).get();
                const history = historyDoc.data();
                const options = {
                    version: 'v4',
                    action: 'read',
                    expires: Date.now() + 1440 * 60 * 1000, // URL expires in 15 minutes
                };

                const bucketName = 'capstone-ificam';
                const [url] = await storage.bucket(bucketName).file(history.photoUrl).getSignedUrl(options);

                history.photoUrl = url;
                userHistories.push(history);
            });

            await Promise.all(promises);
        }

        const data = {
            uid: userId,
            histories: userHistories,
        };

        data.histories.sort((a, b) => parseDateString(b.createdAt) - parseDateString(a.createdAt));

        return res.status(200).json(data);
    } catch (error) {
        return res.status(500).json({ message: 'Error retrieving user' });
    }
};

const deleteHistory = async (req, res) => {
    try {
        const userId = req.params.id;
        const historyId = req.body.historyid;
    
        const userRef = admin.firestore().collection('users').doc(userId);
        const userDoc = await userRef.get();
    
        if (!userDoc.exists) {
            return res.status(404).json({ message: 'User not found' });
        }
  
        const userData = userDoc.data();
        const historyData = userData.historyId;
  
        if (historyData.length > 0) {
            const deletePromises = [];
            for (const element of historyData) {
                if (historyId == element) {
                    const historyDocRef = admin.firestore().collection('histories').doc(element);
                    const historyDoc = await historyDocRef.get();
                    const historyPhoto = historyDoc.data().photoUrl;
                    const bucketName = 'capstone-ificam';
                    const bucket = storage.bucket(bucketName);
                    const file = bucket.file(historyPhoto);
        
                    await file.delete();
                    deletePromises.push(historyDocRef.delete());
                    
                    userData.historyId = userData.historyId.filter((item) => item !== historyId);
                    await userRef.update({ historyId: userData.historyId });
                }
            }
            await Promise.all(deletePromises);
        }
        return res.json({ message: 'History deleted successfully' });
    } catch (error) {
        return res.json({ message: 'Delete History Failed' });
    }
};
  

module.exports = {
    getDetailedHistoryByUserAndHistoryId,
    getHistoryByUserId,
    deleteHistory,
};