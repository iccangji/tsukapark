fetch('/auth', {
    method: 'GET',
    credentials: 'include'
})
    .then(response => response.json())
    .then(data => {
        if (data.isAuthenticated) {
            window.location.href = '/';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        window.location.href = '/login';
    });

document.getElementById('loginForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    console.log(JSON.stringify({ email, password }));

    fetch('/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => {
                    throw new Error(data.message);
                });
            }
            return response.json();
        })
        .then(data => {
            window.location.href = '/';
        })
        .catch(error => {
            const alertLogin = document.getElementById('alertLogin');
            alertLogin.style.setProperty('display', 'block', 'important');
        });
});