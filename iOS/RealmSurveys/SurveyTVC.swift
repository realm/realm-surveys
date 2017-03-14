import UIKit
import RealmSwift

class SurveyTVC: UITableViewController {

    var realm: Realm!
    fileprivate var questions: Results<Question>!
    fileprivate var questionsToken: NotificationToken?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.rowHeight = 88
        realm = try! Realm() // handle gracefully without crashing in a real world scenario.
        
        questions = realm.objects(Question.self)
            .filter("answers.@count == 0 OR (SUBQUERY(answers, $a, $a.userId == %@).@count == 0)", UserDefaults.userId())
            .sorted(byKeyPath: "timestamp")
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        questionsToken = questions.addNotificationBlock { [weak self] changes in
            
            guard let strongSelf = self else { return }
            switch changes {
            case .update(_, let del, let ins, let mod):
                strongSelf.tableView.applyChanges(deletions: del, insertions: ins, updates: mod)
            default:
                strongSelf.tableView.reloadData()
            }
        }

    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        questionsToken?.stop()
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
        
        DispatchQueue(label: "background").async {
            let bgRealm = try! Realm()
            try! bgRealm.write {
                let question = bgRealm.object(ofType: Question.self, forPrimaryKey: questionId)
                let existingAnswerForDevice = question?.answers.filter("userId == %@", UserDefaults.userId()).first
                if existingAnswerForDevice == nil {
                    let ans = Answer()
                    ans.userId = UserDefaults.userId()
                    ans.question = question
                    ans.response.value = answer
                    question?.answers.append(ans)
                }
            }
        }
    }
}


