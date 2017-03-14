## Realm Surveys iOS demo script

### AppDelegate.swift

Show the `connect()` call in **AppDelegate**. 

============================

##### Set IP
RealmConnect.swift,  set `host` to IP of server

============================

### RealmConnect.swift, `connect()`, replace:
`swift
DispatchQueue.main.async(execute: completion)
`
with:

`swift
let cred = SyncCredentials.usernamePassword(
username: user, password: pass, register: false)

SyncUser.logIn(with: cred, server: serverURL) { user, error in

guard let user = user else {
return DispatchQueue.main.asyncAfter(deadline: .now() + 5) {
connect(completion: completion)
}
}

var config = Realm.Configuration.defaultConfiguration
config.schemaVersion = 1
config.syncConfiguration = SyncConfiguration(user: user, realmURL: syncURL)
Realm.Configuration.defaultConfiguration = config

DispatchQueue.main.async(execute: completion)
}
`

============================

##### Generate Models
Generate and Bring in Realm Models, delete Fake Models

============================


### SurveyTVC.swift

Replace

`swift
fileprivate var questions...
`

with:
`swift
var realm: Realm!
fileprivate var questions: Results<Question>!
fileprivate var questionsToken: NotificationToken?
`
============================

Append to viewDidLoad
`swift
realm = try! Realm() // handle gracefully without crashing in a real world scenario.
questions = realm.objects(Question.self)
.filter("answers.@count == 0 OR (SUBQUERY(answers, $a, $a.userId == %@).@count == 0)", UserDefaults.userId())
.sorted(byKeyPath: "timestamp")
`

============================

Add viewWillAppear/Disappear tokens

`swift
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
`

==============================

Append to onQuestionAnswered

`swift
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
`


