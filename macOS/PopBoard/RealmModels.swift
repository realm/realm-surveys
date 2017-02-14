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

  override static func primaryKey() -> String? {
    return "questionId"
  }
}

