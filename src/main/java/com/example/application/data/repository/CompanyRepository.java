package com.example.application.data.repository;

import com.example.application.data.entity.Company;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

}
