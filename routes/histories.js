// routes/users.js
const express = require('express');
const router = express.Router();

// Controller
const { getHistoryByUserId, deleteHistory, getDetailedHistoryByUserAndHistoryId } = require('../controllers/histories');

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
router.get('/:id', authenticate, getHistoryByUserId);
router.get('/:id/:histId', authenticate, getDetailedHistoryByUserAndHistoryId);
router.delete('/:id', authenticate, deleteHistory);

module.exports = router;
