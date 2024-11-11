function toggleDropdown(event) {
    event.preventDefault(); // Prevent default anchor click behavior
    const dropdown = document.getElementById("accountDropdown");
    dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
    if (!event.target.matches('.dropdown a')) {
        const dropdowns = document.getElementsByClassName("dropdown-menu");
        for (let i = 0; i < dropdowns.length; i++) {
            const openDropdown = dropdowns[i];
            if (openDropdown.style.display === "block") {
                openDropdown.style.display = "none";
            }
        }
    }
};
// Show the delete confirmation popup
function showDeletePopup() {
    document.getElementById("deletePopup").style.display = "flex";
}

// Hide the delete confirmation popup
function hideDeletePopup() {
    document.getElementById("deletePopup").style.display = "none";
}

// Handle Delete Account option click
document.querySelector("#accountDropdown a[href='#delete-account']").onclick = function(event) {
    event.preventDefault(); // Prevent default link behavior
    showDeletePopup();
};

// Handle "Yes" button in popup
document.getElementById("confirmDelete").onclick = function() {
    // Add logic to delete the account
    alert("Account deleted"); // Placeholder action
    hideDeletePopup();
};

// Handle "No" button in popup
document.getElementById("cancelDelete").onclick = function() {
    hideDeletePopup();
};
