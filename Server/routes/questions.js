var express = require('express');
var router = express.Router();

var Question = require('../models/Question.js');

var openConnections = [];

/* POST /questions */
router.post('/', function(req, res, next) {
  Question.create(req.body, function (err, post) {
    if (err) return next(err);
    res.json(post);
  });
});

/* GET /questions/id */
router.get('/:id', function(req, res, next) {
  Question.findById(req.params.id, function (err, post) {
    if (err) return next(err);
    res.json(post);
  });
});

/* PUT /questions/:id */
router.put('/:id', function(req, res, next) {
  Question.findByIdAndUpdate(req.params.id, req.body, function (err, post) {
    if (err) return next(err);
    res.json(post);
  });
});

/* DELETE /questions/:id */
router.delete('/:id', function(req, res, next) {
  Question.findByIdAndRemove(req.params.id, req.body, function (err, post) {
    if (err) return next(err);
    res.json(post);
  });
});

router.get('/', function(req, res, next) {
  Question.find(function (err, questions) {
    if (err) return next(err);
    res.json(questions);
  });
});

/* GET /questions listing. */
/*
router.get('/', function(req, res, next) {

     // set timeout as high as possible
    req.socket.setTimeout(Infinity);
 
    // send headers for event-stream connection
    // see spec for more information
    res.writeHead(200, {
        'Content-Type': 'text/event-stream',
        'Cache-Control': 'no-cache',
        'Connection': 'keep-alive'
    });
    res.write('\n');
 
    // push this res object to our global variable
    openConnections.push(res);
 
    // When the request is closed, e.g. the browser window
    // is closed. We search through the open connections
    // array and remove this connection.
    req.on("close", function() {
        var toRemove;
        for (var j =0 ; j < openConnections.length ; j++) {
            if (openConnections[j] == res) {
                toRemove =j;
                break;
            }
        }
        openConnections.splice(j,1);
        console.log(openConnections.length);
    });

});

setInterval(function() {
    // we walk through each connection
    openConnections.forEach(function(resp) {
        Question.find(function (err, questions) {
            if (err) return next(err);
            res.json(questions);
        });
    });
 
}, 1000);
*/

module.exports = router;
