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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
            notifications.add(new Notification("No notifications to show", LocalDateTime.now().toString()));
        }
    
        // Create a grid to display the list of notifications
        Grid<Notification> grid = new Grid<>();
        grid.setHeight("90%");
 

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
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.ENGLISH);


        
        // Get events of all types except tasks that are happening within 24 hours
        // TODO: Not working

        try {
            List<Events> events = eventService.findEventsWithinHoursForPerson(person, 24);
            for (Events event : events) {      
                Notification notification = new Notification(
                    "Event happening soon: " + event.getEventTitle(),
                    event.getEventDate().format(dateFormatter) + " at " + event.getEventTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                );
                notifications.add(notification);
            }
        } catch (Exception e) {
            System.out.println("Error: No events found or login does not have a person set.");
        }
        
        
        
        
        // Get uncompleted tasks that are due within 48 hours
        List<Tasks> tasks = tasksService.findTasksDueWithinHoursForPerson(person, 48);        
        for (Tasks task : tasks) {
            if (!task.isCompleted()) {
                Notification notification = new Notification(
                    "Uncompleted task due soon: " + task.getTitle(),
                    "Due: " + task.getDeadLine().atStartOfDay().format(dateFormatter)
                );
                notifications.add(notification);
            }
        }
        
        // Get non-acknowledged tasks that have been set for person
        List<Tasks> allTasks = tasksService.findTasksForPerson(person);
        for (Tasks task : allTasks) {
            if (task.getAckDate() == null) {
                LocalDate assignDate = task.getAssignDate();
                LocalDate today = LocalDate.now();
                String daySet;

                if (assignDate.equals(today)) {
                    daySet = "Today";
                } else {
                    long daysAgo = ChronoUnit.DAYS.between(assignDate, today);
                    if (daysAgo > 1) {
                        daySet = daysAgo + " days ago";
                    } else {
                        daySet = daysAgo + " day ago";
                    }
                }

                Notification notification = new Notification(
                    "New task awaiting acknowledgement: " + task.getTitle(),
                    daySet
                );
                notifications.add(notification);
            }
        }


        // Fetch absences with approved or denied status for the current person

        try {
            List<Absence> absences = absenceService.getAbsencesForPerson(person);
            for (Absence absence : absences) {
                Boolean approvalStatus = absence.getAbsenceApproval();
                if (approvalStatus != null && absence.getEndDate().isAfter(LocalDate.now())) {
                    String status = approvalStatus ? "approved" : "denied";
                    Duration duration = Duration.between(absence.getDateModified(), LocalDateTime.now());
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes();
    
                    if (hours >= 1) {
                        Notification notification = new Notification(
                            "Absence request " + absence.getAbsenceReason() + " has been " + status,
                            hours + " hours ago"
                        );
                        notifications.add(notification);
                    } else {
                        Notification notification = new Notification(
                            "Absence request for " + absence.getAbsenceReason() + " has been " + status,
                            minutes + " minutes ago"
                        );
                        notifications.add(notification);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: No absences found or login does not have a person set.");
        }


        // Fetch holidays with approved or denied status for the current person
        try {
            List<Holidays> holidays = holidaysService.getHolidaysForPerson(person);
            for (Holidays holiday : holidays) {
                Boolean approvalStatus = holiday.getHolidaysApproval();
                if (approvalStatus != null && holiday.getEndDate().isAfter(LocalDate.now())) {
                    String status = approvalStatus ? "approved" : "denied";
                    Duration duration = Duration.between(holiday.getDateModified(), LocalDateTime.now());
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes();
    
                    if (hours >= 1) {
                        Notification notification = new Notification(
                            "Holiday request " + holiday.getHolidayReason() + " has been " + status,
                            hours + " hours ago"
                        );
                        notifications.add(notification);
                    } else {
                        Notification notification = new Notification(
                            "Holiday request for " + holiday.getHolidayReason() + " has been " + status,
                            minutes + " minutes ago"
                        );
                        notifications.add(notification);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: No holidays found or login does not have a person set.");
        }
    





        // Fetch holidays not yet approved if user role admin
        // Completed
        try {
            if (login.getRoles().contains(Role.ADMIN)) {
                List<Holidays> holidayRequests = holidaysService.findAllUnapproved();
                System.out.println(holidayRequests.size());
                for (Holidays holiday : holidayRequests) {
                    Duration duration = Duration.between(holiday.getDateModified(), LocalDateTime.now());
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes();
                    long days = duration.toDays();
                    System.out.println("loop entered");
                    if (hours >= 1) {
                        Notification notification = new Notification(
                            "Request from " + holiday.getPerson().getFirstName() + " " + holiday.getPerson().getLastName() + " awaiting approval",
                            hours + " hours ago"
                        );
                        notifications.add(notification);
                    } else if (hours == 1) {
                        Notification notification = new Notification(
                            "Request from " + holiday.getPerson().getFirstName() + " " + holiday.getPerson().getLastName() + " awaiting approval",
                            hours + " hour ago"
                        );
                        notifications.add(notification);
                    } else if (hours >= 24 && days < 1) {
                        Notification notification = new Notification(
                            "Request from " + holiday.getPerson().getFirstName() + " " + holiday.getPerson().getLastName() + " awaiting approval",
                            1 + " day ago"
                        );
                        notifications.add(notification);
                    } else if (days > 1) {
                        Notification notification = new Notification(
                            "Request from " + holiday.getPerson().getFirstName() + " " + holiday.getPerson().getLastName() + " awaiting approval",
                            Math.floor(days) + " day ago"
                        );
                        notifications.add(notification);
                    } else if (minutes < 1) {
                        Notification notification = new Notification(
                            "Request from " + holiday.getPerson().getFirstName() + " " + holiday.getPerson().getLastName() + " awaiting approval",
                            "Just now"
                        );
                        notifications.add(notification);
                    } else if (minutes == 1) {
                        Notification notification = new Notification(
                            "Request from " + holiday.getPerson().getFirstName() + " " + holiday.getPerson().getLastName() + " is awaiting approval",
                            minutes + " minute ago"
                        );
                        notifications.add(notification);
                    } else {
                        Notification notification = new Notification(
                            "Request from " + holiday.getPerson().getFirstName() + " " + holiday.getPerson().getLastName() + " is awaiting approval",
                            minutes + " minutes ago"
                        );
                        notifications.add(notification);
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error: No holiday requests found");
        }
        
        // Fetch absences not yet approved if user role admin
        // Completed
        try {
            if (login.getRoles().contains(Role.ADMIN)) {
                List<Absence> absenceRequests = absenceService.findAllUnapproved();
                System.out.println(absenceRequests.size());
                for (Absence absence : absenceRequests) {
                    Duration duration = Duration.between(absence.getDateModified(), LocalDateTime.now());
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes();
                    System.out.println("loop entered");
                    if (hours >= 1) {
                        Notification notification = new Notification(
                            "Request from " + absence.getPerson().getFirstName() + " " + absence.getPerson().getLastName() + " awaiting approval",
                            hours + " hours ago"
                        );
                        notifications.add(notification);
                    } else if (hours == 1) {
                        Notification notification = new Notification(
                            "Request from " + absence.getPerson().getFirstName() + " " + absence.getPerson().getLastName() + " awaiting approval",
                            hours + " hour ago"
                        );
                        notifications.add(notification);
                    } else if (minutes < 1) {
                        Notification notification = new Notification(
                            "Request from " + absence.getPerson().getFirstName() + " " + absence.getPerson().getLastName() + " awaiting approval",
                            "Just now"
                        );
                        notifications.add(notification);
                    } else if (minutes == 1) {
                        Notification notification = new Notification(
                            "Request from " + absence.getPerson().getFirstName() + " " + absence.getPerson().getLastName() + " is awaiting approval",
                            minutes + " minute ago"
                        );
                        notifications.add(notification);
                    } else {
                        Notification notification = new Notification(
                            "Request from " + absence.getPerson().getFirstName() + " " + absence.getPerson().getLastName() + " is awaiting approval",
                            minutes + " minutes ago"
                        );
                        notifications.add(notification);
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error: No absence requests found");
        }

        // Sort notifications by requests first, then date and time for all events
        return notifications;
    }

    public static class Notification {
        private String description;
        private String status;

        public Notification(String description, String status) {
            this.description = description;
            this.status = status;
        }

        public String getDescription() {
            return description;
        }

        public String getStatus() {
            return status;
        }

    }

    private static Renderer<Notification> createNotificationRenderer() {
        return LitRenderer.<Notification> of(
            "<vaadin-horizontal-layout style=\"align-items: center; margin: 0; padding: 0; box-sizing: border-box;\">"
            + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m); margin: 0; padding: 0; box-sizing: border-box;\">"
            + "    <span style=\"word-wrap: break-word; max-width: 100%; margin: 0; padding: 0; box-sizing: border-box;\">${item.description}</span>"
            + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color); margin: 0; padding: 0; box-sizing: border-box;\">${item.status}</span>"
            + "  </vaadin-vertical-layout>"
            + "</vaadin-horizontal-layout>")
            .withProperty("description", Notification::getDescription)
            .withProperty("status", notification -> "No notifications to show".equals(notification.getDescription()) ?
            "" : notification.getStatus());
    }
    
    
    

    
    
}
