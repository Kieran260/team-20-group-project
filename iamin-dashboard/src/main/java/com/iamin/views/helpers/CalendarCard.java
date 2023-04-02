package com.iamin.views.helpers;

import java.time.LocalTime;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

public class CalendarCard {


    public Div createCard(Div card) {
        Styling.styleSquareBox(card);

        Label date = new Label("Today");
        date.getStyle().set("font-weight", "bold");
        date.getStyle().set("font-size", "18px");

        Button backButton = new Button("<");
        backButton.getStyle().set("min-width", "40px");
        backButton.getStyle().set("max-width", "40px");
        backButton.getStyle().set("padding", "0");

        Button todayButton = new Button("Today");

        Button forwardButton = new Button(">");
        forwardButton.getStyle().set("min-width", "40px");
        forwardButton.getStyle().set("max-width", "40px");
        forwardButton.getStyle().set("padding", "0");


        Div buttonContainer = new Div();
        buttonContainer.getStyle().set("display", "flex");
        buttonContainer.getStyle().set("gap", "5px");
        buttonContainer.add(backButton, todayButton, forwardButton);

        FlexLayout header = new FlexLayout();
        header.getStyle().set("justify-content","space-between");

        header.add(date,buttonContainer);

        FlexLayout calendar = new FlexLayout();
        calendar.getStyle().set("flex-direction","column");


        Grid<Event> calendarGrid = new Grid();
        calendarGrid.getStyle().set("height","180px");
        calendarGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        calendarGrid.addColumn(Event::getEventName).setHeader("Name");
        calendarGrid.addColumn(Event::getEventType).setHeader("Type");
        calendarGrid.addColumn(Event::getEventTime).setHeader("Time");

        calendar.add(calendarGrid);

        card.add(header,calendar);

        return card;
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
            return "9:00";
        }
    }
}
