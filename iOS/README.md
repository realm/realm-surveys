# Realm-Surveys iOS

A simple survey client to receive and display questions entered in realtime and save user responses, designed to show the reactive nature of the [Realm Mobile Platform](https://realm.io/news/introducing-realm-mobile-platform/).

Any number of clients may be connect to answer questions in realtime.  Answered are tallied as soon as the question is answered.

This version is the iOS version.

## Installation Instructions

1. [Download the Realm Mobile Platform](https://realm.io/docs/realm-mobile-platform/get-started/) Developer Edition.
2. Run a local instance of the Realm Mobile Platform.
3. Create a new user, with the email `survey@demo.io` and the password `password`.
4. Update/download the required Cocoapods with `pod update` (Cocoapods installation instructions may be found on the [Cocoapods site](https://cocoapods.org))
5. Open the `RealmDraw.xcworkspace` with Xcode
6. Open the `RealmConnect.swift` file and set the host variable to the IP of your Realm Object Server.
7. Build the Draw app and deploy it to iOS device.
8. When app starts you will be automatically be logged in as survey@demo.io and be able to start drawing. The Realm Object server address you enter can be local or it can be an instance running on any of our other supported Linux platforms which may also be downloaded from [Realm](https://realm.io). In either case you should ensure your firewall allows access to ports 9080 and 27800 as these are needed by the application in order to communicate wth the Realm Object Server.
