var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var routes = require('./routes/index');
var users = require('./routes/users');
var questions = require('./routes/questions');

var realm;

const QuestionSchema = {
  name: 'Question', 
      primaryKey: 'questionId',
      properties: {
        questionId:   { type: 'string' }, 
        timestamp:    { type: 'date', optional: true },
        questionText: { type: 'string' },
        answers:      { type: 'list', objectType: 'Answer' }
    }
};

const AnswerSchema = {
    name: 'Answer', 
      properties: {
        userId:   { type 'string' } 
        question: { type 'Answer' }, 
        response: { type: 'bool', optional: true }
      }
};

// load mongoose package
//var mongoose = require('mongoose');
const IP = '192.168.1.79';
var Realm = require('realm');

let user = Realm.Sync.User.current;
if(user != undefined) {
   console.log('logout current user'))
   user.logout();
}
Realm.Sync.User.login('http://' + IP + ':9080', 'survey@demo.io', 'password', (error, user) => { 
   console.log('logged in as survey@demo.io'))
   realm = new Realm({
      sync: {
        user: user,
        url: 'realm://' + IP + ':9080/~/my-realm',
      },
      schema: [ QuestionSchema, AnswerSchema ];
    }); 
});


// Use native Node promises
// mongoose.Promise = global.Promise;

// connect to MongoDB
// mongoose.connect('mongodb://localhost/question-api')
  // .then(() =>  console.log('connection succesful'))
  // .catch((err) => console.error(err));

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', routes);
app.use('/users', users);
app.use('/questions', questions);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
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
