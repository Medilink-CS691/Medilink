function showLogin(type) {
    const patientLogin = document.getElementById('patient-login');
    const doctorLogin = document.getElementById('doctor-login');
    const patientBtn = document.getElementById('patient-btn');
    const doctorBtn = document.getElementById('doctor-btn');

    if (type === 'patient') {
        patientLogin.classList.remove('hidden');
        doctorLogin.classList.add('hidden');
        patientBtn.classList.add('active');
        doctorBtn.classList.remove('active');
    } else {
        doctorLogin.classList.remove('hidden');
        patientLogin.classList.add('hidden');
        doctorBtn.classList.add('active');
        patientBtn.classList.remove('active');
    }
}

// Validate login inputs
function validateForm(type) {
    let username, password;
    if (type === 'patient') {
        username = document.querySelector('#patient-login input[type="text"]').value;
        password = document.querySelector('#patient-login input[type="password"]').value;
    } else if (type === 'doctor') {
        username = document.querySelector('#doctor-login input[type="text"]').value;
        password = document.querySelector('#doctor-login input[type="password"]').value;
    }

    if (username === '' || password === '') {
        alert('Please fill in all fields.');
        return false;
    }

    if (password.length < 6) {
        alert('Password must be at least 6 characters.');
        return false;
    }

    return true;
}

/*
// Handle patient login form submission
document.querySelector('#patient-login button[type="submit"]').addEventListener('click', function (event) {
    event.preventDefault();
    if (validateForm('patient')) {
        // If valid, send login request to the server
        login('patient');
    }
});

// Handle doctor login form submission
document.querySelector('#doctor-login button[type="submit"]').addEventListener('click', function (event) {
    event.preventDefault();
    if (validateForm('doctor')) {
        // If valid, send login request to the server
        login('doctor');
    }
});

// Send login request to the server

function login(type) {
    let username, password;
    if (type === 'patient') {
        username = document.querySelector('#patient-login input[type="text"]').value;
        password = document.querySelector('#patient-login input[type="password"]').value;
    } else {
        username = document.querySelector('#doctor-login input[type="text"]').value;
        password = document.querySelector('#doctor-login input[type="password"]').value;
    }

    // Create the payload to send to the server
    const loginData = {
        username: username,
        password: password,
        userType: type
    };

    // Send the login request via Fetch API
    fetch('/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('Login successful!');
            // Redirect to dashboard or home page
            window.location.href = '/dashboard';
        } else {
            alert(data.message || 'Login failed. Please try again.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    });

}*/
