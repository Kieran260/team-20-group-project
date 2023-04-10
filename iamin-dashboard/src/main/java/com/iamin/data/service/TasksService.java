package com.iamin.data.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;


@Service
public class TasksService {

    private final TasksRepository tasksRepository;

    public TasksService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public List<Tasks> findAll() {
        return tasksRepository.findAll();
    }

    public Page<Tasks> list(Pageable pageable) {
        return tasksRepository.findAll(pageable);
    }

    public Optional<Tasks> get(Long id) {
        return tasksRepository.findById(id);
    }

    @Transactional
    public Tasks update(Tasks tasks) {
        return tasksRepository.save(tasks);
    }

    @Transactional
    public Tasks create(Tasks tasks) {
        return tasksRepository.save(tasks);
    }

    @Transactional
    public void delete(Long id) {
        tasksRepository.deleteById(id);
    }

    public List<Tasks> findTasksForPerson(SamplePerson person) {
        return tasksRepository.findByPerson(person);
    }

    public List<Tasks> findTasksDueWithinHoursForPerson(SamplePerson person, int hours) {
        LocalDateTime deadlineThreshold = LocalDateTime.now().plusHours(hours);
        LocalDate deadlineThresholdDate = deadlineThreshold.toLocalDate();
        return tasksRepository.findByPersonAndDeadLineBefore(person, deadlineThresholdDate);
    }

    public List<Tasks> findCompletedFalseForPerson(SamplePerson person) {
        return tasksRepository.findByPersonAndCompletedFalse(person);
    }

}
