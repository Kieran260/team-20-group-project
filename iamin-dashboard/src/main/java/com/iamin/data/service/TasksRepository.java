package com.iamin.data.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {
    List<Tasks> findByPerson(SamplePerson person);
    List<Tasks> findByPersonAndDeadLineBefore(SamplePerson person, LocalDate deadlineThreshold);
    List<Tasks> findByPersonAndCompletedFalse(SamplePerson person);
}