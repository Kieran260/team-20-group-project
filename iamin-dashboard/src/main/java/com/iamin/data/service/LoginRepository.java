package com.iamin.data.service;

import com.iamin.data.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoginRepository extends JpaRepository<Login, Long>, JpaSpecificationExecutor<Login> {

    Login findByUsername(String username);
    
}