package com.iamin.views.timetable;

import com.iamin.views.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;

@CssImport(value = "timetable-grid.css")
@PageTitle("Timetable")
@Route(value = "TimetableView", layout = MainLayout.class)
@PermitAll
public class TimetableView extends VerticalLayout {

    private FlexLayout timetableGrid;

    public TimetableView() {
        setSizeFull();
        initTimetableGrid();
        add(timetableGrid);
    }
    
    private void initTimetableGrid() {
        timetableGrid = new FlexLayout();
        timetableGrid.addClassName("timetable-grid");
        timetableGrid.setFlexWrap(FlexWrap.WRAP);
    
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1); // Get Monday of the current week
    
        // Create 6x10 grid cells
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 6; col++) {
                Div cell = new Div();
                cell.addClassName("grid-cell");
    
                Paragraph content;
    
                if (row == 0 && col > 0) {
                    // Display the day of the week and date in the format "Day DD/MM"
                    LocalDate dayOfWeek = startOfWeek.plusDays(col - 1);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd/MM");
                    String formattedDate = dayOfWeek.format(formatter);
                    content = new Paragraph(formattedDate);
                } else if (col == 0 && row > 0) {
                    // Display the time labels in the first column
                    int hour = 8 + row; // Start at 9:00 and increment per hour
                    content = new Paragraph(hour + ":00");
                } else {
                    // Add content to the cell
                    content = new Paragraph("Row: " + row + ", Col: " + col);
                }
    
                content.getStyle().set("margin", "0");
                content.getStyle().set("font-weight", "bold"); // Make the text bold
                content.getStyle().set("display", "flex"); // Center the text horizontally
                content.getStyle().set("justify-content", "center");
                content.getStyle().set("align-items", "center"); // Center the text vertically
                content.getStyle().set("height", "100%"); // Make the paragraph fill the cell height
    
                cell.add(content);
    
                timetableGrid.add(cell);
            }
        }
    }
    
    
    
}
