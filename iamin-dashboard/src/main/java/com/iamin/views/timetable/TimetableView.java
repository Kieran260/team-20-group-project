package com.iamin.views.timetable;

import com.iamin.data.entity.Events;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.EventService;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.LoginRepository;
import com.iamin.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.security.PermitAll;

@CssImport(value = "timetable-grid.css")
@PageTitle("Timetable")
@Route(value = "timetable", layout = MainLayout.class)
@PermitAll
public class TimetableView extends VerticalLayout {

    private final LoginRepository loginRepository;
    private final EventService eventService;


    private FlexLayout timetableGrid;

    public TimetableView(LoginRepository loginRepository, EventService eventService) {
        this.loginRepository = loginRepository;
        this.eventService = eventService;

        FlexLayout headerLayout = new FlexLayout();
        headerLayout.getStyle().set("display", "flex");
        headerLayout.getStyle().set("gap", "5px");
        headerLayout.getStyle().set("align-items","flex-end");
    
        // Header + Button Container Items
        Button backButton = new Button("<");
        backButton.getStyle().set("min-width", "40px");
        backButton.getStyle().set("max-width", "40px");
        backButton.getStyle().set("padding", "0");
    
        // Create a Button with the current week of the year as its text
        Button weekButton = new Button("Week " + Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
        int currentWeekNumber = Integer.parseInt(weekButton.getText().substring(5));
        initTimetableGrid(currentWeekNumber);


        // Set the button's styles
        weekButton.getStyle().set("max-width", "150px");
        weekButton.getStyle().set("min-width", "40px");

        weekButton.addClickListener(event -> {
            // Parse the current week number from the button's text
            int currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        
            // Update the button's text with the new week number
            weekButton.setText("Week " + currentWeek);

            remove(timetableGrid);
            timetableGrid = initTimetableGrid(currentWeek);
            add(timetableGrid);
        });

        backButton.addClickListener(event -> {
            // Parse the current week number from the button's text
            int currentWeek = Integer.parseInt(weekButton.getText().substring(5));
        
            // Subtract 1 from the current week number
            int newWeek = currentWeek - 1;
        
            // Update the button's text with the new week number
            weekButton.setText("Week " + newWeek);

            remove(timetableGrid);
            timetableGrid = initTimetableGrid(newWeek);
            add(timetableGrid);
        });
    
        Button forwardButton = new Button(">");
        forwardButton.getStyle().set("min-width", "40px");
        forwardButton.getStyle().set("max-width", "40px");
        forwardButton.getStyle().set("padding", "0");
    
        forwardButton.addClickListener(event -> {
            // Parse the current week number from the button's text
            int currentWeek = Integer.parseInt(weekButton.getText().substring(5));
        
            // Add 1 to the current week number
            int newWeek = currentWeek + 1;
        
            // Update the button's text with the new week number
            weekButton.setText("Week " + newWeek);

            remove(timetableGrid);
            timetableGrid = initTimetableGrid(newWeek);
            add(timetableGrid);
        });

        headerLayout.add(backButton,weekButton,forwardButton);

        setSizeFull();
        timetableGrid = initTimetableGrid(currentWeekNumber);
        add(headerLayout);
        add(timetableGrid);
    }
    
    private FlexLayout initTimetableGrid(int selectedWeekNumber) {
        FlexLayout newTimetableGrid = new FlexLayout();
        newTimetableGrid.addClassName("timetable-grid");
        newTimetableGrid.setFlexWrap(FlexWrap.WRAP);

        // Calculate the start of the current week (Monday)
        LocalDate today = LocalDate.now();
        LocalDate startOfYear = LocalDate.of(today.getYear(), Month.JANUARY, 1).with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        LocalDate startOfWeek = startOfYear.plusWeeks(selectedWeekNumber - 1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        
        Events[][] contentMatrix = createMatrix(5, 9);
    
        // Fill the content matrix with the selected week's events
        List<Events> events = getEvents(startOfWeek);
        mapEventsToMatrix(events, contentMatrix, startOfWeek);
    
        // Create 6x10 grid cells
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 6; col++) {
                Div cell = new Div();
                cell.addClassName("grid-cell");
    
                Paragraph content;
    
                // Display the day labels in the first row
                if (row == 0 && col > 0) {
                    LocalDate dayOfWeek = startOfWeek.plusDays(col - 1);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd/MM");
                    String formattedDate = dayOfWeek.format(formatter);
                    content = new Paragraph(formattedDate);
                    content.getStyle().set("color", "black");


                    // Format to highlight current time 
                    if (dayOfWeek.equals(LocalDate.now())) {
                        content.getStyle().set("font-weight", "bold");
                        content.getStyle().set("color", "#275CE3");
                    }

                // Display the hour labels in the first column
                } else if (col == 0 && row > 0) {
                    int hour = 8 + row; // Start at 9:00 and increment per hour
                    content = new Paragraph(hour + ":00");
                    content.getStyle().set("color", "black");

                    // Format to highlight current time
                    if (hour == LocalTime.now().getHour()) {
                        content.getStyle().set("font-weight", "bold");
                        content.getStyle().set("color", "#275CE3");
                    }

                } else if (row >= 1 && row <= 9 && col >= 1 && col <= 5) {
                    // Add content to the cells in rows 1 to 9 and columns 1 to 5 from the content matrix
                    Events cellContent = contentMatrix[row - 1][col - 1];
                    if (cellContent.getEventTitle() != null) {
                        content = new Paragraph(cellContent.getEventTitle());
                        content.getStyle().set("color", "black");
                        cell.getStyle().set("transition", "background-color 0.2s");
                        cell.getElement().executeJs("this.classList.add('hoverable')");

                        // Make cell clickable and open a dialog
                        cell.addClickListener(e -> {
                            Dialog dialog = new Dialog();
                            dialog.setWidth("300px");

                            VerticalLayout dialogLayout = new VerticalLayout();
                            dialogLayout.add(new H3(cellContent.getEventTitle()));
                            dialogLayout.add(new Label(cellContent.getEventDescription()));
                            dialogLayout.add(new Label("Time: " + cellContent.getEventTime()));
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM");
                            String formattedDate = cellContent.getEventDate().format(formatter);
                            dialogLayout.add(new Label("Date: " + formattedDate));
                            dialogLayout.add(new Label("Location: " + cellContent.getEventLocation()));

                            HorizontalLayout buttonLayout = new HorizontalLayout();
                            Button closeButton = new Button("Close", ev -> dialog.close());
                            buttonLayout.getStyle().set("margin", "0 auto");
                            buttonLayout.getStyle().set("margin-top", "10px");
                            buttonLayout.add(closeButton);
                            dialogLayout.add(buttonLayout);

                            dialog.add(dialogLayout);
                            dialog.open();
                        });
                    } else {
                        content = new Paragraph("");
                    }
                } else {
                    // Add content to the cell
                    content = new Paragraph("");
                }
    
                content.getStyle().set("margin", "0");
                content.getStyle().set("display", "flex"); // Center the text horizontally
                content.getStyle().set("justify-content", "center");
                content.getStyle().set("align-items", "center"); // Center the text vertically
                content.getStyle().set("height", "100%"); // Make the paragraph fill the cell height
    
                cell.add(content);
    
                newTimetableGrid.add(cell);
            }
        }
        return newTimetableGrid;
    }

    public Events[][] createMatrix(int columns, int rows) {
        Events[][] matrix = new Events[rows][columns];
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = new Events();
            }
        }
        return matrix;
    }

    private void mapEventsToMatrix(List<Events> events, Events[][] matrix, LocalDate startOfWeek) {
        for (Events event : events) {
            LocalDate eventDate = event.getEventDate();
            LocalTime eventTime = event.getEventTime();
    
            int col = (int) ChronoUnit.DAYS.between(startOfWeek, eventDate) + 1;
            int row = eventTime.getHour() - 8; // Assuming 9:00 is row 1, so 9:00 minus 8 equals 1
    
            if (row >= 1 && row <= 9 && col >= 1 && col <= 5) {
                matrix[row - 1][col - 1] = event; 
            }
        }
    }
    
    
    private List<Events> getEvents(LocalDate startOfWeek) {
        // Retrieve the list of Events objects from the database or another source
        // Replace this with your actual implementation
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Login userLogin = loginRepository.findByUsername(currentUsername);
    
        SamplePerson person = userLogin.getPerson();
    
        // Get the dates for the given week (Monday to Friday)
        List<LocalDate> weekDates = IntStream.range(0, 5)
                .mapToObj(startOfWeek::plusDays)
                .collect(Collectors.toList());
    
        // Get events for person and dates
        List<Events> events = eventService.findByAttendees_Id(person.getId());
    
        // Filter the list of Events objects to only include events in the specified week
        List<Events> filteredEvents = events.stream()
                .filter(event -> weekDates.contains(event.getEventDate()))
                .collect(Collectors.toList());
    
        return filteredEvents;
    }
    
    
}
