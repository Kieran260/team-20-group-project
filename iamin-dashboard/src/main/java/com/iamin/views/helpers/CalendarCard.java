package com.iamin.views.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;


import com.iamin.data.entity.Events;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.EventService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalendarCard {

    private EventService eventService;

    Grid<CalendarItem> calendarGrid = new Grid();

    @Autowired
    public CalendarCard(EventService eventService) {
        this.eventService = eventService;
    }

    public Div createCard(Div card, Login login) {
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
        calendarGrid.addColumn(CalendarItem::getItemName).setHeader("Name");
        calendarGrid.addColumn(CalendarItem::getItemType).setHeader("Type");
        calendarGrid.addColumn(CalendarItem::getItemTime).setHeader("Time");
        getEvents(login.getPerson(), LocalDate.now());

        calendar.add(calendarGrid);
        card.add(header,calendar);

        // Functionality for updating grid
        datePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = datePicker.getValue();
            getEvents(login.getPerson(), selectedDate); 
        });

        return card;
    }

    // To be updated to fetch events from repository
    private void getEvents(SamplePerson person, LocalDate date) {
        List<CalendarItem> calendarItems = new ArrayList<>();
        
        // Get events
        if (person != null) {
            List<Events> events = eventService.findEventsForDate(person, date);
            for (Events event : events) {
                if (event.getEventTime() != null) {
                    CalendarItem item = new CalendarItem(
                        event.getEventTitle(), 
                        event.getEventType(), 
                        event.getEventTime());
                    calendarItems.add(item);
                }
            }          
        }
    
        // Sort calendarItems by time, earliest first
        List<CalendarItem> sortedCalendarItems = calendarItems.stream()
            .sorted(Comparator.comparing(CalendarItem::getItemTime))
            .collect(Collectors.toList());
    
        calendarGrid.setItems(sortedCalendarItems); 
    }

    private static class CalendarItem {
        private final String itemName;
        private final String itemType;
        private final LocalTime itemTime;
    
        public CalendarItem(String itemName, String itemType, LocalTime itemTime) {
            this.itemName = itemName;
            this.itemType = itemType;
            this.itemTime = itemTime;
        }
    
        public String getItemName() {
            return itemName;
        }
    
        public String getItemType() {
            return itemType;
        }

        public LocalTime getItemTime() {
            return itemTime;
        }
    
        public String getItemTimeString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return itemTime.format(formatter);
        }
    }
    
}
