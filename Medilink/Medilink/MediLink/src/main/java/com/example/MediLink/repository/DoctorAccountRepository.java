package com.example.MediLink.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.MediLink.model.DoctorAccount;
@Repository
public interface DoctorAccountRepository extends JpaRepository<DoctorAccount, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByLicense(String license);
    boolean existsByLicenseAndPassword(String license, String password);
    Optional<DoctorAccount> findByLicense(String license);
}

