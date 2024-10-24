package com.example.MediLink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MediLink.model.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByUsername(String username);
}