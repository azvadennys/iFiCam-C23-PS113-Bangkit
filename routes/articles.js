const express = require('express');
const router = express.Router();

// Controller
const { getFishArticles } = require('../controllers/articles');

// Routes
router.get('/', getFishArticles);

module.exports = router;