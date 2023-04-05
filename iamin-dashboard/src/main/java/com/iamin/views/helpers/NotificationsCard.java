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

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

@Component
public class NotificationsCard {

    @Autowired
    private TasksService tasksService;

    public Div createCard(Div card, Login login) {
        Styling.styleSquareBox(card);

        // Labels
        Label title = new Label("Notifications");
        title.getStyle().set("font-weight", "bold");
        title.getStyle().set("font-size", "18px");
        title.getStyle().set("margin-bottom","10px");

        // Create a list of notifications
        List<Notification> notifications = getNotifications(login.getPerson());

        // Test Tasks
        notifications.add(new Notification("Sample Task 1", LocalDateTime.now()));
        notifications.add(new Notification("Sample Task 2", LocalDateTime.now().plusHours(2)));
    
        // Create a grid to display the list of notifications
        Grid<Notification> grid = new Grid<>();
        grid.setHeight("80%");
        grid.setItems(notifications);

        // Remove the default header row
        grid.removeAllColumns();

        // Set grid width to 100%
        grid.setWidthFull();

        // Create a custom renderer for title and subtitle
        grid.addColumn(createNotificationRenderer()).setWidth("100%").setFlexGrow(0);

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);

        card.add(title, grid);

        return card;
    }

    public List<Notification> getNotifications(SamplePerson person) {
        List<Notification> notifications = new ArrayList<>();
        // Get events of all types except tasks that are happening within 24 hours

        // Get tasks that are due within 72 hours

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

        public LocalDate getLocalDate() {
            return dateTime.toLocalDate();
        }
    }

    private static Renderer<Notification> createNotificationRenderer() {
        return LitRenderer.<Notification> of(
                "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                        + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                        + "    <span> ${item.description} </span>"
                        + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                        + "      ${item.dateTime}" + "    </span>"
                        + "  </vaadin-vertical-layout>"
                        + "</vaadin-horizontal-layout>")
                .withProperty("description", Notification::getDescription)
                .withProperty("dateTime", notification -> notification.getLocalDate().toString()); 
    }
}
