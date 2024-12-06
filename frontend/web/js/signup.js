document.getElementById('signupForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const firstname = document.getElementById('firstName').value;
    const lastname = document.getElementById('lastName').value;

    // console.log(JSON.stringify({ firstname, lastname, email, password }));

    fetch('/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ firstname, lastname, email, password }),
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
            window.location.href = '/login';
        })
        .catch(error => {
            console.log(error);
        });
});