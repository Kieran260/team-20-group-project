package com.iamin.data.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.iamin.data.entity.Department;
@Component
public class DepartmentService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public Optional<Department> get(Long id) {
        return repository.findById(id);
    }

    public Department update(Department entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Department> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }
    
    public Optional<Department> findByName(String name) {
        return repository.findByDepartmentName(name);
    }

}