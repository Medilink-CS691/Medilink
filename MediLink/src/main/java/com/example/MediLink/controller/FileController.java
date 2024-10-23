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

import com.example.MediLink.model.FileEntity;
import com.example.MediLink.service.FileService;

@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    // Show upload/download page
    @GetMapping("/medical-records")
    public String showUploadPage(@RequestParam("username") String username, Model model) {
        List<FileEntity> files = fileService.getFilesByUsername(username);
        model.addAttribute("files", files);
        model.addAttribute("username", username);
        return "records"; // Ensure this template exists
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
        List<FileEntity> files = fileService.getFilesByUsername(username); // Get files for the specific username
        model.addAttribute("files", files);
        model.addAttribute("username", username); // Add username to the model
        return "records"; // Ensure records.html exists
    }

    // Download a File
    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long fileId) {
        return fileService.getFile(fileId)
            .map(fileEntity -> ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntity.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                .body(new ByteArrayResource(fileEntity.getFileData())))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<Resource> viewFile(@PathVariable Long id) {
        // Retrieve the file from the database (assuming a service method)
        FileEntity fileEntity = fileService.getFile(id).get();
    
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
}
