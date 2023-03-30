package com.iamin.data.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class HolidaysTest {

    @Test
    void testGettersAndSetters() {
        Holidays holidays = new Holidays();
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 5);
        Integer totalDays = 5;
        LocalDateTime dateModified = LocalDateTime.now();
        String authorisedBy = "John Doe";
        LocalDate authorisedDate = LocalDate.now();
        String deniedBy = "Jane Doe";
        String deniedReason = "Out of office";
        String reason = "Vacation";

        holidays.setStartDate(startDate);
        holidays.setEndDate(endDate);
        holidays.setTotalDays(totalDays);
        holidays.setDateModified(dateModified);
        holidays.setAuthorisedBy(authorisedBy);
        holidays.setAuthorisedDate(authorisedDate);
        holidays.setDeniedBy(deniedBy);
        holidays.setDeniedReason(deniedReason);
        holidays.setReason(reason);

        assertEquals(startDate, holidays.getStartDate());
        assertEquals(endDate, holidays.getEndDate());
        assertEquals(totalDays, holidays.getTotalDays());
        assertEquals(dateModified, holidays.getDateModified());
        assertEquals(authorisedBy, holidays.getAuthorisedBy());
        assertEquals(authorisedDate, holidays.getAuthorisedDate());
        assertEquals(deniedBy, holidays.getDeniedBy());
        assertEquals(deniedReason, holidays.getDeniedReason());
        assertEquals(reason, holidays.getReason());
    }

}
