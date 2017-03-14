import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        
        connect { [unowned self] in
            if let splash = self.window?.rootViewController,
                let rootVc = splash.storyboard?.instantiateViewController(withIdentifier: "Navigation") {
                self.window!.rootViewController = rootVc
            }
        }
        
        return true
    }

}

