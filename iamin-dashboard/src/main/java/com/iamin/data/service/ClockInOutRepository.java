package com.iamin.data.service;

import com.iamin.data.entity.DocumentTemplates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DocumentTemplatesRepository
        extends JpaRepository<DocumentTemplates, Long>, JpaSpecificationExecutor<DocumentTemplates> {

    DocumentTemplates findByUsername(String username);
}