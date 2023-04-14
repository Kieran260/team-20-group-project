package com.iamin.views.helpers;

import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.CheckInOutService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.select.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmployeeAverageAttendanceCard {

    String department = "";
    Div leftContainer = new Div();
    Div rightContainer = new Div();

    Login userLogin;

    private CheckInOutService checkInOutService;

    @Autowired
    public EmployeeAverageAttendanceCard(CheckInOutService checkInOutService) {
        this.checkInOutService = checkInOutService;
    }

    public Div createCard(Div card, Login login) {
        this.userLogin = login;
        leftContainer.getStyle().set("display","flex");
        leftContainer.getStyle().set("flex-direction","column");

        card.getStyle().set("display", "flex");
        card.getStyle().set("justify-content", "space-between");

        Styling.styleSquareBox(card);


        // Create Label
        Label cardHeader = new Label("Average Attendance");
        cardHeader.getStyle().set("font-weight", "bold");
        cardHeader.getStyle().set("font-size", "18px");



    
        // Create subtext Label
        Label subtext = new Label("Compared to Previous Period:");
        subtext.getStyle().set("font-size", "14px");
        subtext.getStyle().set("color", "grey");
    
        // Display the average attendance on the card
        Span averageAttendanceSpan = new Span();
        averageAttendanceSpan.addClassName("average-attendance");
        averageAttendanceSpan.getStyle().set("margin-top","10px");
        averageAttendanceSpan.getStyle().set("font-size","48px");
    
        // Add the timePeriodSelect component and its value change listener
        Select<String> timePeriodSelect = createTimePeriodSelect();
        timePeriodSelect.addValueChangeListener(event -> updateAverageAttendance(event.getValue(), leftContainer));
    
        leftContainer.add(cardHeader, timePeriodSelect, averageAttendanceSpan, subtext);
        //card.add(cardHeader, timePeriodSelect, averageAttendanceSpan, subtext);
    
        // Call updateAverageAttendance with the default time period value
        updateAverageAttendance(timePeriodSelect.getValue(), leftContainer);

        card.removeAll();
        card.add(leftContainer);

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

        double averageAttendance = calculateAverageAttendance(
            userLogin.getPerson() != null ? userLogin.getPerson() : new SamplePerson(),
            startDate, 
            endDate);

        // Update the average attendance on the card
        Span averageAttendanceSpan = (Span) card.getChildren()
                .filter(component -> component.getClass().equals(Span.class))
                .findFirst()
                .orElse(null);

        if (averageAttendanceSpan != null) {
            averageAttendanceSpan.setText(String.format("%.2f%%", averageAttendance * 100));
        }

        // Calculate previous average attendance
        double previousAverageAttendance = calculatePreviousAverageAttendance(userLogin.getPerson(), startDate, endDate);

        // Calculate percentage difference
        double percentageDifference = ((averageAttendance - previousAverageAttendance) / previousAverageAttendance) * 100;

        // Create the badge
        Span badge = createBadge(percentageDifference);

        // Add or update the badge on the card
        Span existingBadge = (Span) leftContainer.getChildren()
                .filter(component -> component.getClass().equals(Span.class) && "badge".equals(component.getId().orElse("")))
                .findFirst()
                .orElse(null);

        if (existingBadge == null) {
            leftContainer.add(badge);
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
        Span badge = new Span();
        badge.setId("badge");
        badge.setWidth("50px");
        badge.getStyle().set("display","inline-flex");
        badge.getStyle().set("justify-content","center");
    
        if (Double.isInfinite(percentageDifference) || Double.isNaN(percentageDifference)) {
            badge.setText("0%");
            badge.getStyle().set("background-color", "green");
            badge.getStyle().set("color", "white");
        } else {
            badge.setText(String.format("%.1f%%", Math.abs(percentageDifference)));
            if (percentageDifference >= 0) {
                badge.getStyle().set("background-color", "green");
                badge.getStyle().set("color", "white");
            } else {
                badge.getStyle().set("background-color", "red");
                badge.getStyle().set("color", "white");
            }
        }
    
        badge.getStyle().set("padding", "3px 8px");
        badge.getStyle().set("border-radius", "12px");
        badge.getStyle().set("font-size", "14px");
        badge.getStyle().set("margin-top", "8px");
    
        return badge;
    }
    
    
    public double calculateAverageAttendance(SamplePerson person,LocalDate start,LocalDate finish) {

        List<CheckInOut> checkInOutList = Collections.emptyList();

        // Query the checkInOut table to retrieve all the records for the given person_id for the dates provided
        try {
            checkInOutList = checkInOutService.findByPersonAndDateBetween(person, start, finish);
            System.out.println(checkInOutList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage()); 
        }
    
        int totalDaysAttended = 0;
        for (CheckInOut checkInOut : checkInOutList) {
            if (checkInOut.getcheckOutTime() != null) {
                // Count the number of days the person attended
                totalDaysAttended++;
            }
        }
    
        int totalWorkingDays = 0;
        LocalDate date = start;
        while (date.isBefore(finish) || date.equals(finish)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                totalWorkingDays++;
            }
            date = date.plusDays(1);
        }
    
        // Calculate the average attendance
        double averageAttendance = (double) totalDaysAttended / totalWorkingDays;
        System.out.println(averageAttendance);
        return averageAttendance;

    }

    public double calculatePreviousAverageAttendance(SamplePerson person, LocalDate startDate, LocalDate endDate) {
        LocalDate previousStartDate = startDate.minusDays(ChronoUnit.DAYS.between(startDate, endDate));
        LocalDate previousEndDate = endDate.minusDays(ChronoUnit.DAYS.between(startDate, endDate));
    
        return calculateAverageAttendance(person, previousStartDate, previousEndDate);
    }
    
    
    
    
}

    

