package com.iamin.views.helpers;

import com.iamin.views.helpers.AttendanceCalculator;

import com.iamin.views.helpers.Styling;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

@Component
public class AverageAttendanceCard {

    @Autowired
    private AttendanceCalculator attendanceCalculator;

    public Div createCard(Div card) {
        Styling.styleSquareBox(card);

        // Calculate the average attendance for the person
        double averageAttendance = attendanceCalculator.calculateAverageAttendance(1);

        
        // Display the average attendance on the card
        Span averageAttendanceSpan = new Span(String.format("%.2f%%", averageAttendance * 100));
        averageAttendanceSpan.addClassName("average-attendance");

        card.add(averageAttendanceSpan);

        return card;
    }


}
