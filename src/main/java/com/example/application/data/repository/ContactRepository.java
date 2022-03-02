package com.example.application.data.repository;

import com.example.application.data.entity.Contact;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, UUID> {

}
