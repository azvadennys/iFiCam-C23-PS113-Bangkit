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
const { getAllFishRecipes, getSpecFishRecipes, addNewRecipes } = require('../controllers/recipes');

// Routes
router.get('/', getAllFishRecipes);
router.get('/:id', getSpecFishRecipes);
router.post('/', upload.single('image'), addNewRecipes);

module.exports = router;