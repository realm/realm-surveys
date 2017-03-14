import UIKit
import RealmSwift

class SurveyTVC: UITableViewController {

    fileprivate var questions = [Question(questionId: "1", questionText: "Are you a Swift developer?"),
                                 Question(questionId: "2", questionText: "Have you tried Realm?")]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.rowHeight = 88

    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return questions.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "SurveyCell", for: indexPath) as! SurveyCell
        
        let questionModel = questions[indexPath.row]
        cell.showQuestion(questionModel: questionModel, delegate: self)

        return cell
    }
    
}

extension SurveyTVC: AnswerDelegate {
    func onQuestionAnswered(questionId: String, answer: Bool) {

        print("Answered [\(answer ? "Yes": "No")] question with id[\(questionId)]")
        
        
    }
}


