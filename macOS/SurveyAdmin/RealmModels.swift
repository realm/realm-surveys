import Foundation
import RealmSwift

class Answer: Object {
  dynamic var userId = ""
  dynamic var question: Question?
  let response = RealmOptional<Bool>()
}

class Question: Object {
    
  dynamic var questionId = ""
  dynamic var timestamp: NSDate?
  let answers = List<Answer>()
  dynamic var questionText = ""

  convenience init(questionId: String, questionText: String) {
    self.init()
    self.questionId = questionId
    self.questionText = questionText
    self.timestamp = NSDate()
  }
    
  override static func primaryKey() -> String? {
    return "questionId"
  }
}

