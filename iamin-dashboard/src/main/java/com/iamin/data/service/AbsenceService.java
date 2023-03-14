package com.iamin.data.service;

import com.iamin.data.entity.Absence;

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
public class AbsenceService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final AbsenceRepository repository;

    public AbsenceService(AbsenceRepository repository) {
        this.repository = repository;
    }

    public Optional<Absence> get(Long Absence_id) {
        return repository.findById(Absence_id);
    }

    public Absence update(Absence entity) {
        return repository.save(entity);
    }

    public void delete(Long Absence_id) {
        repository.deleteById(Absence_id);
    }

    public Page<Absence> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Absence> list(Pageable pageable, Specification<Absence> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
