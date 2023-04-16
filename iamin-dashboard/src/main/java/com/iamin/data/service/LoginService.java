package com.iamin.data.service;
import com.iamin.data.Role;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    
    private final LoginRepository repository;
    
    public LoginService(LoginRepository repository) {
        this.repository = repository;
    }

    public Optional<Login> get(Long id) {
        return repository.findById(id);
    }

    public Login update(Login entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
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
    public SamplePerson getSamplePersonByUsername(String username) {
        Login login =  repository.findByUsername(username);
        return login.getPerson();
    }
    public Optional<String> getPersonNameByUsername(String username) {
        Login login = repository.findByUsername(username);
        if (login == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        SamplePerson person = login.getPerson();
        if (person == null) {
            throw new EntityNotFoundException("Person not found for username");
        }
        return Optional.of(person.getFirstName() + " " + person.getLastName());
    }


    public boolean checkIfUsernameExists(String username) {
        Login login = repository.findByUsername(username);
        return login != null;
    }

    public List<Login> findAllByRole(Role role) {
        return repository.findAll().stream()
                .filter(login -> login.getRoles().contains(role))
                .collect(Collectors.toList());
    }

    public List<Login> findAll() {
        return repository.findAll();
    }
    
    public List<Login> findAllPendingLogins() {
        return repository.findAll().stream()
                .filter(login -> login.getPasswordSet() != true)
                .collect(Collectors.toList());
    }
}