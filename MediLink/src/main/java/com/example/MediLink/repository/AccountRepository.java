package com.example.MediLink.repository;

import java.util.Optional; // Adjust the import statement if needed

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MediLink.model.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String username); // This method should be present
}