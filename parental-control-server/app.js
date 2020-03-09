require('dotenv').config()
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');

const indexRouter = require('./routes/index');
const answerRouter = require('./routes/answer');
const proxyRouter = require('./routes/proxy');
const eventRouter = require('./routes/event'); 
const proxyService = (require('./services/proxy'))(); 

const app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/answer', answerRouter(proxyService)); 
app.use('/event', eventRouter); 
app.use('/proxy', proxyRouter(proxyService)); 

module.exports = app;
