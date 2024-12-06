fetch('/auth', {
    method: 'GET',
    credentials: 'include'
})
    .then(response => response.json())
    .then(data => {
        if (!data.isAuthenticated) {
            window.location.href = '/login';
        }
        else {
            document.getElementById('lastName').textContent = data.name;
            document.getElementById('main-wrapper').style.setProperty('display', 'block', 'important');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        window.location.href = '/login';
    });


async function publishData(index) {

    fetch("http://localhost:3000/publish", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ topic, message }),
    })
        .then((response) => response.json())
        .then((data) => {
            if (data.success) {
                alert("Pesan berhasil dipublish!");
            } else {
                alert("Gagal publish pesan.");
            }
        })
        .catch((error) => console.error("Error:", error));
}
