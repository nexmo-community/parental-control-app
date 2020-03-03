'use strict';
const express = require('express');
const bodyParser = require('body-parser');
const https = require('https');
const http = require('http');
var log = require('log-to-file');
var fs = require("fs");
const app = express();

// use it before all route definitions
//app.use(cors({origin: 'http://localhost:8000'}));
app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

var initServer = function() {
    log('______initServer______');
    var privateKey  = fs.readFileSync('server.key', 'utf8');
    var certificate = fs.readFileSync('server.crt', 'utf8');
    var credentials = {key: privateKey, cert: certificate};

    var httpServer = http.createServer(app);
    var httpsServer = https.createServer(credentials, app);

    httpServer.listen(8000);
    httpsServer.listen(8443);
}
initServer();
var proxyPhoneNumber = "XXXXXXXXXXXX";
var phoneNumberToCall = "XXXXXXXXXXXX";
var phoneNumberToOwner = "XXXXXXXXXXXX";

var nccoConnectToNumber =
    {
        action: 'connect',
        from: '',
        timeout: "45",
        endpoint: [
            {
                type: '',
                number: ''
            }
        ]
    };


function getNCCOForAnswer(req) {
    var from = req.query["from"];
    var isOutCall = from !== undefined && from !== "NONE" && from !== null && from === phoneNumberToOwner;
    log("from = ")
    log(from)
    log("phoneNumberToOwner = ");
    log(phoneNumberToOwner);
    log("phoneNumberToCall = ");
    log(phoneNumberToCall);
    var ncco = [];
    if (isOutCall) {
        nccoConnectToNumber.from = proxyPhoneNumber;
        nccoConnectToNumber.endpoint[0].number = phoneNumberToCall;
        nccoConnectToNumber.endpoint[0].type ="phone";
        ncco.push(nccoConnectToNumber);
    }
    else {
        nccoConnectToNumber.from = from;
        nccoConnectToNumber.endpoint[0].number = phoneNumberToOwner;
        nccoConnectToNumber.endpoint[0].type ="phone";
        ncco.push(nccoConnectToNumber);
    }
    return ncco;
}


app.post('/proxyPhoneNumber', (req, res) => {
    log("____________ PHONE TO CALL START _________________");
    proxyPhoneNumber = req.query["phone_number"];
    log("proxyPhoneNumber = ");
    log(proxyPhoneNumber);
    res.status(200).end();
    log("____________ PHONE TO CALL END _________________");
});

app.post('/phoneToCall', (req, res) => {
    log("____________ PHONE TO CALL START _________________");
    phoneNumberToCall = req.query["phone_number"];
    log("phoneNumberToCall = ");
    log(phoneNumberToCall);
    res.status(200).end();
    log("____________ PHONE TO CALL END _________________");
});

app.post('/phoneToOwner', (req, res) => {
    log("____________ PHONE TO OWNER START _________________");
    phoneNumberToOwner = req.query["phone_number"];
    log("phoneNumberToOwner = ");
    log(phoneNumberToOwner);
    res.status(200).end();
    log("____________ PHONE TO OWNER END _________________");
});


app.post('/event', (req, res) => {
    log("____________ EVENT START _________________");
    log(JSON.stringify(req.body));
//log(res);
    res.status(200).end();
    log("____________ EVENT FINISH _________________");
});


app.post('/events', (req, res) => {
    log("____________ EVENTS START _________________");
    log(JSON.stringify(req.body));
//log(res);
    res.status(200).end();
    log("____________ EVENTS FINISH _________________");
});

app.get('/answer', (req, res) => {
    log("____________ ANSWER START _________________");
    log(JSON.stringify(req.body));
    log(JSON.stringify(req.query));
// log(req);
// log(res);

    var ncco = getNCCOForAnswer(req);

    res.json(ncco);
    log(JSON.stringify(ncco));
    log("____________ ANSWER FINISH _________________");
});


app.get('/log', (req, res) => {
    var cwd = process.cwd();
    var text = fs.readFileSync("./default.log", "utf-8");
    var result = text.split('\n').join('<br/>') + cwd;
    res.send(result, null, 2);
});