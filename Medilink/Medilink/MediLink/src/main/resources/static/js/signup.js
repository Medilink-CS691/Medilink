document.addEventListener('DOMContentLoaded', () => {
    const steps = document.querySelectorAll('.form-step');
    let currentStep = 0;

    // Regex patterns for validation
    const nameRegex = /^[A-Za-z]+$/;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^\d{10}$/; // Regex to validate unformatted 10-digit phone number
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    const zipRegex = /^\d{5}$/;

    // Show the initial step
    steps[currentStep].classList.add('active');

    // Phone number input setup
    const phoneNumberInput = document.getElementById('phone-number');
    phoneNumberInput.dataset.rawValue = ""; // Initialize raw value storage

    // Handle phone number formatting on input
    phoneNumberInput.addEventListener('input', (event) => {
        const input = event.target.value.replace(/\D/g, ''); // Remove all non-numeric characters
        phoneNumberInput.dataset.rawValue = input; // Store the raw value
        event.target.value = formatPhoneNumber(input); // Format the phone number
    });

    // Format the phone number
    /*
    function formatPhoneNumber(input) {
        if (input.length > 10) {
            input = input.slice(0, 10); // Limit input to 10 digits
        }
        const areaCode = input.slice(0, 3);
        const firstPart = input.slice(3, 6);
        const secondPart = input.slice(6, 10);

        if (input.length > 6) {
            return `(${areaCode}) ${firstPart}-${secondPart}`;
        } else if (input.length > 3) {
            return `(${areaCode}) ${firstPart}`;
        } else if (input.length > 0) {
            return `(${areaCode}`;
        }
        return input;
    }*/

    // Function to show error messages
    function showError(inputId, message) {
        const errorSpan = document.getElementById(`${inputId}-error`);
        errorSpan.textContent = message;
    }

    // Function to clear error messages
    function clearError(inputId) {
        const errorSpan = document.getElementById(`${inputId}-error`);
        errorSpan.textContent = '';
    }

    // Validate Step 1: Basic Information
    function validateStep1() {
        let isValid = true;

        // Validate Email
        const email = document.getElementById('email').value.trim();
        if (!emailRegex.test(email)) {
            showError('email', 'Please enter a valid email address.');
            isValid = false;
        } else {
            clearError('email');
        }

        // Validate Phone Number (unformatted)
        const phoneNumber = phoneNumberInput.dataset.rawValue; // Get unformatted number
        if (!phoneRegex.test(phoneNumber)) {
            showError('phone-number', 'Phone number must be 10 digits.');
            isValid = false;
        } else {
            clearError('phone-number');
        }

        // Validate Password
        const password = document.getElementById('password').value.trim();
        if (!passwordRegex.test(password)) {
            showError('password', 'Password must be at least 8 characters long, with 1 uppercase, 1 lowercase, 1 number, and 1 special character.');
            isValid = false;
        } else {
            clearError('password');
        }

        return isValid;
    }

    // Validate Step 2: Personal Information
    function validateStep2() {
        let isValid = true;

        // Validate First Name
        const firstName = document.getElementById('first-name').value.trim();
        if (!nameRegex.test(firstName)) {
            showError('first-name', 'First Name should only contain alphabets.');
            isValid = false;
        } else {
            clearError('first-name');
        }

        // Validate Middle Name (Optional)
        const middleName = document.getElementById('middle-name').value.trim();
        if (middleName && !nameRegex.test(middleName)) {
            showError('middle-name', 'Middle Name should only contain alphabets.');
            isValid = false;
        } else {
            clearError('middle-name');
        }

        // Validate Last Name
        const lastName = document.getElementById('last-name').value.trim();
        if (!nameRegex.test(lastName)) {
            showError('last-name', 'Last Name should only contain alphabets.');
            isValid = false;
        } else {
            clearError('last-name');
        }

        // Validate Age
        const age = document.getElementById('age').value.trim();
        if (isNaN(age) || age <= 0) {
            showError('age', 'Please enter a valid age.');
            isValid = false;
        } else {
            clearError('age');
        }

        return isValid;
    }

    // Validate Step 3: Address Information
    function validateStep3() {
        let isValid = true;

        // Validate ZIP Code
        const zipcode = document.getElementById('zipcode').value.trim();
        if (!zipRegex.test(zipcode)) {
            showError('zipcode', 'ZIP Code must be exactly 5 digits.');
            isValid = false;
        } else {
            clearError('zipcode');
        }

        // Check if Terms are Accepted
        const termsCheckbox = document.getElementById('terms');
        if (!termsCheckbox.checked) {
            showError('terms', 'You must agree to the terms and conditions.');
            isValid = false;
        } else {
            clearError('terms');
        }

        return isValid;
    }

    // Next Button Handling
    const nextBtns = document.querySelectorAll('.next-btn');
    nextBtns.forEach((btn, index) => {
        btn.addEventListener('click', () => {
            let isStepValid = false;

            // Validate current step before moving to the next
            if (currentStep === 0) {
                isStepValid = validateStep1();
            } else if (currentStep === 1) {
                isStepValid = validateStep2();
            }

            if (isStepValid) {
                steps[currentStep].classList.remove('active');
                currentStep++;
                steps[currentStep].classList.add('active');
            }
        });
    });

    // Previous Button Handling
    const prevBtns = document.querySelectorAll('.prev-btn');
    prevBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            steps[currentStep].classList.remove('active');
            currentStep--;
            steps[currentStep].classList.add('active');
        });
    });

    // Handle Form Submission
    const form = document.querySelector('#signup-form');
    form.addEventListener('submit', (event) => {
        event.preventDefault(); // Prevent form submission for validation

        if (validateStep3()) {
            alert('Account created successfully!');
            // Here, you would typically submit the form data to the server
            event.target.submit();
        }
    });
});
