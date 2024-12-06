const express = require('express');
const morgan = require('morgan');
const cors = require('cors');
const jwt = require('jsonwebtoken');
const path = require('path');
const authRoute = require('./routes/auth.route');
const { httpLogStream } = require('./utils/logger');
const { JWT_SECRET_KEY } = require('./utils/secrets');
const cookieParser = require('cookie-parser');
const { setupMqttClient, publishMqttClient } = require('./config/mqtt.client');
const { log } = require('console');
const { stringify } = require('querystring');
const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(morgan('dev'));
app.use(morgan('combined', { stream: httpLogStream }));
app.use(cors());
app.use(cookieParser());

const webPath = '../../frontend/web'
app.use('/', authRoute);
app.use(express.static(path.join(__dirname, webPath)));

app.get('/auth', (req, res) => {
    const token = req.cookies.authToken;
    if (!token) {
        return res.json({ isAuthenticated: false });
    }
    jwt.verify(token, JWT_SECRET_KEY, (err, decoded) => {
        if (err) {
            return res.redirect('/login');
        }
        const name = decoded.id;
        console.log(name);

        return res.json({ isAuthenticated: true, name: name });
    });
});

app.get('/login', (req, res) => {
    res.sendFile(path.join(__dirname, webPath, '/login.html'));
});

app.get('/signup', (req, res) => {
    res.sendFile(path.join(__dirname, webPath, '/signup.html'));
});

app.get('/signup', (req, res) => {
    res.sendFile(path.join(__dirname, webPath, '/signup.html'));
});

app.post('/order', (req, res) => {
    const { topic, message } = req.body;
    console.log(req.body);

    if (!topic || !message) {
        return res.status(400).json({ error: 'Topic dan message harus ada.' });
    }
    publishMqttClient('mqtt://localhost', 'order/data', stringify(message));

});

app.use((err, req, res, next) => {
    res.status(err.statusCode || 500).send({
        status: "error",
        message: err.message
    });
    next();
});

module.exports = app;