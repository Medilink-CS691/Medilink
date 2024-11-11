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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.MediLink.model.PatientAccount;
import com.example.MediLink.model.RecordsEntity;
import com.example.MediLink.repository.DoctorAccountRepository;
import com.example.MediLink.repository.PatientAccountRepository;
import com.example.MediLink.repository.RecordsRepository;
import com.example.MediLink.service.RecordsService;

@Controller
public class RecordsController {

    @Autowired
    private RecordsService fileService;

    @Autowired
    private RecordsRepository fileRepository;

    @Autowired
    private DoctorAccountRepository doctorAccountRepository;

    @Autowired
    private PatientAccountRepository patientAccountRepository;

    // Show upload/download page
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
        List<RecordsEntity> files = fileService.getFilesByUsername(username);
        model.addAttribute("files", files);
        model.addAttribute("username", username);
        return "Patientrecords"; // Ensure this template exists
    }

    // Upload a File
    @PostMapping("/recordspatientupload")
    public String patientuploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("username") String username, 
                             Model model) {
        try {
            fileService.saveFile(file, username);
            model.addAttribute("message", "File uploaded successfully!");
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
        }
        List<RecordsEntity> files = fileService.getFilesByUsername(username);
        model.addAttribute("files", files);
        model.addAttribute("username", username);
        return "Patientrecords"; 
    }

    @PostMapping("/recordsdoctorupload")
    public String doctoruploadFile(@RequestParam("license") String license,
                             @RequestParam("name") String name,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam("patientUsername") String patientUsername, 
                             Model model) {
        try {
            fileService.saveFile(file, patientUsername);
            model.addAttribute("message", "File uploaded successfully!");
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
        }
        List<RecordsEntity> files = fileService.getFilesByUsername(patientUsername);
        model.addAttribute("files", files);
        model.addAttribute("name", name);
        model.addAttribute("license", license);
        model.addAttribute("patientAccount", patientAccountRepository.findByUsername(patientUsername).get());
        return "search_record"; 
    }

    // Download a File
    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long id) {
        return fileService.getFile(id)
            .map(fileEntity -> ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntity.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                .body(new ByteArrayResource(fileEntity.getFileData())))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> viewFile(@PathVariable Long id) {
        RecordsEntity fileEntity = fileService.getFile(id).orElse(null);
    
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

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable Long id, @RequestParam String username, Model model) {
        fileRepository.deleteById(id);
        model.addAttribute("username", username);
        model.addAttribute("files", fileService.getFilesByUsername(username));
        return "Patientrecords"; 
    }

    @PostMapping("doctordelete/{id}")
    public String patientdeleteFile(@RequestParam("license") String license,@RequestParam("name") String name,@PathVariable Long id, @RequestParam("patientUsername") String patientUsername, Model model) {
        fileRepository.deleteById(id);
        List<RecordsEntity> files = fileService.getFilesByUsername(patientUsername);
        model.addAttribute("files", files);
        model.addAttribute("name", name);
        model.addAttribute("license", license);
        model.addAttribute("patientAccount", patientAccountRepository.findByUsername(patientUsername).get());
        return "search_record"; 
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
            return "search_record"; 
        }
            List<RecordsEntity> files = fileService.getFilesByEmail(email);
            model.addAttribute("license", license);
            model.addAttribute("files", files);
            model.addAttribute("email", email);
            model.addAttribute("name", doctorAccountRepository.findByLicense(license).get().getFullName());
            return "search_record"; 
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
            return "search_record";
        }
        List<RecordsEntity> files = fileService.getFilesByPhone(phone);
            model.addAttribute("license", license);
            model.addAttribute("files", files);
            model.addAttribute("phone", phone);
            model.addAttribute("name", doctorAccountRepository.findByLicense(license).get().getFullName());

            return "search_record"; 
        
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
            return "search_record";
        }
        List<RecordsEntity> files = fileService.getFilesByUsername(username);
        model.addAttribute("license", license);
        model.addAttribute("files", files);
        model.addAttribute("username", username);
        model.addAttribute("name", doctorAccountRepository.findByLicense(license).get().getFullName());

        return "search_record"; // Ensure this template exists
    }
}
