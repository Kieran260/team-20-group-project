package com.iamin.data.service;
import com.iamin.data.entity.Login;
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

    public boolean checkIfUsernameExists(String username) {
        Login login = repository.findByUsername(username);
        return login != null;
    }

}