const express = require('express');
const router = express.Router();

// Controller
const { getFishSpec, addNewFish, updateFish, deleteFish } = require('../controllers/fishs');

// Routes
router.get('/:id', getFishSpec);
router.post('/', addNewFish);
router.put('/:id', updateFish)
router.delete('/:id', deleteFish)


module.exports = router;