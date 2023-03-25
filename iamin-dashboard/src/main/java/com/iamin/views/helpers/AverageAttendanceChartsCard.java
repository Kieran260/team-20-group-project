package com.iamin.views.helpers;

import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.Login;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.Label;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class AverageAttendanceChartsCard {

    Div chartsContainer = new Div();

    public Div createCard(Div card, Login login) {
        card.getStyle().set("display", "flex");
        card.getStyle().set("justify-content", "space-between");

        Styling.styleSquareBox(card);

    
        // Create Label
        Label cardHeader = new Label("Last 6 Months");
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

        
        chartsContainer.getStyle().set("display","flex");
        chartsContainer.getStyle().set("flex-direction","column");


        Div barChart = createBarChart();
        barChart.getStyle().set("margin-right","10px");

        Label barChartLabel = new Label("Last 3 Months");
        barChartLabel.getStyle().set("font-size", "14px");
        barChartLabel.getStyle().set("color", "grey");
        barChartLabel.getStyle().set("margin-left", "10px");

        chartsContainer.add(cardHeader,barChart,barChartLabel);
        chartsContainer.getStyle().set("display","flex");

        card.add(chartsContainer);

        return card;
    }
    

    private Div createBarChart() {
        
        LocalDate now = LocalDate.now();
        LocalDate[] monthStarts = {
            now.minusMonths(5).withDayOfMonth(1),
            now.minusMonths(4).withDayOfMonth(1),
            now.minusMonths(3).withDayOfMonth(1),
            now.minusMonths(2).withDayOfMonth(1),
            now.minusMonths(1).withDayOfMonth(1),
            now.withDayOfMonth(1),
        };
        LocalDate[] monthEnds = {
            monthStarts[0].plusMonths(1).minusDays(1),
            monthStarts[1].plusMonths(1).minusDays(1),
            monthStarts[2].plusMonths(1).minusDays(1),
            monthStarts[3].plusMonths(1).minusDays(1),
            monthStarts[4].plusMonths(1).minusDays(1),
            monthStarts[5].plusMonths(1).minusDays(1),

        };
            
        int[] attendanceData = {89,95,92,96,92,91};

        String[] monthLabels = new String[6];
        for (int i = 0; i < 6; i++) {
            double averageAttendance = calculateAverageAttendance(1, monthStarts[i], monthEnds[i]);
            attendanceData[i] = (int) Math.round(averageAttendance * 100);
            monthLabels[i] = monthStarts[i].getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
        }
    
        Div barChartContainer = new Div();
        barChartContainer.getStyle().set("display", "flex");
        barChartContainer.getStyle().set("gap", "10%");
        barChartContainer.getStyle().set("margin-bottom", "10px");
        barChartContainer.getStyle().set("width", "100%");
        barChartContainer.getStyle().set("height", "100%");
        barChartContainer.getStyle().set("position", "relative");

    
        for (int i = 0; i < attendanceData.length; i++) {
            Div bar = new Div();
            bar.getStyle().set("width", "30px");
            bar.getStyle().set("height", attendanceData[i]/1.5 + "%");
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
       return 1.00;
    }

    public double calculatePreviousAverageAttendance(long person_id, LocalDate startDate, LocalDate endDate) {
        LocalDate previousStartDate = startDate.minusDays(ChronoUnit.DAYS.between(startDate, endDate));
        LocalDate previousEndDate = endDate.minusDays(ChronoUnit.DAYS.between(startDate, endDate));
    
        return calculateAverageAttendance(person_id, previousStartDate, previousEndDate);
    }
    
}

    

