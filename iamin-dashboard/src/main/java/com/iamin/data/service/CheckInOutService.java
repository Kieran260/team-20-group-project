package com.iamin.data.service;
import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.SamplePerson;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class CheckInOutService{
	
	
    
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

    public List<CheckInOut> findByPersonAndDateBetween(SamplePerson person, LocalDate startDate, LocalDate endDate) {
        return repository.findByPersonAndDateBetween(person, startDate, endDate);
    }

    public List<CheckInOut> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return repository.findByDateBetween(startDate, endDate);
    }

    public int countLateCheckInsForCurrentWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        
        List<CheckInOut> lateCheckIns = repository.findLateCheckInsBetweenDates(startOfWeek, endOfWeek);
        return lateCheckIns.size();
    }
    
    
}