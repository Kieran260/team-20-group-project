package com.iamin.data.service;

import com.iamin.data.entity.StaffPayRates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StaffPayRatesRepository
        extends JpaRepository<StaffPayRates, Long>, JpaSpecificationExecutor<StaffPayRates> {

    StaffPayRates findByUsername(String username);
}
