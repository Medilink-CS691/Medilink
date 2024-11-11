package com.example.MediLink.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "prescription")
public class Prescription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "prescription_name")
    private String fileName;
    
    @Column(name = "prescription_type")
    private String fileType;
    
    @Lob
    @Column(name = "prescription_data", columnDefinition = "LONGBLOB")
    private byte[] fileData;

    @Column(name = "uploaded_at")
    private Timestamp uploadedAt;

    @Column(name = "username") // New field for username
    private String username;

    @Column(name = "doctor_license") // New field for username
    private String doctorLicense;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getUsername() {
        return username; // Getter for username
    }

    public void setUsername(String username) {
        this.username = username; // Setter for username
    }

    public String getDoctorLicense() {
        return doctorLicense; // Getter for username
    }

    public void setDoctorLicense(String doctorLicense) {
        this.doctorLicense = doctorLicense; // Setter for username
    }
}
