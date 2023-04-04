package com.iamin.views.helpers;

import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;
import com.iamin.data.service.TasksService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

@Component
public class NotificationsCard {

    @Autowired
    private TasksService tasksService;

    public Div createCard(Div card, Login login) {
        Styling.styleSquareBox(card);

        // Create a list of notifications
        List<Notification> notifications = getNotifications(login.getPerson());

        // Create a grid to display the list of notifications
        Grid<Notification> grid = new Grid<>();
        grid.setItems(notifications);
        grid.addColumn(Notification::getDescription).setHeader("Description");
        grid.addColumn(Notification::getDateTime).setHeader("Date/Time");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS,
                GridVariant.LUMO_ROW_STRIPES);
        grid.setHeightByRows(true);

        card.add(grid);

        return card;
    }

    public List<Notification> getNotifications(SamplePerson person) {
        List<Notification> notifications = new ArrayList<>();

        // Get events of all types except tasks that are happening within 24 hours

        // Get tasks that are due within 72 hours
        tasksService.addTestTasks(person);

        List<Tasks> tasks = tasksService.findTasksDueWithinHoursForPerson(person, 72);        
        for (Tasks task : tasks) {
            Notification notification = new Notification(
                "Task: " + task.getDescription(),
                task.getDeadLine().atStartOfDay()
            );
            notifications.add(notification);
        }

        // Fetch requests with approved or denied status for the current user

        // Sort notifications by requests first, then date and time for all events


        return notifications;
    }

    public static class Notification {
        private String description;
        private LocalDateTime dateTime;

        public Notification(String description, LocalDateTime dateTime) {
            this.description = description;
            this.dateTime = dateTime;
        }

        public String getDescription() {
            return description;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }
    }
}
