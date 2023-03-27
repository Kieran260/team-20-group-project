package com.iamin.data.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.iamin.data.entity.Department;
import com.iamin.data.entity.Login;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> , JpaSpecificationExecutor<Login>{
    
    Optional<Department> findByDepartmentName(String departmentName);
    
    List<Department> findAll();
}