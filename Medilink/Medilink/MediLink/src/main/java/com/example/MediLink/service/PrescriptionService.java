package com.example.MediLink.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.MediLink.model.PatientAccount;
import com.example.MediLink.model.Prescription;
import com.example.MediLink.repository.PatientAccountRepository;
import com.example.MediLink.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository PrescriptionRepository;

    @Autowired
    private PatientAccountRepository patientAccountRepository;

    public Prescription saveFile(MultipartFile file, String username, String doctorLicense) throws IOException {
        Prescription fileEntity = new Prescription();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileType(file.getContentType());
        fileEntity.setFileData(file.getBytes());
        fileEntity.setUsername(username); // Set the username in the entity
        fileEntity.setDoctorLicense(doctorLicense);
        return PrescriptionRepository.save(fileEntity);
    }

    public List<Prescription> getAllFiles() {
        return PrescriptionRepository.findAll();
    }

    public List<Prescription> getFilesByUsername(String username) {
        return PrescriptionRepository.findByUsername(username); // Assuming this method exists in your repository
    }

    public Optional<Prescription> getFile(Long fileId) {
        return PrescriptionRepository.findById(fileId);
    }

    public List<Prescription> getFilesByEmail(String email) {
        Optional<PatientAccount> patientAccount = patientAccountRepository.findByEmail(email);
        if (patientAccount.isPresent()) {
            String username = patientAccount.get().getUsername();
            return getFilesByUsername(username);
        } else {
            throw new IllegalArgumentException("No account found with email: " + email);
        }
    }

    public List<Prescription> getFilesByPhone(String phone) {
        Optional<PatientAccount> patientAccount = patientAccountRepository.findByPhone(phone);
        if (patientAccount.isPresent()) {
            String username = patientAccount.get().getUsername();
            return getFilesByUsername(username);
        } else {
            throw new IllegalArgumentException("No account found with phone: " + phone);
        }
    }
}
