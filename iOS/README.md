# Realm-Surveys iOS

An iOS version of the [Realm Surveys Demo App](https://github.com/realm-demos/realm-surveys)

## Installation Instructions

1. Follow instructions for installing and running the [Realm Admin App](../macOS)
5. Open a terminal window, change into the local project directory i.e.`cd <checkoutloction>/realm-surveys/iOS`
5. Install Realm Dependencies `pod install`.  (If you don't have cocoapods, you can install that using [these instructions](https://cocoapods.org/#install).
5. Open the `RealmSurveys.xcworkspace` with Xcode
6. Open the `RealmConnect.swift` file and set the host variable to the IP of your Realm Object Server.
7. Build the RealmSurvey app and deploy it to iOS device.
8. When app starts you will be automatically be logged in as `survey@demo.io` and be able to start answering questions entered by the admin.
