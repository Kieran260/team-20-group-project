package com.iamin.views.helpers;

import com.iamin.data.entity.CheckInOut;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AttendanceCalculatorTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<CheckInOut> query;

    private AttendanceCalculator attendanceCalculator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        attendanceCalculator = new AttendanceCalculator();
        attendanceCalculator.entityManager = entityManager;
    }

    @Test
    public void testCalculateAverageAttendance() {
        // Arrange
        long person_id = 1L;
        LocalDate start = LocalDate.of(2022, 1, 1);
        LocalDate finish = LocalDate.of(2022, 1, 31);

        CheckInOut checkInOut1 = new CheckInOut();
        checkInOut1.setId(1L);
        checkInOut1.setDate(LocalDate.of(2022, 1, 3));
        checkInOut1.setCheckInTime(LocalDateTime.of(2022, 1, 3, 9, 0, 0));
        checkInOut1.setCheckOutTime(LocalDateTime.of(2022, 1, 3, 17, 0, 0));
        checkInOut1.setPersonId(person_id);

        CheckInOut checkInOut2 = new CheckInOut();
        checkInOut2.setId(2L);
        checkInOut2.setDate(LocalDate.of(2022, 1, 4));
        checkInOut2.setCheckInTime(LocalDateTime.of(2022, 1, 4, 9, 0, 0));
        checkInOut2.setCheckOutTime(LocalDateTime.of(2022, 1, 4, 17, 0, 0));
        checkInOut2.setPersonId(person_id);

        CheckInOut checkInOut3 = new CheckInOut();
        checkInOut3.setId(3L);
        checkInOut3.setDate(LocalDate.of(2022, 1, 5));
        checkInOut3.setCheckInTime(LocalDateTime.of(2022, 1, 5, 9, 0, 0));
        checkInOut3.setCheckOutTime(LocalDateTime.of(2022, 1, 5, 17, 0, 0));
        checkInOut3.setPersonId(person_id);

        CheckInOut checkInOut4 = new CheckInOut();
        checkInOut4.setId(4L);
        checkInOut4.setDate(LocalDate.of(2022, 1, 6));
        checkInOut4.setCheckInTime(LocalDateTime.of(2022, 1, 6, 9, 0, 0));
        checkInOut4.setCheckOutTime(LocalDateTime.of(2022, 1, 6, 17, 0, 0));
        checkInOut4.setPersonId(person_id);

        List<CheckInOut> checkInOutList = Arrays.asList(checkInOut1, checkInOut2, checkInOut3, checkInOut4);

        when(entityManager.createQuery(any(String.class), any(Class.class))).thenReturn(query);
        when(query.setParameter(any(String.class), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(checkInOutList);

        // Act
        double averageAttendance = attendanceCalculator.calculateAverageAttendance(person_id, start, finish);

        // Assert
        assertEquals(4, checkInOutList.size());
        assertEquals(4 * 8.0 / 24.0, averageAttendance, 0.01);
}
