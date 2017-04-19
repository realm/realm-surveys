# Realm-Surveys Android

A simple survey client to receive and display questions entered in realtime and save user responses, designed to show the reactive nature of the [Realm Mobile Platform](https://realm.io/news/introducing-realm-mobile-platform/).

Any number of clients may be connect to answer questions in realtime.  Answered are tallied as soon as the question is answered.

This version is the Android version.

## Installation Instructions

1. [Download the Realm Mobile Platform](https://realm.io/docs/realm-mobile-platform/get-started/) Developer Edition.
2. Run a local instance of the Realm Mobile Platform.
3. Create a new user, with the email `survey@demo.io` and the password `password`.
4. Open the Android Project located at `Realm-Surveys/Android` with Android Studio
5. Open the `SplashActivity.java` file.  Inside there is host variable that you need to set to your Realm Object Server IP.
6. Build the Survey app and deploy it to an Android device.
7. When app starts you will be automatically be logged in as survey@demo.io and be able to start drawing. The Realm Object server address you enter can be local or it can be an instance running on any of our other supported Linux platforms which may also be downloaded from [Realm](https://realm.io). In either case you should ensure your firewall allows access to ports 9080 and 27800 as these are needed by the application in order to communicate wth the Realm Object Server.
