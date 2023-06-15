// routes/users.js
const express = require('express');
const router = express.Router();
const multer = require('multer');
const upload = multer({
    storage: multer.memoryStorage(),
    limits: {
      fileSize: 10 * 1024 * 1024, // 10MB limit
    },
  });

// Controller
const { getStoryByUserId, getAllUserStories, createNewStory, deleteStory } = require('../controllers/stories');

// Middleware to authenticate requests
const admin = require('firebase-admin');
const authenticate = async (req, res, next) => {
  try {
      const uid = req.params.id
      const user = await admin.auth().getUser(uid);
      if (user){
          next();
      }
  } catch (error) {
      return res.status(401).json({ message: 'Unauthorized' });
  }
};

// Routes
router.get('/:id', authenticate, getStoryByUserId);
router.get('/', getAllUserStories);
router.post('/:id', authenticate, upload.single('image'), createNewStory);
router.delete('/:id', authenticate, deleteStory);

module.exports = router;
