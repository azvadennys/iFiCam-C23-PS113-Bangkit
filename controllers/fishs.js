const admin = require('firebase-admin');

const getFishSpec = async (req, res) => {
    const fishId = req.params.id;
    try {
        const doc = await admin.firestore().collection('fishs').doc(fishId).get();
        if (doc.exists) {
        const fishData = doc.data();
        return res.status(200).json(fishData);
        } else {
        return res.status(404).json({ message: 'Fish not found' });
        }
    } catch (error) {
        return res.status(500).json({ message: 'Error retrieving fish' });
    }
};

const addNewFish = async (req, res) => {
    const fishId = req.body.id;
    const name = req.body.name;
    const description = req.body.description;
    const nutrition = req.body.nutrition;
    const createdAt = new Date().toLocaleString('en-US', { timeZone: 'Asia/Jakarta' }).replace(/(\d+)\/(\d+)/, '$2/$1');
    const fishcol = req.body.fishcol || '';

    if (!fishId || !name || !description || !nutrition) {
        return res.status(400).json({ message: 'Please add fish id, name, description, nutrition' });
    }

    try {
        const doc = await admin.firestore().collection('fishs').doc(fishId).get();
        if (doc.exists) {
        return res.status(404).json({ message: 'Fish already exists' });
        }

        const newFish = {
        id: fishId,
        name,
        description,
        nutrition,
        createdAt,
        fishcol,
        };

        await admin.firestore().collection('fishs').doc(newFish.id).set(newFish);
        return res.status(201).json(newFish);
    } catch (error) {
        return res.status(500).json({ message: 'Error creating user' });
    }
};

const updateFish = async (req, res) => {
    const fishId = req.params.id;
    const name = req.body.name;
    const description = req.body.description;
    const nutrition = req.body.nutrition;
    const fishcol = req.body.fishcol;

    try {
        const doc = await admin.firestore().collection('fishs').doc(fishId).get();
        if (doc.exists) {
        const fishData = doc.data();
        if (name) {
            fishData.name = name;
        }

        if (description) {
            fishData.description = description;
        }

        if (nutrition) {
            fishData.nutrition.push(nutrition);
        }

        if (fishcol) {
            fishData.fishcol = fishcol;
        }

        await admin.firestore().collection('fishs').doc(fishId).update(fishData);
        return res.status(200).json({ message: 'Fish updated' });
        } else {
        return res.status(500).json({ message: 'Error updating fish' });
        }
    } catch (error) {
        return res.status(500).json({ message: 'Error updating fish' });
    }
};

const deleteFish = async (req, res) => {
    const fishId = req.params.id;
    try {
        await admin.firestore().collection('fishs').doc(fishId).delete();
        return res.json({ message: 'Fish deleted successfully' });
    } catch (error) {
        return res.status(500).json({ message: 'Failed deleting fish' });
    }
};

module.exports = {
    getFishSpec,
    addNewFish,
    updateFish,
    deleteFish,
};