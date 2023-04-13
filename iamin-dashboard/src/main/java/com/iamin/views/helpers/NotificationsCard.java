package com.iamin.views.helpers;

import com.iamin.data.Role;
import com.iamin.data.entity.Absence;
import com.iamin.data.entity.Events;
import com.iamin.data.entity.Holidays;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;
import com.iamin.data.service.AbsenceService;
import com.iamin.data.service.EventService;
import com.iamin.data.service.HolidaysRepository;
import com.iamin.data.service.HolidaysService;
import com.iamin.data.service.TasksService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

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
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NotificationsCard {

    private TasksService tasksService;

    private AbsenceService absenceService;

    private HolidaysService holidaysService;
    private final HolidaysRepository holidaysRepository;


    private EventService eventService;

    Login login;

    @Autowired
    public NotificationsCard (TasksService tasksService,AbsenceService absenceService,HolidaysService holidaysService,EventService eventService, HolidaysRepository holidaysRepository) {
        this.tasksService = tasksService;
        this.absenceService = absenceService;
        this.holidaysService = holidaysService;
        this.eventService = eventService;
        this.holidaysRepository = holidaysRepository;
    }

    public Div createCard(Div card, Login login) {
        this.login = login;

        Styling.styleSquareBox(card);

        // Labels
        Label title = new Label("Notifications");
        title.getStyle().set("font-weight", "bold");
        title.getStyle().set("font-size", "18px");
        title.getStyle().set("margin-bottom","10px");

        // Create a list of notifications
        List<Notification> notifications = Collections.emptyList();

        try {
            notifications = getNotifications(login.getPerson());
        } catch (Exception e) {
            System.out.println("Error: No notifications found");
        }
        
        if (notifications.size() == 0) {
            notifications.add(new Notification("No notifications to show", LocalDateTime.now()));
        }
    
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
        // TODO: Fix events and add dismiss button.


        List<Notification> notifications = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");


        
        // Get events of all types except tasks that are happening within 24 hours
        // TODO: Not working
        /* 
        List<Events> events = eventService.findEventsWithinHoursForPerson(person, 24);
        for (Events event : events) {      
            LocalDateTime eventDateTime = event.getEventDate().atTime(event.getEventTime());
            Notification notification = new Notification(
                "You have an event: " + event.getEventTitle(),
                eventDateTime
            );
            notifications.add(notification);
        }
        */
        
        
        // Get tasks that are due within 72 hours
        List<Tasks> tasks = tasksService.findTasksDueWithinHoursForPerson(person, 72);        
        for (Tasks task : tasks) {
            if (!task.isCompleted()) {
                Notification notification = new Notification(
                    "Task due soon: " + task.getTitle(),
                    task.getDeadLine().atStartOfDay()
                );
                notifications.add(notification);
            }
        }

        // Fetch absences with approved or denied status for the current person
        List<Absence> absences = absenceService.getAbsencesForPerson(person);
        for (Absence absence : absences) {
            Boolean approvalStatus = absence.getAbsenceApproval();
            if (approvalStatus != null && absence.getEndDate().isAfter(LocalDate.now())) {
                String status = approvalStatus ? "approved" : "denied";
                Notification notification = new Notification(
                    "Absence request for " + absence.getStartDate().format(dateFormatter) + " to " + absence.getEndDate().format(dateFormatter) + " has been " + status,
                    absence.getDateModified().toLocalDate().atStartOfDay()
                );
                notifications.add(notification);
            }
        }

        // Fetch holidays with approved or denied status for the current person
        List<Holidays> holidays = holidaysService.getHolidaysForPerson(person);
        for (Holidays holiday : holidays) {
            Boolean approvalStatus = holiday.getHolidaysApproval();
            if (approvalStatus != null && holiday.getEndDate().isAfter(LocalDate.now())) {
                String status = approvalStatus ? "approved" : "denied";
                Notification notification = new Notification(
                    "Holiday request for " + holiday.getStartDate().format(dateFormatter) + " to " + holiday.getEndDate().format(dateFormatter) + " has been " + status,
                    holiday.getDateModified().toLocalDate().atStartOfDay()
                );
                notifications.add(notification);
            }
        }


        // Fetch holidays not yet approved if user role admin
        // TODO: Fix this
        try {
            if (login.getRoles().contains(Role.ADMIN)) {
                List<Holidays> holidayRequests = holidaysService.findAllUnapproved();
                System.out.println(holidayRequests.size());
                for (Holidays holiday : holidayRequests) {
                    Boolean approvalStatus = holiday.getHolidaysApproval();
                    if (approvalStatus == false) {
                        Notification notification = new Notification(
                            "New holiday request from " + holiday.getPerson().getFirstName() + " " + holiday.getPerson().getLastName(),
                            holiday.getDateModified().toLocalDate().atStartOfDay()
                        );
                        notifications.add(notification);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: No holiday requests found");
        }
        


    

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
                .withProperty("dateTime", notification -> "No notifications to show".equals(notification.getDescription()) ?
                        "" : notification.getLocalDate().format(DateTimeFormatter.ofPattern("EEEE dd/MM")));
    }
}
