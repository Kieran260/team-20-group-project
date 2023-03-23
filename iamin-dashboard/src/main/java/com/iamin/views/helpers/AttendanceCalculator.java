package com.iamin.views.helpers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.time.temporal.ChronoUnit;

import javax.persistence.EntityManager;
import org.springframework.stereotype.Component;

import com.iamin.data.entity.CheckInOut;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AttendanceCalculator {
    
    @Autowired
    private EntityManager entityManager;
    
    public double calculateAverageAttendance(long person_id,LocalDate start,LocalDate finish) {

        // Query the checkInOut table to retrieve all the records for the given person_id for the past 4 weeks
        List<CheckInOut> checkInOutList = entityManager.createQuery(
                "SELECT c FROM CheckInOut c WHERE c.person.id = :person_id AND c.date BETWEEN :start AND :finish",
                CheckInOut.class)
                .setParameter("person_id", person_id)
                .setParameter("start", start)
                .setParameter("finish", finish)
                .getResultList();
    
        int totalDaysAttended = 0;
        for (CheckInOut checkInOut : checkInOutList) {
            if (checkInOut.getcheckOutTime() != null) {
                // Count the number of days the person attended
                totalDaysAttended++;
            }
        }
    
        int totalWorkingDays = 0;
        LocalDate date = start;
        while (date.isBefore(finish) || date.equals(finish)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                totalWorkingDays++;
            }
            date = date.plusDays(1);
        }
    
        // Calculate the average attendance
        double averageAttendance = (double) totalDaysAttended / totalWorkingDays;
    
        return averageAttendance;
    }

    public double calculatePreviousAverageAttendance(long person_id, LocalDate startDate, LocalDate endDate) {
        LocalDate previousStartDate = startDate.minusDays(ChronoUnit.DAYS.between(startDate, endDate));
        LocalDate previousEndDate = endDate.minusDays(ChronoUnit.DAYS.between(startDate, endDate));
    
        return calculateAverageAttendance(person_id, previousStartDate, previousEndDate);
    }
    
    
    
    
}
