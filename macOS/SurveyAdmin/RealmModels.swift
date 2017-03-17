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

