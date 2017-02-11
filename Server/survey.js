'use strict';

//
// TODO: 1. replace with your own admin token before starting the app
// 2. replace the server IP
//

const REALM_ADMIN_TOKEN = "ewoJImlkZW50aXR5IjogIl9fYXV0aCIsCgkiYWNjZXNzIjogWyJ1cGxvYWQiLCAiZG93bmxvYWQiLCAibWFuYWdlIl0KfQo=:U7wn0iibiXBI+hbSEK8W0iH1JHFHGulfaDiDMg8+fbDn1oM/GUH7Al8/3h5PN9byTQ7M/Wrz1tXSPSB5MWbNBpGfnig4SYjR7j4tn2PnjIV5+4VLfkiidkjIw9ozjpkuIj50yO71VmZg0FR/DSVqs3mx41lbeSfZtZFNxtSvEk5l64AHvTb3VfPqJRHVxEkFI3S1pWIWUiBWxohM+dQviXGQCVqS7t8d+Hvhh6pYIfJiv2O8syMeYK+sOlwM/iRS6jly/+RNqH1yiHwdamu2cOxeG+z36mHqGLd8DtoXr09ICQ+TW6ATmsLPWC6mpa6ia3NBCi47D61hkNo0SVzR7A==";

const IP = '192.168.1.79';

var Realm = require('realm');

function isRealmObject(x) {
  return x !== null && x !== undefined && x.constructor === Realm.Object
}

/**
 * Pop game class
 *
 * Connects to Realm Object Server, installs event listener, processes Score object insertions
 */

function Pop(url, path) {
  this.url = url;
  this.path = path;
}

Pop.prototype.connect = function(token) {
  var admin = Realm.Sync.User.adminUser(token);

  Realm.Sync.addListener(this.url, admin, this.path, 'change', Pop.changeCallback);
  console.log('Pop app at: ' + this.url + " observing changes at: " + this.path);
}

Pop.changeCallback = function(event) {

  let realm = event.realm;
  let changes = event.changes.Score;
  if (changes == undefined) { 
    return
  }
  let indexes = changes.insertions;

  if (indexes.length == 0) return;

  var scores = realm.objects("Score");

  for (var i = 0; i < indexes.length; i++) {
    let index = indexes[i];
    let score = scores[index];

    if (isRealmObject(score)) {
      console.log("[score]: " + score.name + " > " + score.time)

      Board.addScore(score, function() {
        realm.write(function() {
          realm.delete(score)
        })
      })
    }
  }
}

/**
 * Score board class
 *
 * Saves scores to a text file
 */

var Board = {
  file: 'board.txt'
}

Board.addScore = function(score, success) {
  fs.appendFile(this.file, score.name+"\t"+score.time+"\n", function (err) {
    if (err) {
      return console.error(err);
    }
    success()
  })
}

//
// initialize and start the Pop app
//

var pop = new Pop('realm://'+IP+':9080', '.*/game')
pop.connect(REALM_ADMIN_TOKEN)
