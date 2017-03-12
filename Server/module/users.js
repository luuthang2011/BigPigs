
var promise = require('bluebird');

var options = {
    // Initialization Options
    promiseLib: promise
};

var pgp = require('pg-promise')(options);

var connectionString = require('../module/database');

var db = pgp(connectionString.url);


//var express = require('express');
//var app = require('../app');
var jwt    = require('jsonwebtoken');

// secret variable
//app.set('secret', connectionString.secret);

function createNew(req, res, next) {

    // check name accout

    db.any('SELECT email FROM users WHERE email = $1', req.body.email).then(function (data) {

        if (data.length > 0) {
            res.status(200)
                .json({
                    status : 'fail',
                    messenger : 'Email Already Exists'
                });
        }
        else {
            // create new user
            db.none('INSERT INTO users(email, password) VALUES(${email}, ${password})', req.body)
                .then(function () {
                    res.status(200)
                        .json({
                            status : 'success',
                            messenger : 'Create New User'
                        });
                })
                .catch(function (err) {
                    return next(err);
                });

        }
    })
}

function getAllUsers(req, res, next) {

    var token = req.body.token || req.query.token || req.headers['x-access-token'];

    if (token){
        // verifies secret and checks exp
        jwt.verify(token, connectionString.secret, function(err, decoded) {
            if (err) {
                return res.json({ success: false, message: 'Failed to authenticate token.' });
            } else {
                // if everything is good, save to request for use in other routes
                req.decoded = decoded;

                db.any('SELECT * FROM users')
                    .then(function (data) {
                        res.status(200)
                            .json({
                                status: 'success',
                                data: data,
                                message: 'Retrieved ALL Users'
                            });
                    })
                    .catch(function (err) {
                        return next(err);
                    });
            }
        });
    } else {

        // if there is no token
        // return an error
        return res.status(403).
            json({
            success: 'false',
            message: 'No token provided.'
        });
    }
}



function login (req, res, next) {
    db.any('SELECT * FROM users WHERE email = $1 AND password = $2', [req.body.email, req.body.password] )
        .then(function (data) {

            if (data.length > 0) // db have email - password
            {
               // login sccess
               var token = jwt.sign(data[0], connectionString.secret, {
                    expiresIn: 86400 // expires in 24 hours
                });

                res.json({
                    status : 'success',
                    data : data,
                    token : token
                })
            }
            else {
                res.json({
                    status : 'Fail',
                    messenger : 'Fail ! Please check username or password'
                })
            }


    })

    
}
function getAllUsersNew(req, res, next) {
    db.any('SELECT * FROM users')
        .then(function (data) {
            res.status(200)
                .json({
                    status: 'success',
                    data: data,
                    message: 'Get All Users'
                });
        })
        .catch(function (err) {
            return next(err);
        });
}



module.exports = {
    getAllUsers : getAllUsers,
    getAllUsersNew : getAllUsersNew,
    createNew : createNew,
    login : login

};
