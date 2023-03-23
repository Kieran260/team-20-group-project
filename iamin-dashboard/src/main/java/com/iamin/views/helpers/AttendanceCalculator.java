package com.iamin.views.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Component;

import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.SamplePerson;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.iamin.data.entity.CheckInOut;

@Component
public class AttendanceCalculator {
    
    @Autowired
    private EntityManager entityManager;
    
    public double calculateAverageAttendance(long person_id) {
        LocalDate today = LocalDate.now();
        LocalDate fourWeeksAgo = today.minusWeeks(4);
    
        // Query the checkInOut table to retrieve all the records for the given person_id for the past 4 weeks
        List<CheckInOut> checkInOutList = entityManager.createQuery(
                "SELECT c FROM CheckInOut c WHERE c.person.id = :person_id AND c.date BETWEEN :fourWeeksAgo AND :today",
                CheckInOut.class)
                .setParameter("person_id", person_id)
                .setParameter("fourWeeksAgo", fourWeeksAgo)
                .setParameter("today", today)
                .getResultList();
    
        int totalDaysAttended = 0;
        for (CheckInOut checkInOut : checkInOutList) {
            if (checkInOut.getcheckOutTime() != null) {
                // Count the number of days the person attended
                totalDaysAttended++;
            }
        }
    
        int totalWorkingDays = 0;
        LocalDate date = fourWeeksAgo;
        while (date.isBefore(today) || date.equals(today)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                totalWorkingDays++;
            }
            date = date.plusDays(1);
        }
    
        // Calculate the average attendance
        double averageAttendance = (double) totalDaysAttended / totalWorkingDays;
    
        return averageAttendance;
    }
    
    
    
    
}
