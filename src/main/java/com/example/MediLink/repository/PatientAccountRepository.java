package com.example.MediLink.repository;

import java.util.Optional; // Adjust the import statement if needed

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MediLink.model.PatientAccount;

public interface PatientAccountRepository extends JpaRepository<PatientAccount, Long> {
       Optional<PatientAccount> findByUsername(String username); // This method should be present
        // Find account by email
        Optional<PatientAccount> findByEmail(String email);

        // Find account by phone number
        Optional<PatientAccount> findByPhone(String phone);
}