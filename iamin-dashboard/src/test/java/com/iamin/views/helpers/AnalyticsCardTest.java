package com.iamin.views.helpers;

import com.iamin.data.service.CheckInOutService;
import com.iamin.data.service.LoginService;
import com.iamin.data.service.TasksService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

class AnalyticsCardTest {
    private AnalyticsCard analyticsCard;
    private CheckInOutService checkInOutService;
    private TasksService tasksService;
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        checkInOutService = Mockito.mock(CheckInOutService.class);
        tasksService = Mockito.mock(TasksService.class);
        loginService = Mockito.mock(LoginService.class);
        analyticsCard = new AnalyticsCard(checkInOutService, tasksService, loginService);
    }

    @Test
    void testCreateCard() {
        Div card = new Div();
        Div createdCard = analyticsCard.createCard(card);

        assertNotNull(createdCard);
        assertEquals("flex", createdCard.getStyle().get("display"));
        assertEquals("column", createdCard.getStyle().get("flex-direction"));
        assertEquals("space-between", createdCard.getStyle().get("justify-content"));
    }

    @Test
    void testTotalEmployees() {
        Mockito.when(loginService.count()).thenReturn(5);
        Div div = new Div();
        Div totalEmployeesDiv = analyticsCard.createTotalEmployees(div);
    
        assertNotNull(totalEmployeesDiv);
        assertTrue(checkComponentText(totalEmployeesDiv, "5"));
    }
    
    @Test
    void testTasksCompletionRate() {
        Mockito.when(tasksService.calculateOnTimeCompletionPercentageForCurrentWeek()).thenReturn(70.0);
        Div div = new Div();
        Div tasksCompletionRateDiv = analyticsCard.createTasksCompletionRate(div);

        assertNotNull(tasksCompletionRateDiv);
        assertTrue(checkComponentText(tasksCompletionRateDiv, "70.0%"));
    }

    @Test
    void testLateArrivals() {
        Mockito.when(checkInOutService.countLateCheckInsForCurrentWeek()).thenReturn(2);
        Div div = new Div();
        Div lateArrivalsDiv = analyticsCard.createLateArrivals(div);

        assertNotNull(lateArrivalsDiv);
        assertTrue(checkComponentText(lateArrivalsDiv, "2"));
    }

    @Test
    void testTasksBeyondDeadline() {
        Mockito.when(tasksService.countTasksBeyondDeadlineForCurrentWeek()).thenReturn(3);
        Div div = new Div();
        Div tasksBeyondDeadlineDiv = analyticsCard.createTasksBeyondDeadline(div);

        assertNotNull(tasksBeyondDeadlineDiv);
        assertTrue(checkComponentText(tasksBeyondDeadlineDiv, "3"));
    }

    private boolean checkComponentText(Component component, String expectedText) {
        Stream<Component> children = component.getChildren();
        if (children.anyMatch(child -> child.getElement().getText().contains(expectedText))) {
            return true;
        }

        return children.anyMatch(child -> checkComponentText(child, expectedText));
    }
}

