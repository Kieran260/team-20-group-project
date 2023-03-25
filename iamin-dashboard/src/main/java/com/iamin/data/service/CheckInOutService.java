package com.iamin.data.service;
import com.iamin.data.entity.CheckInOut;
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
public class CheckInOutService{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    
    private final CheckInOutRepository repository;
    
    public CheckInOutService(CheckInOutRepository repository) {
        this.repository = repository;
    }

    public Optional<CheckInOut> get(Long id) {
        return repository.findById(id);
    }

    public CheckInOut update(CheckInOut entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<CheckInOut> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}