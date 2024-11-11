document.addEventListener('DOMContentLoaded', () => {
    // Regex patterns for validation
    const nameRegex = /^[A-Za-z]+$/;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^\d{10}$/; // Raw phone number format (10 digits)
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    const zipRegex = /^\d{5}$/;

    // Show error messages
    function showError(inputId, message) {
        const errorSpan = document.getElementById(`${inputId}-message`);
        if (errorSpan) {
            errorSpan.textContent = message;
            errorSpan.classList.add("error-message");
        }
    }

    // Clear error messages
    function clearError(inputId) {
        const errorSpan = document.getElementById(`${inputId}-message`);
        if (errorSpan) {
            errorSpan.textContent = '';
        }
    }

    // Form validation
    function validateForm() {
        let isValid = true;

        // Validate First Name
        const firstName = document.getElementById('first-name')?.value.trim();
        if (firstName && !nameRegex.test(firstName)) {
            showError('first-name', 'First Name should only contain alphabets.');
            isValid = false;
        } else {
            clearError('first-name');
        }

        // Validate Middle Name (Optional)
        const middleName = document.getElementById('middle-name')?.value.trim();
        if (middleName && !nameRegex.test(middleName)) {
            showError('middle-name', 'Middle Name should only contain alphabets.');
            isValid = false;
        } else {
            clearError('middle-name');
        }

        // Validate Last Name
        const lastName = document.getElementById('last-name')?.value.trim();
        if (lastName && !nameRegex.test(lastName)) {
            showError('last-name', 'Last Name should only contain alphabets.');
            isValid = false;
        } else {
            clearError('last-name');
        }

        // Validate Email
        const email = document.getElementById('email')?.value.trim();
        if (email && !emailRegex.test(email)) {
            showError('email', 'Please enter a valid email address.');
            isValid = false;
        } else {
            clearError('email');
        }

        // Validate Phone Number (Raw numeric)
        const phoneNumber = document.getElementById('phone-number')?.value.trim().replace(/\D/g, ''); // Remove non-numeric characters
        if (phoneNumber && !phoneRegex.test(phoneNumber)) {
            showError('phone-number', 'Phone number must be exactly 10 digits.');
            isValid = false;
        } else {
            clearError('phone-number');
        }

        // Validate Password
        const password = document.getElementById('password')?.value.trim();
        if (password && !passwordRegex.test(password)) {
            showError('password', 'Password must be at least 8 characters long, with 1 uppercase, 1 lowercase, 1 number, and 1 special character.');
            isValid = false;
        } else {
            clearError('password');
        }

        // Validate ZIP Code
        const zipcode = document.getElementById('zipcode')?.value.trim();
        if (zipcode && !zipRegex.test(zipcode)) {
            showError('zipcode', 'ZIP Code must be exactly 5 digits.');
            isValid = false;
        } else {
            clearError('zipcode');
        }

        return isValid;
    }

    // Handle form submission
    const form = document.getElementById('signup-form'); // Ensure this matches your form ID
    if (form) {
        form.addEventListener('submit', (event) => {
            event.preventDefault(); // Prevent form submission for validation

            if (validateForm()) {
                showSuccessMessage(); // Show success message
                form.submit(); // Submit the form if validation passes
            }
        });
    }

    // Success message function
    function showSuccessMessage() {
        alert("Update information successful!");
    }
});
