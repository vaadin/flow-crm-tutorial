package com.example.application.data.repository;

import com.example.application.data.entity.Status;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, UUID> {

}
