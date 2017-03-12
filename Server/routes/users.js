var express = require('express');
var router = express.Router();

/* GET users listing. */

var db = require('../module/users');
var nodefcm = require('../module/node_fcm');

router.get('/getallusers', db.getAllUsers);
router.get('/getallusersnew', db.getAllUsersNew);
router.post('/createNew', db.createNew);
router.post('/login', db.login);
router.put('/updatetockentouser/:id', nodefcm.updateTokenFCMToUser);

// test
router.post('/testnotification', nodefcm.testNotification);

module.exports = router;
