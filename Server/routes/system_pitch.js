
var express = require('express');
var router = express.Router();

/* GET users listing. */

var db = require('../module/system_pitch');



router.get('/getallsystempitch', db.getAllSystemPitch); //
router.get('/getsystempitch/:id', db.getsystempitch); //
router.post('/insertsystempich', db.insertSystemPitch); //
router.put('/updatesystempitch/:id', db.updateSystemPitch); //
router.put('/updatestatussystempitch/:id', db.updateStatusSystemPitch); //
router.post('/bookpitch', db.bookPitch); //
router.post('/insertmanagement', db.insertManagement); //
router.get('/getmanagement/:id', db.getManagement); //
router.post('/getPricePitch', db.getPricePitch); //
router.put('/updatebookpitch/:id', db.updateBookPitch);


router.post('/getTimeTableOfDay', db.getTimeTableOfDay);


router.post('/getAllPitchOfSystem', db.getAllPitchOfSystem);

router.post('/searchPitch', db.searchPitch);//


module.exports = router;
