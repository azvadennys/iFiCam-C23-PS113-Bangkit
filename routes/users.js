// routes/users.js
const express = require('express');
const router = express.Router();
const multer = require('multer');
const upload = multer({
    storage: multer.memoryStorage(),
    limits: {
      fileSize: 10 * 1024 * 1024, // 5MB limit
    },
});

// Controller
const { getUserById, createNewUser, updateUser, uploadProfilePhoto, deleteUser } = require('../controllers/users');

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
router.get('/:id', authenticate, getUserById);
router.post('/:id', authenticate, createNewUser);
router.put('/:id', authenticate, updateUser);
router.put('/upload/:id', authenticate, upload.single('image'), uploadProfilePhoto)
router.delete('/:id', authenticate, deleteUser);

module.exports = router;
