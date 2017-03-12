
var express = require('express');
var router = express.Router();

var db = require('../module/pitch');

router.get('/getallpitchs', db.getAllPitchs);
router.get('/getallpitchsofsystem/:id', db.getAllPitchsOfSystem);
router.get('/getonepitch/:id', db.getOnePitch);
router.get('/getonepitchandsystempitch/:ids/:idp', db.getOnePitchAndSystemPitch);

router.post('/insertpitch', db.insertPitch);
router.put('/updatepitch/:id', db.updatePitch);
router.post('/getcalendarforday', db.getCalendarForDay);
router.put('/updateorder',db.updateOrder);

router.put('/updatemanagementpitch/:id', db.updateManagementPitch);

router.post('/getlistmanagementpitchforday', db.getListManagementPitchForDay);

router.get('/getmanagementpitchforpitch/:id', db.getManagementPitchForPitch);

module.exports = router;