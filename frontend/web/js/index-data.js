let parkIndex = 0;
const wsData = {
    'sensor': {
        'park1': false,
        'park2': false,
        'park3': false,
        'park4': false,
        'park5': false,
        'park6': false
    },
    'order': {
        'park1': false,
        'park2': false,
        'park3': false,
        'park4': false,
        'park5': false,
        'park6': false
    }
};

document.getElementById('logoutBtn').addEventListener('click', function () {
    fetch('/logout', {
        method: 'POST',
        credentials: 'include', // Sertakan cookie dalam permintaan
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/login';
            } else {
                return response.json().then(data => {
                    console.error('Error:', data.message);
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});

const ws = new WebSocket('ws://localhost:3000');
const park1 = document.getElementById('park1');
const parkStatus1 = document.getElementById('parkStatus1');
const park2 = document.getElementById('park2');
const parkStatus2 = document.getElementById('parkStatus2');
const park3 = document.getElementById('park3');
const parkStatus3 = document.getElementById('parkStatus3');
const park4 = document.getElementById('park4');
const parkStatus4 = document.getElementById('parkStatus4');
const park5 = document.getElementById('park5');
const parkStatus5 = document.getElementById('parkStatus5');
const park6 = document.getElementById('park6');
const parkStatus6 = document.getElementById('parkStatus6');

const connectionStatus = document.getElementById('connectionStatus');
const connectionStatus2 = document.getElementById('connectionStatus2');

ws.onmessage = (event) => {
    connectionStatus.classList.remove("bg-secondary-subtle");
    connectionStatus.classList.add("bg-success");
    connectionStatus.classList.add("text-light");
    connectionStatus2.classList.remove("bg-secondary-subtle");
    connectionStatus2.classList.add("bg-success");
    connectionStatus2.classList.add("text-light");


    const topic = JSON.parse(event.data).topic;
    const data = JSON.parse(event.data).data;
    const keyWs = topic == 'sensor/data' ? 'sensor' : 'order';
    wsData[keyWs] = data;
    if (wsData['sensor']['park1']) {
        park1.classList.remove("btn-danger");
        park1.classList.add("btn-success");
        parkStatus1.textContent = "Terisi"
    } else {
        park1.classList.remove("btn-success");
        park1.classList.add("btn-danger");
        parkStatus1.textContent = "Kosong"
    }
    parkStatus1.textContent += wsData['order']['park1'] ? " (Dipesan)" : ""

    if (wsData['sensor']['park2']) {
        park2.classList.remove("btn-danger");
        park2.classList.add("btn-success");
        parkStatus2.textContent = "Terisi"
    } else {
        park2.classList.remove("btn-success");
        park2.classList.add("btn-danger");
        parkStatus2.textContent = "Kosong"
    }
    parkStatus2.textContent += wsData['order']['park2'] ? " (Dipesan)" : ""

    if (wsData['sensor']['park3']) {
        park3.classList.remove("btn-danger");
        park3.classList.add("btn-success");
        parkStatus3.textContent = "Terisi"
    } else {
        park3.classList.remove("btn-success");
        park3.classList.add("btn-danger");
        parkStatus3.textContent = "Kosong"
    }
    parkStatus3.textContent += wsData['order']['park3'] ? " (Dipesan)" : ""

    if (wsData['sensor']['park4']) {
        park4.classList.remove("btn-danger");
        park4.classList.add("btn-success");
        parkStatus4.textContent = "Terisi"
    } else {
        park4.classList.remove("btn-success");
        park4.classList.add("btn-danger");
        parkStatus4.textContent = "Kosong"
    }
    parkStatus4.textContent += wsData['order']['park4'] ? " (Dipesan)" : ""

    if (wsData['sensor']['park5']) {
        park5.classList.remove("btn-danger");
        park5.classList.add("btn-success");
        parkStatus5.textContent = "Terisi"
    } else {
        park5.classList.remove("btn-success");
        park5.classList.add("btn-danger");
        parkStatus5.textContent = "Kosong"
    }
    parkStatus5.textContent += wsData['order']['park5'] ? " (Dipesan)" : ""

    if (wsData['sensor']['park6']) {
        park6.classList.remove("btn-danger");
        park6.classList.add("btn-success");
        parkStatus6.textContent = "Terisi"
    } else {
        park6.classList.remove("btn-success");
        park6.classList.add("btn-danger");
        parkStatus6.textContent = "Kosong"
    }
    parkStatus6.textContent += wsData['order']['park6'] ? " (Dipesan)" : ""


};

document.querySelectorAll(".btn-modal").forEach((button) => {
    button.addEventListener("click", () => {
        const message = button.getAttribute("data-bs-message");
        parkIndex = message;
        document.getElementById("exampleModalLabel").textContent = `Isi parkiran 00${message}?`;
    });
});

document.getElementById("order-park").addEventListener("click", async () => {
    const orderIndex = `park${parkIndex}`
    wsData['order'][orderIndex] = true;
    try {
        const response = await fetch('http://localhost:3000/order', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "topic": "order/data",
                "message": wsData['order'],
            }),
        });

        // const data = await response.json();

        // if (response.ok) {
        //     // Tampilkan hasil jika berhasil
        //     responseMessage.textContent = `Data berhasil dipublikasikan ke topic ${data.topic}: ${data.message}`;
        //     responseMessage.style.color = 'green';
        // } else {
        //     // Tampilkan pesan error jika gagal
        //     responseMessage.textContent = `Error: ${data.error}`;
        //     responseMessage.style.color = 'red';
        // }
    } catch (error) {
        // Tangani kesalahan jika request gagal
        console.log('Gagal: ', error);

    }
})


