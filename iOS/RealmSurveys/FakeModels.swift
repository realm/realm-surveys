import Foundation

class Answer {
    var userId = ""
    var question: Question?
    var response: Bool?
}

class Question {
    
    var questionId = ""
    var timestamp: NSDate?
    let answers = [] as [Answer]
    var questionText = ""
    
    init() {}
    
    convenience init(questionId: String, questionText: String) {
        self.init()
        self.questionId = questionId
        self.questionText = questionText
        self.timestamp = NSDate()
    }
    
}
