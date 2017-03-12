
var promise = require('bluebird');

var options = {
    // Initialization Options
    promiseLib: promise
};

var pgp = require('pg-promise')(options);

var connectionString = require('../module/database');

var db = pgp(connectionString.url);

function createNews(req, res, next) {


    db.none('insert into find_teams(title, address , description, time_start , user_id )' +
        'values($1, $2, $3, $4, $5)',
        [req.body.title, req.body.address, req.body.description, req.body.time_start, req.body.user_id])
        .then(function () {
            res.status(200)
                .json({
                    status: 'success',
                    message: 'Inserted one puppy'
                });
        })
        .catch(function (err) {
            return next(err);
        });
}

function getFindTeams(req, res, next) {
    var user_id = parseInt(req.params.id);
    db.any('select * from find_teams where user_id = $1', user_id)
        .then(function (data) {
            res.status(200)
                .json({
                    status: 'success',
                    data: data,
                    message: 'Retrieved ALL puppies'
                });
        })
        .catch(function (err) {
            return next(err);
        });

}

function getAllFindTeams(red, res, next) {
    db.any('select * from find_teams ')
        .then(function (data) {
            res.status(200)
                .json({
                    status: 'success',
                    data: data,
                    message: 'Retrieved ALL puppies'
                });
        })
        .catch(function (err) {
            return next(err);
        });

}


module.exports = {
    createNews : createNews,
    getFindTeams : getFindTeams,
    getAllFindTeams : getAllFindTeams,
};