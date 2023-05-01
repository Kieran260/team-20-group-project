package com.iamin.views.ManageEvents;

import com.iamin.data.entity.Events;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;
import com.iamin.data.service.EventService;
import com.iamin.data.service.LoginRepository;
import com.iamin.data.service.LoginService;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;
import com.iamin.data.service.SamplePersonService;
import com.iamin.data.service.TasksService;
import com.iamin.data.validation.Validation;
import com.iamin.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Uses(Icon.class)
@PageTitle("Manage Events")
@Route(value = "manage-events", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ManageEventsView extends Div implements BeforeEnterObserver{

    //Checks if current user has a SamplePerson entity and if not shows a sign up dialog
    @Autowired
    LoginRepository loginRepository;
    
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Login userLogin;
    String currentUsername = authentication.getName();
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        try{
            userLogin = loginRepository.findByUsername(currentUsername);
        }catch(Exception e){
            userLogin = null;
        }
        // Check your condition and redirect if necessary
        boolean Redirect = (userLogin != null && userLogin.getPerson() == null);
        if (Redirect) {
            UI.getCurrent().getPage().executeJs("location.href = 'dashboard'");
        }
    }
    //end check
    
    Grid<Events> grid = new Grid<>(Events.class, false);

    SplitLayout splitLayout = new SplitLayout();

    private final EventService eventService;

    @Autowired
    private SamplePersonService samplePersonService;

    public ManageEventsView(EventService eventService, SamplePersonService samplePersonService ) {
        this.samplePersonService = samplePersonService;
        this.eventService = eventService;
        addClassName("list-view");

        splitLayout.getStyle().set("width", "100%");

        configureEvents();
        configureAssignBar();
        add(splitLayout);
    }

    public void configureEvents() {
        VerticalLayout events = new VerticalLayout();
        events.setWidth("80%");
    
        // give grid a class name - as a reference point
        grid.addClassName("manager-assigns-events");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setWidth("80%");
    
        grid.addColumn("eventTitle").setAutoWidth(true);
        grid.addColumn("eventDescription").setAutoWidth(true);
    
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
        grid.addColumn(event -> event.getEventDate().format(dateFormatter))
            .setHeader("Event Date")
            .setAutoWidth(true);
    
        grid.addColumn(event -> event.getEventTime().format(timeFormatter))
            .setHeader("Event Time")
            .setAutoWidth(true);
    
        grid.addColumn(event -> event.getEventType())
            .setHeader("Event Type")
            .setAutoWidth(true);
    
        grid.addColumn("eventLocation").setAutoWidth(true);
    
        grid.addColumn(event -> {
            List<SamplePerson> attendees = event.getAttendees();
            return attendees.stream()
                    .map(person -> person.getFirstName() + " " + person.getLastName())
                    .collect(Collectors.joining(", "));
        }).setHeader("Attendees").setAutoWidth(true);
    
        grid.setItems(query -> eventService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream()
                .sorted(Comparator.comparing(Events::getEventDate)
                    .thenComparing(Events::getEventTime).reversed())
        );
        events.add(grid);
    
        splitLayout.addToPrimary(grid);
    }
    
    
 
    public void configureAssignBar() {
        VerticalLayout content = new VerticalLayout();
        content.setWidth("20%");
    
        List<SamplePerson> employees = samplePersonService.getAllSamplePersons();
    
        // start
        MultiSelectComboBox<SamplePerson> employeeList = new MultiSelectComboBox<>("Select Attendees");
        employeeList.setDataProvider(new ListDataProvider<>(employees));
        employeeList.setWidthFull();
        employeeList.setRenderer(new ComponentRenderer<>(person -> {
            HorizontalLayout row = new HorizontalLayout();
            row.setAlignItems(FlexComponent.Alignment.CENTER);
    
            Span name = new Span(person.getFirstName() + " " + person.getLastName());
            Span profession = new Span(person.getJobTitle());
            profession.getStyle().set("color", "var(--lumo-secondary-text-color)").set("font-size", "var(--lumo-font-size-s)");
    
            VerticalLayout column = new VerticalLayout(name, profession);
            column.setPadding(false);
            column.setSpacing(false);
    
            row.add(column);
            row.getStyle().set("line-height", "var(--lumo-line-height-m)");
            return row;
        }));
        // end
    
        TextField eventTitle = new TextField();
        eventTitle.setLabel("Event Title");
    
        TextField eventDescription = new TextField();
        eventDescription.setLabel("Event Description");
    
        DatePicker eventDate = new DatePicker("Event Date");
    
        TimePicker eventTime = new TimePicker("Event Time");
        eventTime.setStep(Duration.ofMinutes(60));
        eventTime.addValueChangeListener(event -> {
            LocalTime selectedTime = event.getValue();
            LocalTime startTime = LocalTime.of(9, 0);
            LocalTime endTime = LocalTime.of(17, 0);
    
            if (selectedTime != null && (selectedTime.isBefore(startTime) || selectedTime.isAfter(endTime))) {
                eventTime.setErrorMessage("Please select a time between 9:00 and 17:00.");
                eventTime.setInvalid(true);
            } else {
                eventTime.setInvalid(false);
            }
        });
    
        TextField eventType = new TextField();
        eventType.setLabel("Event Type");
    
        TextField eventLocation = new TextField();
        eventLocation.setLabel("Event Location");
    
        employeeList.setWidthFull();
        eventTitle.setWidthFull();
        eventDescription.setWidthFull();
        eventDate.setWidthFull();
        eventTime.setWidthFull();
        eventType.setWidthFull();
        eventLocation.setWidthFull();
    
        Button saveButton = new Button("Create Event", event -> {
            Set<SamplePerson> selectedAttendees = employeeList.getValue();
            String title = eventTitle.getValue();
            String description = eventDescription.getValue();
            LocalDate date = eventDate.getValue();
            LocalTime time = eventTime.getValue();
        
            String type = eventType.getValue();
            String location = eventLocation.getValue();
            
            if (selectedAttendees != null && !selectedAttendees.isEmpty()
                    && title != null && !title.trim().isEmpty()
                    && description != null && !description.trim().isEmpty()
                    && date != null && Validation.isAfterCurrentDate(date) && time != null
                    && type != null && !type.trim().isEmpty()
                    && location != null && !location.trim().isEmpty()
                    && !Validation.isSqlInjection(title)
                    && !Validation.isSqlInjection(description)
                    ) {
        
                Events newEvent = new Events();
                newEvent.setEventTitle(title);
                newEvent.setEventDescription(description);
                newEvent.setEventDate(date);
                newEvent.setEventTime(time);
                newEvent.setEventType(type);
                newEvent.setEventLocation(location);
        
                for (SamplePerson attendee : selectedAttendees) {
                    newEvent.addAttendee(attendee);
                }
                eventService.createEvent(newEvent);
        
                Notification.show("Event created successfully.", 3000, Notification.Position.TOP_CENTER);
                eventTitle.clear();
                eventDescription.clear();
                eventDate.clear();
                eventTime.clear();
                eventType.clear();
                eventLocation.clear();
                employeeList.clear();
                grid.getDataProvider().refreshAll();
            } else {
                Notification.show("Please fill all required fields.", 3000, Notification.Position.TOP_CENTER);
            }
        });
        saveButton.setWidthFull();
        

        content.add(employeeList, eventTitle, eventDescription, eventDate, eventTime, eventType, eventLocation, saveButton);
    
        splitLayout.addToSecondary(content);
    }
    
}

