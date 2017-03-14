import Foundation

extension UserDefaults {
    static private let defaultsUserKey = "defaultsUserKey"

    static func userId() -> String {
        return standard.value(forKey: defaultsUserKey) as? String ?? {
            let id = UUID().uuidString
            standard.setValue(id, forKey: defaultsUserKey)
            standard.synchronize()
            return id
        }()
    }
}
