package com.iamin.views.helpers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.AbsenceService;
import com.iamin.data.service.DocumentService;
import com.iamin.data.service.EventService;
import com.iamin.data.service.HolidaysRepository;
import com.iamin.data.service.HolidaysService;
import com.iamin.data.service.TasksService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.component.UI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationsCardTest {

    @Mock
    private TasksService tasksService;

    @Mock
    private AbsenceService absenceService;

    @Mock
    private HolidaysService holidaysService;

    @Mock
    private EventService eventService;

    @Mock
    private HolidaysRepository holidaysRepository;

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private NotificationsCard notificationsCard;

    private Login login;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        login = new Login();
        login.setPerson(new SamplePerson());
    }


    

}