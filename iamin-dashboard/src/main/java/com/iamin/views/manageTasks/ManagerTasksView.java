package com.iamin.views.manageTasks;

import com.iamin.data.entity.Tasks;
import com.iamin.views.MainLayout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;

@Uses(Icon.class)
@PageTitle("Manage Tasks")
@Route(value = "manage-tasks", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ManagerTasksView extends HorizontalLayout{

    Grid<Tasks> grid = new Grid<>(Tasks.class, false);

    SplitLayout splitLayout = new SplitLayout();

    public ManagerTasksView() {
        addClassName("list-view");

        configureTasks();
        configureAssignBar();
        add(splitLayout);
    }

    
    public void configureTasks() {
        VerticalLayout tasks = new VerticalLayout();
        tasks.setWidth("800px");

        // give grid a class name - as a referece point
        grid.addClassName("manager-assigns-tasks");

        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("description").setAutoWidth(true);
        grid.addColumn("assignDate").setAutoWidth(true);
        grid.addColumn("deadLine").setAutoWidth(true);
        grid.addColumn("dateModified").setAutoWidth(true);
        grid.addColumn("submittedDate").setAutoWidth(true);
        grid.addColumn("completed").setAutoWidth(true);

        tasks.add(grid);

        splitLayout.addToPrimary(tasks);
    }


    public void configureAssignBar() {
        VerticalLayout content = new VerticalLayout();
        content.setWidth("200px");

        // TOP
        Select<String> selectEmployee = new Select<>();
        selectEmployee.setLabel("Select Employee");
        TextField selectText = new TextField();
        selectText.setLabel("Task Description");
        DatePicker dueDate = new DatePicker("Deadline");
        Button button = new Button("Assign Task");
        button.setWidthFull();

        // BOTTOM
        Div progressBarLabel = new Div();
        progressBarLabel.setText("% Assigned A Task");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setValue(0.5);

        content.add(selectEmployee, selectText, dueDate, button, progressBarLabel, progressBar);

        splitLayout.addToSecondary(content);
    }
}
