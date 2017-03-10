var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var routes = require('./routes/index');
var users = require('./routes/users');
var system_pitch = require('./routes/system_pitch');
var pitch = require('./routes/pitch');
var find_teams = require('./routes/find_teams');

//var connectionString = require('./module/database');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', routes);
app.use('/users', users);
app.use('/system_pitch', system_pitch );
app.use('/pitch', pitch);
app.use('/find_teams', find_teams);

// catch 404 and forward to error handler
// app.use(function(req, res, next) {
//   var err = new Error('Not Found 12');
//   err.status = 404;
//   next(err);
// });

app.get('/',function(req,res){
  // var data = {
  //   "Data":""
  // };
  // data["Data"] = "1. users/getallusers : get " +
  //     "2. users/createuser : post";
  //
  //
  // res.json(data);
  res.render('index', { title: 'List API' });
});
// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});

module.exports = app;
