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


import UIKit

protocol AnswerDelegate {
    func onQuestionAnswered(questionId: String, answer: Bool)
}

class SurveyCell: UITableViewCell {
    
    @IBOutlet weak var questionLabel: UILabel!
    @IBOutlet weak var answerSegControl: UISegmentedControl!

    @IBAction func onAnswerQuestion() {
        if let ans = answer {
            answerDelegate?.onQuestionAnswered(
                questionId: questionId, answer: ans)
        }
    }
    
    var answerDelegate: AnswerDelegate?
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

    func showQuestion(questionModel: Question, delegate: AnswerDelegate? = nil) {
        questionId = questionModel.questionId
        question = questionModel.questionText
        answer = nil
        answerDelegate = delegate
    }

    

    
}
