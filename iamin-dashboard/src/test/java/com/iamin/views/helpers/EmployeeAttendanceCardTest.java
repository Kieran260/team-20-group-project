package com.iamin.views.helpers;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.iamin.data.service.AbsenceService;
import com.iamin.data.service.HolidaysService;
import com.iamin.data.service.CheckInOutRepository;
import com.iamin.data.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeAttendanceCardTest {

    @InjectMocks
    private EmployeeAttendanceCard employeeAttendanceCard;

    @Mock
    private LoginService loginService;
    @Mock
    private CheckInOutRepository checkInOutRepository;
    @Mock
    private HolidaysService holidaysService;
    @Mock
    private AbsenceService absenceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeAttendanceCard = new EmployeeAttendanceCard(loginService, checkInOutRepository);
    }

    @Test
    public void testUpdateHolidaysSelectedLabel() {
        // Prepare data
        DatePicker fromDate = new DatePicker(LocalDate.of(2023, 5, 1));
        DatePicker toDate = new DatePicker(LocalDate.of(2023, 5, 5));
        Label holidaysSelectedLabel = new Label();

        // Mock holidaysService
        when(holidaysService.calculateTotalDaysOff(fromDate.getValue(), toDate.getValue())).thenReturn(4);

        // Call updateHolidaysSelectedLabel method
        employeeAttendanceCard.updateHolidaysSelectedLabel(fromDate, toDate, holidaysSelectedLabel, holidaysService);

        // Verify the call to holidaysService
        verify(holidaysService, times(1)).calculateTotalDaysOff(fromDate.getValue(), toDate.getValue());

        // Verify the result
        assertEquals("Holidays Requested: 4", holidaysSelectedLabel.getText());
    }
}
