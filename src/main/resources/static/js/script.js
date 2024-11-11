document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("signup-form");
    const emailInput = document.getElementById("email");
    const phoneInput = document.getElementById("phone-number");
    const licenseInput = document.getElementById("license");
    const successMessage = document.getElementById("successMessage");

    // Scroll to the top of the form on load
    window.scrollTo(0, 0); // Ensure the top of the page is visible

    async function checkUnique(field, value, errorElement) {
        const url = `/api/DoctorAccounts/check-${field}?${field}=${encodeURIComponent(value)}`;
        try {
            const response = await fetch(url);
            const exists = await response.json();
            if (exists) {
                errorElement.textContent = `${field.charAt(0).toUpperCase() + field.slice(1)} is already registered`;
                errorElement.style.color = "red";
            } else {
                errorElement.textContent = `${field.charAt(0).toUpperCase() + field.slice(1)} is available`;
                errorElement.style.color = "green";
            }
        } catch (error) {
            errorElement.textContent = "Error checking uniqueness. Please try again.";
            errorElement.style.color = "red";
            console.error("Error:", error);
        }
    }

    emailInput.addEventListener("blur", () => checkUnique("email", emailInput.value, document.getElementById("email-error")));
    licenseInput.addEventListener("blur", () => checkUnique("license", licenseInput.value, document.getElementById("license-error")));

    // Add phone validation check on blur
    phoneInput.addEventListener("blur", () => checkUnique("phone", phoneInput.value.replace(/\D/g, ""), document.getElementById("phone-error")));

    phoneInput.addEventListener("input", () => {
        // Store the raw value without formatting
        phoneInput.dataset.rawValue = phoneInput.value.replace(/\D/g, ""); // Store only numeric characters
    });

    form.addEventListener("submit", async (event) => {
        event.preventDefault(); // Prevent form submission for validation

        // Set the phone input to its raw value before submission
        phoneInput.value = phoneInput.dataset.rawValue; // Only raw number is stored

        // Check for existing error messages
        const emailError = document.getElementById("email-error").textContent;
        const phoneError = document.getElementById("phone-error").textContent;
        const licenseError = document.getElementById("license-error").textContent;

        if (emailError || phoneError || licenseError) {
            // Prevent submission if there are validation errors
            return;
        }

        // Submit the form data and handle the response
        try {
            const response = await fetch(form.action, {
                method: 'POST',
                body: new FormData(form),
            });

            if (response.ok) {
                successMessage.textContent = "Account created successfully!";
                successMessage.style.display = "block";
                form.reset(); // Reset form after successful submission
            } else {
                const errorText = await response.text();
                document.getElementById("errorMessage").textContent = errorText || "An error occurred. Please try again.";
                document.getElementById("errorMessage").style.display = "block";
            }
        } catch (error) {
            console.error("Error:", error);
            document.getElementById("errorMessage").textContent = "Network error. Please try again.";
            document.getElementById("errorMessage").style.display = "block";
        }
    });
});
