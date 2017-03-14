import Foundation
import RealmSwift

let host = "" // <== Realm Object Server IP HERE
let serverURL = URL(string: "http://\(host):9080")!
let syncURL = URL(string: "realm://\(host):9080/~/survey")!

let user = "survey@demo.io"
let pass = "password"

func connect(completion: @escaping () -> Void) {

    DispatchQueue.main.async(execute: completion)

//    let cred = SyncCredentials.usernamePassword(
//        username: user, password: pass, register: false)
//    
//    SyncUser.logIn(with: cred, server: serverURL) { user, error in
//
//        guard let user = user else {
//            return DispatchQueue.main.asyncAfter(deadline: .now() + 5) {
//                connect(completion: completion)
//            }
//        }
//        
//        var config = Realm.Configuration.defaultConfiguration
//        config.schemaVersion = 1
//        config.syncConfiguration = SyncConfiguration(user: user, realmURL: syncURL)
//        Realm.Configuration.defaultConfiguration = config
//        
//        DispatchQueue.main.async(execute: completion)
//    }
    
}
