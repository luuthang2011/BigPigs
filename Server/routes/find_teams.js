
var express = require('express');
var router = express.Router();

var db = require('../module/find_teams');

router.post('/createNews', db.createNews);
router.get('/getallfindteams', db.getAllFindTeams);
router.get('/getfindteams/:id', db.getFindTeams);

module.exports = router;