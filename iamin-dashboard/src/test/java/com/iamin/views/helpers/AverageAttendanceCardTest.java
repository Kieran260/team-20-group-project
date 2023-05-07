package com.iamin.views.helpers;

import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.Login;
import com.iamin.data.service.CheckInOutService;
import com.vaadin.flow.component.html.Div;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AverageAttendanceCardTest {

    private CheckInOutService checkInOutService;
    private AverageAttendanceCard averageAttendanceCard;

    @BeforeEach
    public void setUp() {
        checkInOutService = Mockito.mock(CheckInOutService.class);
        averageAttendanceCard = new AverageAttendanceCard(checkInOutService);
    }

    @Test
    public void testCreateCard() {
        Login login = new Login();
        Div card = new Div();
    
        Div result = averageAttendanceCard.createCard(card, login);
    
        assertEquals(result, card, "The result should be the same card instance passed as an argument.");
    }

    @Test
    public void testCalculateAverageAttendance() {
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 31);

        CheckInOut checkInOut1 = new CheckInOut();
        checkInOut1.setcheckInTime();
        checkInOut1.setcheckOutTime();
        checkInOut1.setdate();

        CheckInOut checkInOut2 = new CheckInOut();
        checkInOut2.setcheckInTime();
        checkInOut2.setcheckOutTime();
        checkInOut2.setdate();

        when(checkInOutService.findByDateBetween(startDate, endDate)).thenReturn(Arrays.asList(checkInOut1, checkInOut2));

        double averageAttendance = averageAttendanceCard.calculateAverageAttendance(startDate, endDate);

        assertEquals(0.09523809523809523, averageAttendance, 1e-8, "The calculated average attendance should be correct.");
    }
}
