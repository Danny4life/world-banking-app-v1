package com.osiki.World_Banking_Application.repository;

import com.osiki.World_Banking_Application.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByEmail(String email);

    Optional<Admin> findByEmail(String email);

    Optional<Admin> findByPhoneNumber(String phoneNumber);
}
