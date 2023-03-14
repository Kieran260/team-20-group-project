package com.iamin.data.service;

import com.iamin.data.entity.ClockInOutTime;

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
public class ClockInOutTimeService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ClockInOutTimeRepository repository;

    public ClockInOutTimeService(ClockInOutTimeRepository repository) {
        this.repository = repository;
    }

    public Optional<ClockInOutTime> get(Long ClockInOutTime_id) {
        return repository.findById(ClockInOutTime_id);
    }

    public ClockInOutTime update(ClockInOutTime entity) {
        return repository.save(entity);
    }

    public void delete(Long ClockInOutTime_id) {
        repository.deleteById(ClockInOutTime_id);
    }

    public Page<ClockInOutTime> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ClockInOutTime> list(Pageable pageable, Specification<ClockInOutTime> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
