import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        
        connect { [unowned self] in
            if let splash = self.window?.rootViewController,
                let tvc = splash.storyboard?.instantiateViewController(withIdentifier: "Navigation") {
                self.window!.rootViewController = tvc
            }
        }
        
        return true
    }

}

