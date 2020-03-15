# parental-control-app

Sample code for building an android parental control application

## Setup

Fill in the SERVER_ADDRESS in "./app/src/main/res/values/strings.xml", you can use [ngrok](https://ngrok.com/) to expose your server

## Run

`install on any Android device with Android 10+`

## User guide

1. Install the application on your Android device and open it
2. Set it up by entering the device’s phone number
3. Press ‘enable’. You should see a prompt that asks you to grant the redirection permission to the application, please accept.
4. Call any number, using the native dialer, and your call will be proxied.
5. To follow the stream of events, take a look at the [webhook’s](../parental-control-server/README.md) logs!
