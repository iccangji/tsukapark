const app = require('./app');
const { logger } = require('./utils/logger');
const { setupWebSocketServer } = require('./config/ws.config');
const { setupMqttClient, publishMqttClient } = require('./config/mqtt.client');

const HOSTNAME = '0.0.0.0';
const PORT = process.env.PORT || 3000;

const server = app.listen(PORT, HOSTNAME, () => {
    logger.info(`Running on PORT ${PORT}`);
});

let mqttData = {};
let orderData = {
    park1: "",
    park2: "",
    park3: "",
    park4: "",
    park5: "",
    park6: "",
};

const { wss, broadcastMqttData } = setupWebSocketServer(server, mqttData);

wss.on('connection', (ws) => {
    ws.on('message', (message) => {
        console.log(`Checkin message: ${message}`);
        const data = JSON.parse(message);
        orderData[data.park] = (data.checkin == true) ? data.email : "";
        console.log(orderData);
        publishMqttClient('mqtt://localhost', 'order/data', orderData)
    });

    setTimeout(() => {
        broadcastMqttData('sensor/data', mqttData);
        broadcastMqttData('order/data', orderData);
    }, 1000);

});

setupMqttClient('mqtt://localhost', ['sensor/data', 'order/data'], (topic, data) => {
    if (topic == 'sensor/data') {
        mqttData = data;
        broadcastMqttData(topic, mqttData);
    }
    else {
        broadcastMqttData(topic, orderData);
    }
});