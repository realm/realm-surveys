/*
 * Copyright 2017 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import Cocoa
import RealmSwift

struct SurveyResult {
    let question: String
    let yesCount: Int
    let noCount: Int
}

let host = "" // <== Set your Realm Object Server host IP here.
let user = "survey@demo.io"
let pass = "password"

let serverURL = URL(string: "http://\(host):9080")!
let syncURL = URL(string: "realm://\(host):9080/~/survey")!


class ViewController: NSViewController {
    @IBOutlet var tableView: NSTableView!

    @IBOutlet weak var newQuestionText: NSTextField!
    
    var realm: Realm?
    var questions: Results<Question>?
    var questionsToken: NotificationToken?
    var scores = [SurveyResult]()
    
    override func viewWillAppear() {
        super.viewDidAppear()
        connect { [unowned self] in
            self.realm = try! Realm()
            guard let realm = self.realm else { return }
            self.questions = realm.objects(Question.self).sorted(byKeyPath: Question.primaryKey()!)

            self.questionsToken = self.questions?.observe { [weak self] changes in
                self?.refresh()
            }
            self.refresh();
        }
    }

    override func viewWillDisappear() {
        super.viewWillDisappear()
        questionsToken?.invalidate()
    }

    func refresh() {
        guard let questions = self.questions else { return }

        scores.removeAll()
        for question in questions {
            scores.append(
                SurveyResult(
                    question: question.questionText,
                    yesCount: question.answers.filter("response == true").count,
                    noCount: question.answers.filter("response == false").count)
            )
        }


        tableView.reloadData()
        DispatchQueue.main.asyncAfter(deadline: .now()+5, execute: refresh)
    }

    @IBAction func addNewQuestion(_ sender: Any) {
        let newQuestionText = self.newQuestionText.stringValue
        
        if newQuestionText.isEmpty == false && self.realm != nil {

            if newQuestionText == "clear" {
                try! self.realm!.write {
                    self.realm!.deleteAll()
                }
                
            } else {
                try! self.realm!.write {
                    let questionCount = (self.questions?.count ?? 0) + 1
                    let newQuestion = Question(
                        questionId: String(questionCount),
                        questionText: self.newQuestionText.stringValue)
                    realm?.add(newQuestion)
                }
            }
            
            self.newQuestionText.stringValue = ""
        }
    }

    func connect(completion: @escaping () -> Void) {
        let cred = SyncCredentials.usernamePassword(username: user, password: pass, register: false)
        
        SyncUser.logIn(with: cred, server: serverURL) {user, error in
            guard let user = user else {
                return DispatchQueue.main.asyncAfter(deadline: .now() + 5) {
                    self.connect(completion: completion)
                }
            }
            
            var config = Realm.Configuration.defaultConfiguration
            config.schemaVersion = 1
            config.syncConfiguration = SyncConfiguration(user: user, realmURL: syncURL)
            Realm.Configuration.defaultConfiguration = config
            
            DispatchQueue.main.async(execute: completion)
        }
    }
}


extension ViewController: NSTableViewDataSource {
    func numberOfRows(in tableView: NSTableView) -> Int {
        return scores.count
    }
}

extension ViewController: NSTableViewDelegate {
    func tableView(_ tableView: NSTableView, viewFor tableColumn: NSTableColumn?, row: Int) -> NSView? {
        guard let cell = tableView.make(
            withIdentifier: tableColumn!.identifier,
            owner: nil) as? NSTableCellView else {
            return nil
        }

        let score = scores[row]
        
        if tableColumn!.identifier == "question" {
            cell.textField?.stringValue = score.question
        } else if tableColumn!.identifier == "yesCount" {
            cell.textField?.stringValue = String(format: "%d", score.yesCount)
        } else if tableColumn!.identifier == "noCount" {
            cell.textField?.stringValue = String(format: "%d", score.noCount)
        }
        return cell
    }

    func tableView(_ tableView: NSTableView, heightOfRow row: Int) -> CGFloat {
        return 40.0
    }
}
