package com.iamin.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iamin.data.entity.Tasks;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {
}