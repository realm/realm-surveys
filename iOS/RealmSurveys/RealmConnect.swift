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

let host = "107.170.21.92" // <-- Enter your Realm Object Server IP here
let serverURL = URL(string: "http://\(host):9080")!
let syncURL = URL(string: "realm://\(host):9080/~/survey")!

let user = "survey@demo.io"
let pass = "password"

func connect(completion: @escaping () -> Void) {
    
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
    
}
