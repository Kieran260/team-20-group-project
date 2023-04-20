package com.iamin.data.service;
import com.iamin.data.entity.Holidays;
import com.iamin.data.entity.SamplePerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class HolidaysService {

    private final HolidaysRepository holidaysRepository;

    @Autowired
    public HolidaysService(HolidaysRepository holidaysRepository) {
        this.holidaysRepository = holidaysRepository;
    }

    public List<Holidays> getAllHolidays() {
        return holidaysRepository.findAll();
    }
    
    public List<Holidays> getHolidaysForPerson(SamplePerson person) {
        return holidaysRepository.findByPerson(person);
    }

    public Holidays createHolidayRequest(Holidays holidayRequest) {
        return holidaysRepository.save(holidayRequest);
    }

    public void deleteHolidayRequest(Holidays holidayRequest) {
        holidaysRepository.delete(holidayRequest);
    }

    public int calculateTotalDaysOff(LocalDate startDate, LocalDate endDate) {
        // Calculate the total number of days between the start and end dates, inclusive of the start and end dates
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate.plusDays(1));
    
        // Calculate the number of weekends (Saturday and Sunday) between the start and end dates
        long weekendsBetween = IntStream.range(0, (int) daysBetween)
                                        .mapToObj(startDate::plusDays)
                                        .filter(date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
                                        .count();
    
        // Calculate the total number of days off by subtracting the weekends from the total number of days
        int totalDaysOff = (int) daysBetween - (int) weekendsBetween;
    
        // If the start and end dates are the same day and it falls on a weekend, return 0 days off
        if (totalDaysOff < 0) {
            totalDaysOff = 0;
        }
    
        return totalDaysOff;
    }
    public Integer getRemainingHolidays(SamplePerson person) {
        Integer totalHolidays = person.getMaxHolidays();
        Integer usedHolidays = calculateTotalDaysOff(person);
        if (usedHolidays == null) {
            usedHolidays = 0;
        }
        return totalHolidays - usedHolidays;
    }
    public List<Holidays> findAllUnapproved() {
        return holidaysRepository.findAllUnapproved();
    }

    public Optional<Holidays> findById(Long requestId) {
        return holidaysRepository.findById(requestId);
    }
    public Integer calculateTotalDaysOff(SamplePerson person) {
        // Call findByPerson to get the list of holidays for the given person
        List<Holidays> holidaysList = holidaysRepository.findByPerson(person);

        // Calculate the total days off by summing the approved holidays
        int totalDaysOff = holidaysList.stream()
                .filter(h -> h.getHolidaysApproval() == null || h.getHolidaysApproval())
                .mapToInt(Holidays::getTotalDays)
                .sum();

        return totalDaysOff;
    }

}
