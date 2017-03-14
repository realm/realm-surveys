import UIKit

class SurveyCell: UITableViewCell {
    
    @IBOutlet weak var questionLabel: UILabel!
    @IBOutlet weak var answerSegControl: UISegmentedControl!

    var questionId: String = ""
    
    var question: String {
        get {
            return questionLabel.text ?? ""
        }
        set {
            questionLabel.text = newValue
        }
    }
    
    var answer: Bool? {
        get {
            if answerSegControl.selectedSegmentIndex == -1 {
                return nil
            } else {
                return answerSegControl.selectedSegmentIndex == 0
            }
        }
        
        set {
            if newValue == nil {
                answerSegControl.selectedSegmentIndex = -1
            } else {
                answerSegControl.selectedSegmentIndex = newValue! ? 0 : 1
            }
        }
    }

    func show(questionModel: Question) {
        questionId = questionModel.questionId
        question = questionModel.questionText
        answer = nil
    }
}
