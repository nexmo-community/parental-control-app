# parental-control-app

Sample code for building a voice parental control application

## Server

The server is a simple Express based server, to run it all you need to do is

`npm install && npm run start`

Before running you'll need to copy example.env to .env and fill in the required paramters:

1. VONAGE_PROXY_NUMBER: The Vonage number you've purchased and linked to your application. Can be found on the [dashboard](https://dashboard.nexmo.com/applications/) under the application you're using.
