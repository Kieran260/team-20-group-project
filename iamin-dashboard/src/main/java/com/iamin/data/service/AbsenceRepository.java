package com.iamin.data.service;

import com.iamin.data.entity.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AbsenceRepository
        extends JpaRepository<Absence, Long>, JpaSpecificationExecutor<Absence> {

    Absence findByUsername(String username);
}