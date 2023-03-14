package com.iamin.data.service;

import com.iamin.data.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HolidayRepository
        extends JpaRepository<Holiday, Long>, JpaSpecificationExecutor<Holiday> {

    Holiday findByUsername(String username);
}