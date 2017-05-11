# Realm-Surveys
A simple survey client to receive and display questions entered in realtime and save user responses, designed to show the reactive nature of the [Realm Mobile Platform](https://realm.io/news/introducing-realm-mobile-platform/).

Any number of clients may be connect to answer questions, entered by the admin app, in realtime.  Answered are tallied as soon as the question is answered.

![Images](Github-Image.png)

# Features

- [x] Stream questions as they stream in realtime.
- [x] Answer each question only once
- [x] Watch the results tally right away

# Requirements

* Realm Mobile Platform

# Setting Up Realm Mobile Platform

In order to properly use Realm Surveys, an instance of the Realm Object Server must be running for which each client copy of the app can connect.

* The [macOS version](https://realm.io/docs/get-started/installation/mac/) can be downloaded and run as a `localhost` on any desktop Mac.
* The [Linux version](https://realm.io/docs/get-started/installation/linux/) can be installed on a publicly accessible server and accessed over the internet.

# Building and Running

1. Start by running the Admin App for macOS, following the instructions in the README file in the [Admin macOS App](macOS) sub project.
1. Run the platform-specific client of your choice by following the instructions in the respective client sub project.

# Project Subdirectories

## Admin App

* [Admin macOS App](macOS)

## Client Apps

* [Android Java](Java)
* [Android Kotlin](Kotlin)
* [iOS](iOS)

# Known Issues

#### Android Versions
* Animates items moving out of the Recycler View, but when you answer a question in the middle of the list of questions, the animation fades out and back in questions that have moved up the list, instead of just moving them.  

# Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for more details!

This project adheres to the [Contributor Covenant Code of Conduct](https://realm.io/conduct/). By participating, you are expected to uphold this code. Please report unacceptable behavior to [info@realm.io](mailto:info@realm.io).

# License

Distributed under the Apache license. See ``LICENSE`` for more information.

[1]: https://realm.io/news/introducing-realm-mobile-platform/
[2]: https://realm.io/docs/get-started/installation/mac/
[3]: https://realm.io/docs/get-started/installation/linux/
[4]: https://github.com/realm-demos/realm-draw/tree/master/Android
[5]: https://github.com/realm-demos/realm-draw/tree/master/Apple
[6]: https://github.com/realm-demos/realm-draw/tree/master/Xamarin
