package com.iamin.views.timetable;

import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.LoginRepository;
import com.iamin.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
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


    private FlexLayout timetableGrid;

    public TimetableView(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;

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
        LocalDate startOfYear = LocalDate.of(today.getYear(), Month.JANUARY, 1);
        LocalDate startOfWeek = startOfYear.plusWeeks(selectedWeekNumber - 1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        
        String[][] contentMatrix = createMatrix(5, 9);
    
        // Fill the content matrix with events
        List<Event> events = getEvents();
        mapEventsToMatrix(events, contentMatrix);
        for (String[] row : contentMatrix) {
            System.out.println(Arrays.toString(row));
        }
    
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
                // Display the hour labels in the first column
                } else if (col == 0 && row > 0) {
                    int hour = 8 + row; // Start at 9:00 and increment per hour
                    content = new Paragraph(hour + ":00");
                } else if (row >= 1 && row <= 9 && col >= 1 && col <= 5) {
                    // Add content to the cells in rows 1 to 9 and columns 1 to 5 from the content matrix
                    String cellContent = contentMatrix[row - 1][col - 1];
                    if (!cellContent.isEmpty()) {
                        content = new Paragraph(cellContent);
                    } else {
                        content = new Paragraph("");
                    }
                } else {
                    // Add content to the cell
                    content = new Paragraph("");
                }
    
                content.getStyle().set("margin", "0");
                content.getStyle().set("font-weight", "bold"); // Make the text bold
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

    public String[][] createMatrix(int columns, int rows) {
        String[][] matrix = new String[rows][columns];
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = "";
            }
        }
        return matrix;
    }

    private void mapEventsToMatrix(List<Event> events, String[][] matrix) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1); // Get Monday of the current week
    
        for (Event event : events) {
            LocalDate eventDate = event.getDate();
            LocalTime eventTime = event.getTime();
    
            int col = (int) ChronoUnit.DAYS.between(startOfWeek, eventDate) + 1;
            int row = eventTime.getHour() - 8; // Assuming 9:00 is row 1, so 9:00 minus 8 equals 1
    
            if (row >= 1 && row <= 9 && col >= 1 && col <= 5) {
                matrix[row - 1][col - 1] = event.getDescription(); 
            }
        }
    }
    
    private List<Event> getEvents() {
        // Retrieve the list of Event objects from the database or another source
        // Replace this with your actual implementation
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Login userLogin = loginRepository.findByUsername(currentUsername);

        SamplePerson person = userLogin.getPerson();

        // Get the dates for the current week (Monday to Friday)
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
        List<LocalDate> weekDates = IntStream.range(0, 5)
                .mapToObj(startOfWeek::plusDays)
                .collect(Collectors.toList());

        List<Event> events = new ArrayList<>();
        // Get events for person and dates
    
        // Add some fake events
        events.add(new Event(LocalDate.now().plusDays(1), LocalTime.of(10, 0), "Meeting"));
        events.add(new Event(LocalDate.now().plusDays(2), LocalTime.of(12, 0), "Lunch"));
        events.add(new Event(LocalDate.now().plusDays(3), LocalTime.of(14, 0), "Presentation"));
        events.add(new Event(LocalDate.now().plusDays(4), LocalTime.of(16, 0), "Workshop"));
    
        return events;
    }

    public class Event {
        private LocalDate date;
        private LocalTime time;
        private String description;
    
        public Event(LocalDate date, LocalTime time, String description) {
            this.date = date;
            this.time = time;
            this.description = description;
        }
    
        public LocalDate getDate() {
            return date;
        }
    
        public void setDate(LocalDate date) {
            this.date = date;
        }
    
        public LocalTime getTime() {
            return time;
        }
    
        public void setTime(LocalTime time) {
            this.time = time;
        }
    
        public String getDescription() {
            return description;
        }
    
        public void setDescription(String description) {
            this.description = description;
        }
    }
}
