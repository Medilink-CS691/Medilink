package com.example.MediLink.controller;

import java.io.IOException;
import java.util.List;

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

import com.example.MediLink.model.RecordsEntity;
import com.example.MediLink.repository.RecordsRepository;
import com.example.MediLink.service.RecordsService;

@Controller
public class RecordsController {

    @Autowired
    private RecordsService fileService;

    @Autowired
    private RecordsRepository fileRepository;

    // Show upload/download page
    @GetMapping("/returnPatient.html")
    public String returnPatient(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "patient"; // Ensure this template exists
    }

    @GetMapping("/returnDoctor.html")
    public String returnDoctor(@RequestParam("license") String license, Model model) {
        model.addAttribute("license", license);
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
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("username") String username, // Added username parameter
                             Model model) {
        try {
            fileService.saveFile(file, username); // Pass username to the service
            model.addAttribute("message", "File uploaded successfully!");
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
        }
        List<RecordsEntity> files = fileService.getFilesByUsername(username); // Get files for the specific username
        model.addAttribute("files", files);
        model.addAttribute("username", username); // Add username to the model
        return "Patientrecords"; // Ensure records.html exists
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
        // Retrieve the file from the database (assuming a service method)
        RecordsEntity fileEntity = fileService.getFile(id).get();
    
        if (fileEntity == null) {
            return ResponseEntity.notFound().build();
        }
    
        // Create a ByteArrayResource from the file data
        ByteArrayResource resource = new ByteArrayResource(fileEntity.getFileData());
    
        // Set content type and headers
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntity.getFileType())) // Use fileType from FileEntity
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileEntity.getFileName() + "\"") // Use fileName from FileEntity
                .contentLength(fileEntity.getFileData().length)
                .body(resource);
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable Long id, @RequestParam String username, Model model) {
    // Implement the logic to remove the file from the database, potentially checking the username
    fileRepository.deleteById(id);
    
    // Add necessary attributes and return the records view
    model.addAttribute("username", username);
    model.addAttribute("files", fileService.getFilesByUsername(username));
    return "Patientrecords";  // Return to the records page
}

}
