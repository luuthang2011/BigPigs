
var promise = require('bluebird');

var options = {
    // Initialization Options
    promiseLib: promise
};

var pgp = require('pg-promise')(options);

var connectionString = require('../module/database');

var db = pgp(connectionString.url);

function getAllPitchs(req, res, next) {
    db.any('SELECT * FROM pitch').then(function (data) {
        res.status(200)
            .json({
                status: 'success',
                data: data,
                message: 'Get All Pitchs Of Pitchs Table'
            });
    })
        .catch(function (err) {
            return next(err);
        })
}

function getAllPitchsOfSystem(req, res,next) {
    var systemPitchID = parseInt(req.params.id);
    db.any('SELECT * FROM pitch WHERE system_id = $1', systemPitchID).then(function (data) {
        res.status(200)
            .json({
                status : 'success',
                data : data,
                message : 'Get All Pitchs Of A System'
            });
    })
        .catch(function (err) {
            return next(err);
        })
}

function getOnePitch(req, res, next) {
    var pitchID = parseInt(req.params.id);
    db.any('SELECT * FROM pitch WHERE id = $1', pitchID).then(function (data) {
        res.status(200)
            .json({
                status : 'success',
                data : data,
                message : 'Get One Pitch'
            });
    })
        .catch(function (err) {
            return next(err);
        })
}

function getOnePitchAndSystemPitch(req, res, next) {

    var systemPitchID = parseInt(req.params.ids);
    var pitchID = parseInt(req.params.idp);

    db.any('SELECT s.name, s.address, s.phone, p.name, p.type, p.size, p.description ' +
        'FROM system_pitch s ' +
        'INNER JOIN pitch p ON s.id = p.system_id ' +
        'WHERE s.id = $1 AND p.id = $2', [systemPitchID, pitchID] ).then(function (data) {
        res.status(200)
            .json({
                status : 'success',
                data : data,
                message : 'Get Infor Pitchs and System Pitchs'
            });
    })
        .catch(function (err) {
            return next(err);
        });

}

function insertPitch(req, res, next) {
    req.body.size = parseInt(req.body.size);
    req.body.system_id = parseInt(req.body.system_id);

    db.any('SELECT * FROM pitch WHERE name = ${name}', req.body).then(function (data) {
        if (data.length > 0) // In the system, name already exists.
        {
            res.status(200)
                .json({
                    status : 'fail',
                    messenger : 'Name Pitch Already Exists'
                });
        }
        else {
            db.none('INSERT INTO pitch(name,type, size, image, description, system_id) VALUES ( ${name}, ${type}, ${size}, ${image}, ${description}, ${system_id} ) ',
                req.body)
                .then(function () {
                    res.status(200)
                        .json({
                            status: 'success',
                            message: 'Inserted one pitch'
                        });
                })
                .catch(function (err) {
                    return next(err);
                });
        }

    })


}

function updatePitch(req, res, next) {
    req.body.size = parseInt(req.body.size);
    req.body.system_id = parseInt(req.body.system_id);


    db.none('UPDATE pitch SET name=$1, type=$2, size=$3, description=$4, system_id=$5 WHERE id = $6',
        [req.body.name, req.body.type, req.body.size,
            req.body.description, req.body.system_id, parseInt(req.params.id)])
        .then(function () {
            res.status(200)
                .json({
                    status: 'success',
                    message: 'Updated pitch'
                });
        })
        .catch(function (err) {
            return next(err);
        });
}

function getCalendarForDay(req, res, next) {

        db.any('SELECT o.id, o.user_id, o.status, m.id, m.time_start, m.time_end, u.name, u.email, u.phone, u.type ' +
            'FROM orders o ' +
            'INNER JOIN management_pitch m ON m.id = o.management_id ' +
            'INNER JOIN users u ON u.id = o.user_id ' +
            " WHERE o.day = $1 AND m.pitch_id = $2 AND o.status = '1' ",
            [req.body.day, parseInt(req.body.pitch_id) ]).then(function (data) {
                res.status(200)
                    .json({
                       status : 'success',
                        data : data,
                        message : 'Get Calendar For Day'
                    });
        })
            .catch(function (err) {
                return next(err);
            });
}

function updateOrder(req, res, next) {
    var statusOrder = parseInt(req.body.status_order);
    var contentSQL = null;

    if (statusOrder == 1 ) //chap nhan
    {
        contentSQL = "UPDATE orders SET status = '1' WHERE id = $1 " ;
    }
    else { // status == 0 // k chap nhan yeu cau
        contentSQL = "UPDATE orders SET status = '0' WHERE id = $1 ";
    }

    db.none(contentSQL, [req.body.order_id]).then(function () {
        res.status(200)
            .json({
                status : 'success',
                messeage : 'Update Order'
            });
    })
        .catch(function (err) {
            return next(err);
        });
}


function updateManagementPitch(req, res, next) {

    var id = parseInt(req.params.id);
    req.body.system_id = parseInt(req.body.system_id);
    req.body.pitch_id = parseInt(req.body.pitch_id);
    req.body.typedate = parseInt(req.body.typedate);


    db.none('UPDATE management_pitch SET system_id = $1, pitch_id = $2, time_start = $3, time_end = $4, price = $5, typedate = $6, description = $7 WHERE id = $8',
        [req.body.system_id, req.body.pitch_id, req.body.time_start, req.body.time_end, req.body.price, req.body.typedate, req.body.description,id])
        .then(function () {
            res.status(200)
                .json({
                    status: 'success',
                    message: 'Updated Management Pitch'
                });
        })
        .catch(function (err) {
            return next(err);
        });
}

function getListManagementPitchForDay(req, res, next) {

    db.any('SELECT m.id, m.system_id,o.pitch_id, m.time_start, m.time_end, m.price, m.description, m.typedate FROM management_pitch  m, orders o' +
   ' WHERE ( ((Exists (SELECT * FROM orders o WHERE (m.id = o.management_id)) )=False) AND o.day = $2 AND m.pitch_id = $1)', [parseInt(req.body.pitch_id), req.body.day] ).then(function (data) {
        res.status(200)
            .json({
                status : 'success',
                data : data,
                message : 'Get List Management Pitch For Day'
            });
    })
        .catch(function (err) {
            return next(err);
        });
}

function getManagementPitchForPitch(req, res, next) {
    var id_pitch = parseInt(req.params.id);
    db.any('SELECT * FROM management_pitch WHERE pitch_id = $1', id_pitch).then(function (data) {
        res.status(200)
            .json({
                status : ' success',
                data : data,
                messenger : 'Get Management Pitch For Pitch '
            });
    })
        .catch(function (err) {
            return next(err);
        });

}

module.exports = {
    getAllPitchs : getAllPitchs,
    getAllPitchsOfSystem : getAllPitchsOfSystem,
    getOnePitch : getOnePitch,
    getOnePitchAndSystemPitch : getOnePitchAndSystemPitch,
    insertPitch : insertPitch,
    updatePitch : updatePitch,
    getCalendarForDay : getCalendarForDay,
    updateOrder : updateOrder,

    updateManagementPitch: updateManagementPitch,
    getListManagementPitchForDay : getListManagementPitchForDay,
    getManagementPitchForPitch : getManagementPitchForPitch
};
