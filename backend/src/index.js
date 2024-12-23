const app = require('./app');
const { logger } = require('./utils/logger');
const { setupWebSocketServer } = require('./config/ws.config');
const { setupMqttClient, publishMqttClient } = require('./config/mqtt.client');
const { default: mqtt } = require('mqtt');

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
        // console.log(orderData);

        let orderDataMqtt = {};
        let counter = 1;
        for (let key in orderData) {
            orderDataMqtt[`order${counter}`] = orderData[key] != "";
            counter++;
        }

        console.log(orderDataMqtt);
        publishMqttClient('mqtt://localhost', 'order/data', orderDataMqtt);
    });

    setTimeout(() => {
        broadcastMqttData('sensor/data', mqttData);
        broadcastMqttData('order/data', orderData);
    }, 1000);

});

function transpose(data) {
    let dataTransposed = {};
    const newOrder = ["park1", "park3", "park5", "park2", "park4", "park6"];
    const keys = Object.keys(data);
    newOrder.forEach((key, index) => {
        if (index < keys.length) {
            dataTransposed[key] = data[keys[index]];
        }
    });

    return dataTransposed;
}
setupMqttClient('mqtt://localhost', ['sensor/data', 'order/data'], (topic, data) => {
    if (topic == 'sensor/data') {
        // mqttData = data;
        mqttData = transpose(data);
        // console.log(mqttData);
        broadcastMqttData(topic, mqttData);
    }
    else {
        broadcastMqttData(topic, transpose(orderData));
    }
});