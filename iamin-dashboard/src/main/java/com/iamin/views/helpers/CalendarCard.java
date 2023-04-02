package com.iamin.views.helpers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;


public class CalendarCard {

    Grid<Event> calendarGrid = new Grid();

    public Div createCard(Div card) {
        Styling.styleSquareBox(card);
    
        // Header Items
        Label date = new Label("Calendar");
        date.getStyle().set("font-weight", "bold");
        date.getStyle().set("font-size", "18px");
    
        // Header + Button Container Items
        Button backButton = new Button("<");
        backButton.getStyle().set("min-width", "40px");
        backButton.getStyle().set("max-width", "40px");
        backButton.getStyle().set("padding", "0");
    
        DatePicker datePicker = new DatePicker("");
        datePicker.setValue(LocalDate.now());
        datePicker.getStyle().set("max-width","150px");
        datePicker.getStyle().set("min-width","40px");
    
        backButton.addClickListener(event -> {
            LocalDate currentValue = datePicker.getValue();
            datePicker.setValue(currentValue.minusDays(1));
        });
    
        Button forwardButton = new Button(">");
        forwardButton.getStyle().set("min-width", "40px");
        forwardButton.getStyle().set("max-width", "40px");
        forwardButton.getStyle().set("padding", "0");
    
        forwardButton.addClickListener(event -> {
            LocalDate currentValue = datePicker.getValue();
            datePicker.setValue(currentValue.plusDays(1));
        });
    
        // Button Container Configuration
        Div buttonContainer = new Div();
        buttonContainer.getStyle().set("display", "flex");
        buttonContainer.getStyle().set("gap", "5px");
        buttonContainer.getStyle().set("margin-left","10px");
        buttonContainer.add(backButton, datePicker, forwardButton);
    
        FlexLayout header = new FlexLayout();
        header.getStyle().set("justify-content","space-between");
        header.getStyle().set("align-items","center");
        header.add(date,buttonContainer);
    
        // Master Container
        FlexLayout calendar = new FlexLayout();
        calendar.getStyle().set("flex-direction","column");

        // Calendar grid configuration
        calendarGrid.getStyle().set("height","180px");
        calendarGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        calendarGrid.addColumn(Event::getEventName).setHeader("Name");
        calendarGrid.addColumn(Event::getEventType).setHeader("Type");
        calendarGrid.addColumn(Event::getEventTime).setHeader("Time");
        getEvents(1, LocalDate.now());

        calendar.add(calendarGrid);
        card.add(header,calendar);

        // Functionality for updating grid
        datePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            getEvents(1, selectedDate); // Replace '1' with the personId you want to pass.
        });

        return card;
    }

    // To be updated to fetch events when event repository is implemented
    // TODO: Update to fetch events from database
    private void getEvents(int personId, LocalDate date) {
        System.out.println("Fetching events for personId: " + personId + " and date: " + date);

        List<Event> events = List.of(
            new Event("Meeting", "Business", LocalTime.now()),
            new Event("Presentation", "Product", LocalTime.now().plusHours(1)),
            new Event("Conference", "Industry", LocalTime.now().plusHours(2))
        );

        calendarGrid.setItems(events);
    }

    private static class Event {
        private final String eventName;
        private final String eventType;
        private final LocalTime eventTime;

        public Event(String firstName, String lastName, LocalTime eventTime) {
            this.eventName = firstName;
            this.eventType = lastName;
            this.eventTime = eventTime;
        }
    
        public String getEventName() {
            return eventName;
        }
    
        public String getEventType() {
            return eventType;
        }

        public String getEventTime() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return eventTime.format(formatter);
        }
        
    }
}
