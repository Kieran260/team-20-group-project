package com.iamin.views.helpers;

import com.iamin.data.entity.Events;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.EventService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CalendarCardTest {

    @Mock
    private EventService eventService;

    private CalendarCard calendarCard;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        calendarCard = new CalendarCard(eventService);
    }

    @Test
    public void testCreateCard() {
        Login login = new Login();
        SamplePerson person = new SamplePerson();
        login.setPerson(person);

        LocalDate date = LocalDate.now();
        Events event1 = new Events();
        event1.setEventTitle("Event 1");
        event1.setEventType("Meeting");
        event1.setEventTime(LocalTime.of(10, 0));

        Events event2 = new Events();
        event2.setEventTitle("Event 2");
        event2.setEventType("Workshop");
        event2.setEventTime(LocalTime.of(14, 0));

        List<Events> eventsList = Arrays.asList(event1, event2);

        when(eventService.findEventsForDate(person, date)).thenReturn(eventsList);

        Div card = new Div();
        Div createdCard = calendarCard.createCard(card, login);
        

        verify(eventService, times(1)).findEventsForDate(person, date);
    }
}
