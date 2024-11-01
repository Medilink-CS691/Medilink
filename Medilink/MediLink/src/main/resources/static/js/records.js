let selectedPdfUrl = '';

// Function to display the selected PDF in the iframe
function viewPdf(pdfUrl) {
    if (pdfUrl) {
        selectedPdfUrl = pdfUrl; // Store the selected PDF URL for download
        document.getElementById('pdfFrame').src = pdfUrl;
        document.getElementById('downloadBtn').style.display = 'inline-block'; // Show the download button
    } else {
        selectedPdfUrl = '';
        document.getElementById('pdfFrame').src = '';
        document.getElementById('downloadBtn').style.display = 'none'; // Hide the download button
    }
}

// Function to auto-select and open the first available option
window.onload = function() {
    const dropdown = document.getElementById('pdfDropdown');
    if (dropdown.options.length > 1) { // Ensure there's at least one valid file option
        dropdown.selectedIndex = 1; // Select the first valid option (index 1 because index 0 is the placeholder)
        viewPdf(dropdown.options[1].value); // Automatically open the first available PDF
    }
};

// Function to download the currently selected PDF
function downloadPdf() {
    if (selectedPdfUrl) {
        window.location.href = selectedPdfUrl; // Trigger file download
    }
}
