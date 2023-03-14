package com.iamin.data.service;

import com.iamin.data.entity.PaymentInfo;

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
public class PaymentInfoService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final PaymentInfoRepository repository;

    public PaymentInfoService(PaymentInfoRepository repository) {
        this.repository = repository;
    }

    public Optional<PaymentInfo> get(Long PaymentInfo_id) {
        return repository.findById(PaymentInfo_id);
    }

    public PaymentInfo update(PaymentInfo entity) {
        return repository.save(entity);
    }

    public void delete(Long PaymentInfo_id) {
        repository.deleteById(PaymentInfo_id);
    }

    public Page<PaymentInfo> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<PaymentInfo> list(Pageable pageable, Specification<PaymentInfo> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
