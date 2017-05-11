# Realm-Surveys macOS

A simple survey client to receive and display questions entered in realtime and save user responses, designed to show the reactive nature of the [Realm Mobile Platform](https://realm.io/news/introducing-realm-mobile-platform/).

Any number of clients may be connected to answer questions in realtime.  Answered are tallied as soon as the question is answered.

This version is the macOS Admin app that you use to enter questions.

## Installation Instructions

1. [Download the Realm Mobile Platform](https://realm.io/docs/realm-mobile-platform/get-started/) Developer Edition.
2. Run a local instance of the Realm Mobile Platform.
3. Create a new user, with the email `survey@demo.io` and the password `password`.
4. Update/download the required Cocoapods with `pod update` (Cocoapods installation instructions may be found on the [Cocoapods site](https://cocoapods.org))
5. Open a terminal window, change into the local project directory i.e.`cd <checkoutloction>/realm-surveys/macOS`
5. Install Realm Dependencies `pod install`.  (If you don't have cocoapods, you can install that using [these instructions](https://cocoapods.org/#install).
5. Open the `SurveyAdmin.xcworkspace` with Xcode
6. Open the `ViewController.swift` file and set the host variable to the IP of your Realm Object Server.
7. Build the Admin Survey app and run it.
8. When app starts you will be automatically be logged in as `survey@demo.io` and be able to entering questions.
