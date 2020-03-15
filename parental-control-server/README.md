# parental-control-server

Sample code for building a voice webhook for a parental control application

## Setup

Copy example.env to .env and fill in the required parameters:

1. VONAGE_PROXY_NUMBER: The Vonage number you've purchased and linked to your application. Can be found on the [dashboard](https://dashboard.nexmo.com/applications/) under the application you're using.
2. VONAGE_PROXY_NUMBER_LOCAL: The Vonage number above but in a local format, for example - in Israel 9723XXXXXXX should be written as 03XXXXXXX. This is how you want your Android device to dial it.

## Run

`npm install && npm run start`

## Events

Events from the Voice API should be printed to the console and could be used for debugging and tracking your calls
