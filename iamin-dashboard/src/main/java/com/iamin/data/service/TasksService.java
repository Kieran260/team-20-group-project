package com.iamin.data.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
