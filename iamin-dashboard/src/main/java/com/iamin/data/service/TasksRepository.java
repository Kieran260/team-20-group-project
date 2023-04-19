package com.iamin.data.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {
    List<Tasks> findByPerson(SamplePerson person);
    List<Tasks> findByPersonAndDeadLineBefore(SamplePerson person, LocalDate deadlineThreshold);
    List<Tasks> findByPersonAndCompletedFalse(SamplePerson person);
    List<Tasks> findByDeadLine(LocalDate dueDate);
    List<Tasks> findByPersonAndAckDateIsNotNullAndCompletedFalse(SamplePerson person);
    @Query("SELECT t FROM Tasks t WHERE t.completed = true AND t.submittedDate >= :startDate AND t.submittedDate <= :endDate AND t.submittedDate <= t.deadLine")
    List<Tasks> findCompletedTasksOnOrBeforeDeadlineBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT t FROM Tasks t WHERE t.completed = true AND t.submittedDate >= :startDate AND t.submittedDate <= :endDate AND t.submittedDate > t.deadLine")
    List<Tasks> findCompletedTasksAfterDeadlineBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT t FROM Tasks t WHERE t.completed = false AND t.deadLine < :currentDate AND t.deadLine >= :startOfWeek")
    List<Tasks> findTasksBeyondDeadline(@Param("currentDate") LocalDate currentDate, @Param("startOfWeek") LocalDate startOfWeek);    
}