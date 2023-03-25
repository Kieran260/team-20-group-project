package com.iamin.views.helpers;

import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.Login;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.select.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class AverageAttendanceCard {

    String department = "";
    Div leftContainer = new Div();
    Div rightContainer = new Div();


    public Div createCard(Div card, Login login) {
        leftContainer.getStyle().set("display","flex");
        leftContainer.getStyle().set("flex-direction","column");

        card.getStyle().set("display", "flex");
        card.getStyle().set("justify-content", "space-between");

        Styling.styleSquareBox(card);

        // TODO: Add department when Khaled finished with department entity 
        // Currently this code creates an error of ".getDepartment() is null
        if (login != null && login.getPerson() != null) {
            //department = login.getPerson().getDepartment().getDepartmentName();
        }
    
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
    

        rightContainer.getStyle().set("display","flex");
        rightContainer.getStyle().set("flex-direction","column");


        Div barChart = createBarChart();
        barChart.getStyle().set("margin-right","10px");

        Label barChartLabel = new Label("Last 3 Months");
        barChartLabel.getStyle().set("font-size", "14px");
        barChartLabel.getStyle().set("color", "grey");
        barChartLabel.getStyle().set("margin-left", "10px");

        rightContainer.add(barChart,barChartLabel);
        rightContainer.getStyle().set("display","flex");

        card.add(leftContainer,rightContainer);

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
        double averageAttendance = calculateAverageAttendance(1, startDate, endDate);

        // Update the average attendance on the card
        Span averageAttendanceSpan = (Span) card.getChildren()
                .filter(component -> component.getClass().equals(Span.class))
                .findFirst()
                .orElse(null);

        if (averageAttendanceSpan != null) {
            averageAttendanceSpan.setText(String.format("%.2f%%", averageAttendance * 100));
        }

        // Calculate previous average attendance
        double previousAverageAttendance = calculatePreviousAverageAttendance(1, startDate, endDate);

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

    private Div createBarChart() {
        
        LocalDate now = LocalDate.now();
        LocalDate[] monthStarts = {
            now.minusMonths(3).withDayOfMonth(1),
            now.minusMonths(2).withDayOfMonth(1),
            now.minusMonths(1).withDayOfMonth(1),
        };
        LocalDate[] monthEnds = {
            monthStarts[0].plusMonths(1).minusDays(1),
            monthStarts[1].plusMonths(1).minusDays(1),
            monthStarts[2].plusMonths(1).minusDays(1),

        };
            
        int[] attendanceData = {89,95,92};

        String[] monthLabels = new String[3];
        for (int i = 0; i < 3; i++) {
            double averageAttendance = calculateAverageAttendance(1, monthStarts[i], monthEnds[i]);
            attendanceData[i] = (int) Math.round(averageAttendance * 100);
            monthLabels[i] = monthStarts[i].getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
        }
    
        Div barChartContainer = new Div();
        barChartContainer.getStyle().set("display", "flex");
        barChartContainer.getStyle().set("gap", "20px");
        barChartContainer.getStyle().set("margin-bottom", "10px");
        barChartContainer.getStyle().set("width", "100%");
        barChartContainer.getStyle().set("height", "100%");
        barChartContainer.getStyle().set("position", "relative");

    
        for (int i = 0; i < attendanceData.length; i++) {
            Div bar = new Div();
            bar.getStyle().set("width", "30px");
            bar.getStyle().set("height", attendanceData[i]/1.4 + "%");
            bar.getStyle().set("background-color", "#005eec");
            bar.getStyle().set("border-radius", "2.5px");
            bar.getStyle().set("position", "absolute");
            bar.getStyle().set("bottom", "60px");

            Label monthLabel = new Label(monthLabels[i]);
            monthLabel.getStyle().set("text-align", "center");
            monthLabel.getStyle().set("font-weight", "bold");
            monthLabel.getStyle().set("position", "absolute");
            monthLabel.getStyle().set("bottom", "0");

            Label label = new Label(String.valueOf(attendanceData[i]) + "%");
            label.getStyle().set("text-align", "center");
            label.getStyle().set("position", "absolute");
            label.getStyle().set("bottom", "25px");
            

            Div column = new Div();
            column.getStyle().set("display", "flex");
            column.getStyle().set("flex-direction", "column");
            column.getStyle().set("height", "100%");
            column.getStyle().set("width", "30px");
            column.getStyle().set("margin", "0 10px");
    
            column.add(bar, label, monthLabel);
            barChartContainer.add(column);
        }
    
        return barChartContainer;
    }
    
    
    
    public double calculateAverageAttendance(long person_id,LocalDate start,LocalDate finish) {

        // Query the checkInOut table to retrieve all the records for the given person_id for the past 4 weeks
        List<CheckInOut> checkInOutList = Collections.emptyList();
    
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
    
       // return averageAttendance;

       // Remove this when connected to DB
       return 0.96;
    }

    public double calculatePreviousAverageAttendance(long person_id, LocalDate startDate, LocalDate endDate) {
        LocalDate previousStartDate = startDate.minusDays(ChronoUnit.DAYS.between(startDate, endDate));
        LocalDate previousEndDate = endDate.minusDays(ChronoUnit.DAYS.between(startDate, endDate));
    
        return calculateAverageAttendance(person_id, previousStartDate, previousEndDate);
    }
    
    
    
    
}

    

