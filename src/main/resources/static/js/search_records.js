// Change the placeholder in the search input based on selected criteria
function changePlaceholder() {
    const searchCriteria = document.getElementById("searchCriteria").value;
    const searchInput = document.getElementById("searchInput");

    if (searchCriteria === "email") {
        searchInput.placeholder = "Enter Email";
    } else if (searchCriteria === "phone") {
        searchInput.placeholder = "Enter Phone Number";
    } else {
        searchInput.placeholder = "Enter Username";
    }
}

// Dummy data to simulate search results
const dummyData = {
    "john@example.com": [
        { fileName: "record1.pdf", fullName: "John Doe" },
        { fileName: "record2.pdf", fullName: "John Doe" }
    ],
    "jane@example.com": [
        { fileName: "record3.pdf", fullName: "Jane Smith" }
    ],
    "jdoe": [
        { fileName: "record1.pdf", fullName: "John Doe" }
    ]
};

// Search records based on selected criteria and input value
function searchRecords() {
    const searchCriteria = document.getElementById("searchCriteria").value;
    const searchInput = document.getElementById("searchInput").value;
    const patientRecordsSelect = document.getElementById("patientRecords");

    // Clear previous search results
    patientRecordsSelect.innerHTML = '<option value="">Select a record</option>';

    // Simulate a search in the dummy data
    const searchKey = searchInput.toLowerCase();
    const records = dummyData[searchKey] || [];

    if (records.length > 0) {
        records.forEach(record => {
            const option = document.createElement("option");
            option.value = record.fileName;
            option.textContent = `${record.fullName} - ${record.fileName}`;
            patientRecordsSelect.appendChild(option);
        });
    } else {
        alert("No records found for the entered search criteria.");
    }

    // Display PDF viewer if records are found
    document.querySelector(".pdf-viewer-container").style.display = records.length ? "block" : "none";
}

// Display selected PDF in the iframe
function displayPDF() {
    const patientRecordsSelect = document.getElementById("patientRecords");
    const selectedFile = patientRecordsSelect.value;
    const pdfViewer = document.getElementById("pdfViewer");

    if (selectedFile) {
        pdfViewer.src = selectedFile;
    }
}

// Download selected PDF
function downloadPDF() {
    const patientRecordsSelect = document.getElementById("patientRecords");
    const selectedFile = patientRecordsSelect.value;

    if (selectedFile) {
        const link = document.createElement("a");
        link.href = selectedFile;
        link.download = selectedFile;
        link.click();
    } else {
        alert("Please select a record to download.");
    }
}
