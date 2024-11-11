package com.example.MediLink.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MediLink.model.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByUsername(String username);
}