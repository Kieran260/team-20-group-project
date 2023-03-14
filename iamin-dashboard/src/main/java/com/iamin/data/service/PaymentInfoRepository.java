package com.iamin.data.service;

import com.iamin.data.entity.ClockInOutTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClockInOutTimeRepository
        extends JpaRepository<ClockInOutTime, Long>, JpaSpecificationExecutor<ClockInOutTime> {

    ClockInOutTime findByUsername(String username);
}