package com.example.MediLink.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.MediLink.model.PatientAccount;
import com.example.MediLink.model.Prescription;
import com.example.MediLink.repository.DoctorAccountRepository;
import com.example.MediLink.repository.PatientAccountRepository;
import com.example.MediLink.repository.PrescriptionRepository;
import com.example.MediLink.service.PrescriptionService;

@Controller
@RequestMapping("/prescription")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private DoctorAccountRepository doctorAccountRepository;

    @Autowired
    private PatientAccountRepository patientAccountRepository;

    @GetMapping("/DoctorSearch_record")
    public String DoctorSearch_record(@RequestParam("license") String license, Model model) {
        // Add the license to the model
        model.addAttribute("license", license);
        model.addAttribute("name", doctorAccountRepository.findByLicense(license).get().getFullName());
        // Return the name of the view
        return "doctorprescription";
    }

    @GetMapping("/returnPatient.html")
    public String returnPatient(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "patient"; // Ensure this template exists
    }

    @GetMapping("/returnDoctor.html")
    public String returnDoctor(@RequestParam("license") String license, Model model) {
        model.addAttribute("license", license);
        model.addAttribute("name", doctorAccountRepository.findByLicense(license).get().getFullName());
        return "doctor"; // Ensure this template exists
    }

    @GetMapping("/medical-records")
    public String showUploadPage(@RequestParam("username") String username, Model model) {
        List<Prescription> files = prescriptionService.getFilesByUsername(username);
        model.addAttribute("files", files);
        model.addAttribute("username", username);
        return "Patientprescription"; // Ensure this template exists
    }

    @PostMapping("/Prescriptionupload")
    public String PrescriptionuploadFile(@RequestParam("license") String license,
                             @RequestParam("name") String name,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam("patientUsername") String patientUsername, 
                             Model model) {
        try {
            prescriptionService.saveFile(file, patientUsername,license);
            model.addAttribute("message", "File uploaded successfully!");
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
        }
        List<Prescription> files = prescriptionService.getFilesByUsername(patientUsername);
        model.addAttribute("files", files);
        model.addAttribute("name", name);
        model.addAttribute("license", license);
        model.addAttribute("patientAccount", patientAccountRepository.findByUsername(patientUsername).get());
        return "doctorprescription"; 
    }

    // Download a File
    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long id) {
        return prescriptionService.getFile(id)
            .map(fileEntity -> ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntity.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                .body(new ByteArrayResource(fileEntity.getFileData())))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> viewFile(@PathVariable Long id) {
        Prescription fileEntity = prescriptionService.getFile(id).orElse(null);
    
        if (fileEntity == null) {
            return ResponseEntity.notFound().build();
        }
    
        ByteArrayResource resource = new ByteArrayResource(fileEntity.getFileData());
    
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntity.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileEntity.getFileName() + "\"")
                .contentLength(fileEntity.getFileData().length)
                .body(resource);
    }

    @PostMapping("doctordelete/{id}")
    public String patientdeleteFile(@RequestParam("license") String license,@RequestParam("name") String name,@PathVariable Long id, @RequestParam("patientUsername") String patientUsername, Model model) {
        prescriptionRepository.deleteById(id);
        List<Prescription> files = prescriptionService.getFilesByUsername(patientUsername);
        model.addAttribute("files", files);
        model.addAttribute("name", name);
        model.addAttribute("license", license);
        model.addAttribute("patientAccount", patientAccountRepository.findByUsername(patientUsername).get());
        return "doctorprescription"; 
    }

    // Search files by email
    @GetMapping("/search-by-email")
    public String searchFilesByEmail(@RequestParam("license") String license,@RequestParam("email") String email, Model model) {
        Optional<PatientAccount> patientAccount = patientAccountRepository.findByEmail(email);
        if (patientAccount.isPresent()) {
            model.addAttribute("patientAccount", patientAccount.get());
        }else{
            model.addAttribute("message", "This patient account doesn't exist!");
            model.addAttribute("name", doctorAccountRepository.findByLicense(license).get().getFullName());
            model.addAttribute("license", license);
            return "doctorprescription"; 
        }
            List<Prescription> files = prescriptionService.getFilesByEmail(email);
            model.addAttribute("license", license);
            model.addAttribute("files", files);
            model.addAttribute("email", email);
            model.addAttribute("name", doctorAccountRepository.findByLicense(license).get().getFullName());
            return "doctorprescription"; 
    }

    // Search files by phone
    @GetMapping("/search-by-phone")
    public String searchFilesByPhone(@RequestParam("license") String license,@RequestParam("phone") String phone, Model model) {
        Optional<PatientAccount> patientAccount = patientAccountRepository.findByPhone(phone);
        if (patientAccount.isPresent()) {
            model.addAttribute("patientAccount", patientAccount.get());
        }else{
            model.addAttribute("message", "This patient account doesn't exist!");
            model.addAttribute("name", doctorAccountRepository.findByLicense(license).get().getFullName());
            model.addAttribute("license", license);
            return "doctorprescription";
        }
        List<Prescription> files = prescriptionService.getFilesByPhone(phone);
            model.addAttribute("license", license);
            model.addAttribute("files", files);
            model.addAttribute("phone", phone);
            model.addAttribute("name", doctorAccountRepository.findByLicense(license).get().getFullName());

            return "doctorprescription"; 
        
    }

    // Search files by username
    @GetMapping("/search-by-username")
    public String searchFilesByUsername(@RequestParam("license") String license,@RequestParam("username") String username, Model model) {
        Optional<PatientAccount> patientAccount = patientAccountRepository.findByUsername(username);
        if (patientAccount.isPresent()) {
            model.addAttribute("patientAccount", patientAccount.get());
        }else{
            model.addAttribute("name", doctorAccountRepository.findByLicense(license).get().getFullName());
            model.addAttribute("license", license);
            model.addAttribute("message", "This patient account doesn't exist!");
            return "doctorprescription";
        }
        List<Prescription> files = prescriptionService.getFilesByUsername(username);
        model.addAttribute("license", license);
        model.addAttribute("files", files);
        model.addAttribute("username", username);
        model.addAttribute("name", doctorAccountRepository.findByLicense(license).get().getFullName());

        return "doctorprescription"; // Ensure this template exists
    }
}
