package com.iamin.views.helpers;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.select.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class AverageAttendanceCard {

    @Autowired
    private AttendanceCalculator attendanceCalculator;

    public Div createCard(Div card) {
        card.getStyle().set("display", "flex");
        card.getStyle().set("flex-direction", "column");
        card.getStyle().set("justify-content", "flex-start");
        card.getStyle().set("padding", "20px 20px");
        Styling.styleSquareBox(card);
    
        // Create Label
        Label cardHeader = new Label("Department Attendance");
        cardHeader.getStyle().set("font-weight", "bold");
        cardHeader.getStyle().set("font-size", "18px");
    
        // Create subtext Label
        Label subtext = new Label("Compared to Previous Period");
        subtext.getStyle().set("font-size", "14px");
        subtext.getStyle().set("color", "grey");
    
        // Display the average attendance on the card
        Span averageAttendanceSpan = new Span();
        averageAttendanceSpan.addClassName("average-attendance");
        averageAttendanceSpan.getStyle().set("margin-top","10px");
        averageAttendanceSpan.getStyle().set("font-size","48px");
    
        // Add the timePeriodSelect component and its value change listener
        Select<String> timePeriodSelect = createTimePeriodSelect();
        timePeriodSelect.addValueChangeListener(event -> updateAverageAttendance(event.getValue(), card));
    
        card.add(cardHeader, timePeriodSelect, averageAttendanceSpan, subtext);
    
        // Call updateAverageAttendance with the default time period value
        updateAverageAttendance(timePeriodSelect.getValue(), card);
    
        return card;
    }
    

    private void updateAverageAttendance(String timePeriod, Div card) {
        LocalDate startDate;
        LocalDate endDate = LocalDate.now();

        if (timePeriod.equals("This Month")) {
            startDate = endDate.withDayOfMonth(1);
        } else { // "Last 30 days"
            startDate = endDate.minusDays(30);
        }

        // TODO: Calculate average attendance for all employees in the same department as the current user
        // This can be done using an Authentication instance similar to the DepartmentMembersCard and get
        // the current user's department then use a for loop to iterate through all department SamplePerson's
        // and then call the attendanceCalculator on each person_id for each SamplePerson in the for loop.
        double averageAttendance = attendanceCalculator.calculateAverageAttendance(1, startDate, endDate);

        // Update the average attendance on the card
        Span averageAttendanceSpan = (Span) card.getChildren()
                .filter(component -> component.getClass().equals(Span.class))
                .findFirst()
                .orElse(null);

        if (averageAttendanceSpan != null) {
            averageAttendanceSpan.setText(String.format("%.2f%%", averageAttendance * 100));
        }

        // Calculate previous average attendance
        double previousAverageAttendance = attendanceCalculator.calculatePreviousAverageAttendance(1, startDate, endDate);

        // Calculate percentage difference
        double percentageDifference = ((averageAttendance - previousAverageAttendance) / previousAverageAttendance) * 100;

        // Create the badge
        Span badge = createBadge(percentageDifference);

        // Add or update the badge on the card
        Span existingBadge = (Span) card.getChildren()
                .filter(component -> component.getClass().equals(Span.class) && "badge".equals(component.getId().orElse("")))
                .findFirst()
                .orElse(null);

        if (existingBadge == null) {
            card.add(badge);
        } else {
            existingBadge.getStyle().set("background-color", badge.getStyle().get("background-color") != null ? badge.getStyle().get("background-color") : "green");
            existingBadge.getStyle().set("color", badge.getStyle().get("color") != null ? badge.getStyle().get("color") : "white");
            existingBadge.setText(badge.getText());
        }
    }

    private Select<String> createTimePeriodSelect() {
        Select<String> timePeriodSelect = new Select<>();
        timePeriodSelect.setWidth("200px");
        timePeriodSelect.setLabel("Time Period");
        timePeriodSelect.setItems("This Month", "Last 30 days");
        timePeriodSelect.setValue("This Month"); // Set the default value
        return timePeriodSelect;
    }
    
    private Span createBadge(double percentageDifference) {
        Span badge = new Span(String.format("%.1f%%", Math.abs(percentageDifference)));
        badge.setId("badge");
        badge.setWidth("50px");
        badge.getStyle().set("display","inline-flex");
        badge.getStyle().set("justify-content","center");

        if (percentageDifference >= 0) {
            badge.getStyle().set("background-color", "green");
            badge.getStyle().set("color", "white");
        } else {
            badge.getStyle().set("background-color", "red");
            badge.getStyle().set("color", "white");
        }

        badge.getStyle().set("padding", "3px 8px");
        badge.getStyle().set("border-radius", "12px");
        badge.getStyle().set("font-size", "14px");
        badge.getStyle().set("margin-top", "8px");

        return badge;
    }
}
