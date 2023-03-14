package com.iamin.data.service;

import com.iamin.data.entity.Login;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class LoginService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public List findAll() { 
		  try { 
		    return jdbcTemplate.query("SELECT * FROM login", (rs, rowNum) -> new Login( rs.getString("login_id") , rs.getString("username"), rs.getString("User_id"), rs.getString("role"), rs.getString("hashedPassword"), rs.getString("datamodified"))); 
		  } catch (Exception e) { 
		    return new ArrayList(); 
		  } 
	}
    
    private final LoginRepository repository;
    
    public LoginService(LoginRepository repository) {
        this.repository = repository;
    }

    public Optional<Login> get(Long login_id) {
        return repository.findById(login_id);
    }

    public Login update(Login entity) {
        return repository.save(entity);
    }

    public void delete(Long login_id) {
        repository.deleteById(login_id);
    }

    public Page<Login> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Login> list(Pageable pageable, Specification<Login> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
