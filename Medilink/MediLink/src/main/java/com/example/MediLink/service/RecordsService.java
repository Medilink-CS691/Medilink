package com.example.MediLink.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.MediLink.model.RecordsEntity;
import com.example.MediLink.repository.RecordsRepository;

@Service
public class RecordsService {

    @Autowired
    private RecordsRepository fileRepository;

    public RecordsEntity saveFile(MultipartFile file, String username) throws IOException {
        RecordsEntity fileEntity = new RecordsEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileType(file.getContentType());
        fileEntity.setFileData(file.getBytes());
        fileEntity.setUsername(username); // Set the username in the entity
        return fileRepository.save(fileEntity);
    }

    public List<RecordsEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    public List<RecordsEntity> getFilesByUsername(String username) {
        return fileRepository.findByUsername(username); // Assuming this method exists in your repository
    }

    public Optional<RecordsEntity> getFile(Long fileId) {
        return fileRepository.findById(fileId);
    }
}
