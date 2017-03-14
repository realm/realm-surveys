import Foundation
import RealmSwift

let host = "" // <== Realm Object Server IP HERE
let serverURL = URL(string: "http://\(host):9080")!
let syncURL = URL(string: "realm://\(host):9080/~/survey")!

let user = "survey@demo.io"
let pass = "password"

func connect(completion: @escaping () -> Void) {
    
    DispatchQueue.main.async(execute: completion)

}
