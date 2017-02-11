var mongoose = require('mongoose');

var QuestionSchema = new mongoose.Schema({
  text: String,
  yesCount: String,
  noCount: String
});

module.exports = mongoose.model('Question', QuestionSchema);