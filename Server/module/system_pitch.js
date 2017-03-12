
var promise = require('bluebird');

var options = {
    // Initialization Options
    promiseLib: promise
};

var pgp = require('pg-promise')(options);

var connectionString = require('../module/database');

var db = pgp(connectionString.url);

var nodefcm = require('../module/node_fcm');

function getAllSystemPitch(req, res, next) {

    db.any("SELECT id , name, address, description, phone, lat, log, user_id, status FROM system_pitch")
        .then(function (data) {
            res.status(200)
                .json({
                    status: 'success',
                    data: data,
                    message: 'Get All System Pitch Of system_pitch Table'
                });
        })
        .catch(function (err) {
            return next(err);
        });
}

function getsystempitch(req, res, next) {
    var systemPitchID = parseInt(req.params.id);
    db.one('SELECT * FROM system_pitch WHERE id = $1', systemPitchID)
        .then(function (data) {
            res.status(200)
                .json({
                    status: 'success',
                    data: data,
                    message: 'Get System Pitch For ID'
                });
        })
        .catch(function (err) {
            return next(err);
        });
}

function insertSystemPitch(req, res, next) {

    db.any('SELECT * FROM system_pitch WHERE name = ${name}', req.body).then(function (data) {

        if (data.length > 0){ // In the system, name system pitch already exists
            res.status(200)
                .json({
                    status: 'fail',
                    message: 'Name System Pitch Already Exists'
                });
        }
        else {
            db.none('INSERT INTO system_pitch (name, address, description, phone,lat, log, user_id)' +
                'VALUES($1, $2, $3, $4, $5, $6, $7)',
                [req.body.name, req.body.address, req.body.description,req.body.phone,
                    parseFloat(req.body.lat), parseFloat(req.body.log), parseInt(req.body.user_id)] )
                .then(function () {
                    res.status(200)
                        .json({
                            status: 'success',
                            message: 'Inserted one system pitch'
                        });
                })
                .catch(function (err) {
                    return next(err);
                });
        }
    })


}

function updateSystemPitch(req, res, next) {
    db.none('UPDATE system_pitch SET name=$1, address=$2, description=$3, phone=$4, lat=$5, log = $6, user_id=$7 WHERE id = $8',
        [req.body.name, req.body.address, req.body.description,req.body.phone,
            parseFloat(req.body.lat), parseFloat(req.body.log), parseInt(req.body.user_id), parseInt(req.params.id) ])
        .then(function () {
            res.status(200)
                .json({
                    status: 'success',
                    message: 'Updated system pitch'
                });
        })
        .catch(function (err) {
            return next(err);
        });

}

function updateStatusSystemPitch(req, res, next) {

    db.none('UPDATE system_pitch SET status=$1 WHERE id = $2',
        [req.body.status, parseInt(req.params.id)])
        .then(function () {
            res.status(200)
                .json({
                    status: 'success',
                    message: 'Updated status system pitch'
                });
        })
        .catch(function (err) {
            return next(err);
        });
}

function getPricePitch(req, res, next) {
    var pitchID = parseInt(req.body.id);

    db.any('SELECT * FROM management_pitch WHERE pitch_id = $1 ' ,[pitchID, req.body.day])
        .then(function (data) {           //

            res.status(200)
                .json({
                    status: 'success',
                    data: data,
                    message: 'Get Price Pitch'
                });
        })
        .catch(function (err) {
            return next(err);
        });
}
function  bookPitch(req, res, next) {
    req.body.user_id = parseInt(req.body.user_id);
    req.body.pitch_id = parseInt(req.body.pitch_id);
    db.none('INSERT INTO orders (user_id, pitch_id, management_id, status, day) VALUES(${user_id}, ${pitch_id}, ${management_id}, ${status}, ${day} )', req.body)
        .then(function () {
            // send notification token (users) pitch name time_start
            db.any('SELECT u.tokenfcm, p.name, m.time_start FROM users u, pitch p, management_pitch m, system_pitch s WHERE u.id = s.user_id AND m.system_id = s.id AND p.id = $1 AND m.id = $2',
                [req.body.pitch_id, req.body.management_id]).then(function (data) {
                    console.log(data);
                nodefcm.sendNotificationToAndroid(data[0].tokenfcm, data[0].name, data[0].time_start);
                res.status(200)
                    .json({
                        status: 'success',
                        message: 'Book Pitch And Send Notification'
                    });
            })
                .catch(function (err) {
                    return next(err);
                });
        })
        .catch(function (err) {
            return next(err);
        });
}

function insertManagement(req, res, next) {
    req.body.system_id = parseInt(req.body.system_id);
    req.body.pitch_id = parseInt(req.body.pitch_id);
    req.body.typedate = parseInt(req.body.typedate);

    db.none('INSERT INTO management_pitch ( system_id, pitch_id, time_start, time_end, price, typedate, description) ' +
        'VALUES ( ${system_id}, ${pitch_id}, ${time_start}, ${time_end},${price}, ${typedate}, ${description} )', req.body)
        .then(function () {

            res.status(200)
                .json({
                    status: 'success',
                    message: 'Insert Management Pitch'
                });
        })
        .catch(function (err) {
            return next(err);
        });
}

function getManagement(req, res, next) {
    var managementID = parseInt(req.params.id);
    db.none('SELECT * FROM management_pitch WHERE id = $1', managementID)
        .then(function (data) {
            res.status(200)
                .json({
                    status: 'success',
                    data: data,
                    message: 'Get Management Pitch For ID'
                });
        })
        .catch(function (err) {
            return next(err);
        });
}


function getTimeTableOfDay(req, res, next) { //check lich san theo ngay
    var pitchID = req.body.pitchid;
    var sPitchID = req.body.systemPID;
    var day = req.body.day;
    var status1 = '0';
    db.any('SELECT time_start, time_end FROM management_pitch ' +
        'WHERE pitch_id = $1 AND status = $2 and system_id=$3 AND day=$4',[pitchID,status1,sPitchID,day])
        .then(function (data) {
            res.status(200)
                .json({
                    status: 'success',
                    data: data,
                    message: 'Get Time Table Of Day'
                });
        })
        .catch(function (err) {
            return next(err);
        });
}



function getAllPitchOfSystem(req, res, next) {

    var sPitchID = req.body.systemPID;

    db.any("select * from pitch " +
        "where system_id=$1 ",[sPitchID])
        .then(function (data) {
            res.status(200)
                .json({
                    status: 'success',
                    data: data,
                    message: 'Get All Pitch Of System'
                });
        })
        .catch(function (err) {
            return next(err);
        });
}

function searchPitch(req, res, next) {

    var time_start = req.body.time_start;
    var day = req.body.day;
    var location = req.body.textlocation;
    location = '%' + location + '%';

    db.any("SELECT DISTINCT m.system_id, p.id,s.name, s.address, s.description, s.phone,s.lat, s.log " +
    "FROM management_pitch m " +
    "INNER JOIN system_pitch s ON s.id = m.system_id " +
    "INNER JOIN orders o ON o.management_id = m.id" +
        "INNER JOIN pitch p ON p.id = o.pitch_id " +
    "WHERE m.id NOT IN (SELECT management_id FROM orders WHERE orders.day = $1 AND orders.status ='1') AND m.time_start = $2 AND s.address LIKE $3 ", [day,time_start ,location])
        .then(function (data) {

            if (data.length == 0) {

                db.any("SELECT DISTINCT m.system_id, s.id,s.name, s.address, s.description, s.phone,s.lat, s.log " +
                    "FROM management_pitch m " +
                    "INNER JOIN system_pitch s ON s.id = m.system_id " +
                    "INNER JOIN orders o ON o.management_id = m.id " +
                    "WHERE m.id NOT IN (SELECT management_id FROM orders WHERE orders.day = $1 AND orders.status ='1') AND m.time_start > $2 AND s.address LIKE $3 " +
                    "LIMIT 5", [day,time_start ,location])
                    .then(function (data1) {
                        res.status(200)
                            .json({
                                status: 'success',
                                data: data1,
                                message: 'Search System Pitch With Hours Other'
                            });
                    })
                    .catch(function (err) {
                        return next(err);
                    });
            }
            else {
                res.status(200)
                    .json({
                        status: 'success',
                        data: data,
                        message: 'Search System Pitch'
                    });
            }
        })
        .catch(function (err) {
            return next(err);
        });
}

function updateBookPitch( req, res, next) {

    req.body.user_id = parseInt(req.body.user_id);
    req.body.pitch_id = parseInt(req.body.pitch_id);
    db.none('UPDATE orders SET user_id=$1, pitch_id=$2, management_id=$3, status=$4,day = $5 WHERE id = $6',
        [req.body.user_id, req.body.pitch_id, req.body.management_id, req.body.status, req.body.day, parseInt(req.params.id)])
        .then(function () {
            res.status(200)
                .json({
                    status: 'success',
                    message: 'Update Book Pitch'
                });
        })
        .catch(function (err) {
            return next(err);
        });

}

module.exports = {
  getAllSystemPitch : getAllSystemPitch,
  getsystempitch : getsystempitch,
  insertSystemPitch : insertSystemPitch,
  updateSystemPitch : updateSystemPitch,
  updateStatusSystemPitch : updateStatusSystemPitch ,
  getPricePitch      : getPricePitch,


  bookPitch : bookPitch,
  insertManagement : insertManagement,
  getManagement : getManagement,

  getTimeTableOfDay : getTimeTableOfDay,

  getAllPitchOfSystem : getAllPitchOfSystem,
  searchPitch : searchPitch,
    updateBookPitch : updateBookPitch
};