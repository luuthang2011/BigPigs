
var promise = require('bluebird');

var options = {
    // Initialization Options
    promiseLib: promise
};

var pgp = require('pg-promise')(options);

var connectionString = require('../module/database');

var db = pgp(connectionString.url);

var FCM = require('fcm-node');

var fcm = new FCM(connectionString.serverKey);

function sendNotificationToAndroid(token, pitch_name, time_start) {

    var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
        to: token,
        // collapse_key: 'your_collapse_key',

        notification: {
            title: 'Bạn có một yêu cầu đặt sân',
            body: pitch_name + 'vào lúc' + time_start
        },

        // data: {  //you can send only notification or only data(or include both)
        //     my_key: 'my value',
        //     my_another_key: 'my another value'
        // }
    };
    console.log('notification');
    fcm.send(message, function(err, response){
        if (err) {
            console.log("Something has gone wrong!");
        } else {
            console.log("Successfully sent with response: ", response);
        }
    });
}

function updateTokenFCMToUser(req, res, next) {
    var id = parseInt(req.params.id);

    db.any('UPDATE users SET tokenfcm = $1 WHERE id = $2', [req.body.tokenfcm, id]).then(function () {
        res.status(200)
            .json({
                status : 'success',
                messenger : 'Update Token User'
            });
    })
        .catch(function (err) {
            return next(err);
        });
}

function testNotification(req, res, next) {
    var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
        to: req.body.token,
        // collapse_key: 'your_collapse_key',

        notification: {
            title: req.body.title,
            body: req.body.body
        },

        // data: {  //you can send only notification or only data(or include both)
        //     my_key: 'my value',
        //     my_another_key: 'my another value'
        // }
    };

    fcm.send(message, function(err, response){
        if (err) {
            console.log("Something has gone wrong!");
            res.status(200)
                .json({
                    status : 'fail',
                    messenger : 'Send notification '
                })
        } else {
            console.log("Successfully sent with response: ", response);
            res.status(200)
                .json({
                    status : 'success',
                    messenger : 'Send notification '
                })
        }
    });
}

module.exports = {
    updateTokenFCMToUser : updateTokenFCMToUser,
    sendNotificationToAndroid : sendNotificationToAndroid,

    testNotification : testNotification
};
