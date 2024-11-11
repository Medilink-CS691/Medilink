package com.example.MediLink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MediLink.model.RecordsEntity;

public interface RecordsRepository extends JpaRepository<RecordsEntity, Long> {
    List<RecordsEntity> findByUsername(String username);
}