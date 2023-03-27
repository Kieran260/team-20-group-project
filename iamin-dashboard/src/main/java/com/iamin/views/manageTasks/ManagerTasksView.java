package com.iamin.views.manageTasks;

import com.iamin.data.entity.AssignTasks;
import com.iamin.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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

    Grid<AssignTasks> grid = new Grid<>(AssignTasks.class);

    public ManagerTasksView() {
        addClassName("list-view");
        
        configureTasks();
        configureAssignBar();
    }

    public void configureTasks() {
        VerticalLayout tasks = new VerticalLayout();
        tasks.setWidth("800px");
        tasks.setHeightFull();

        // set title
        Button title = new Button("Current Tasks");
        tasks.add(title);

        // give grid a class name - as a referece point
        grid.addClassName("manager-assigns-tasks");
        tasks.add(grid);

        add(tasks);
    }

    public void configureAssignBar() {
        VerticalLayout content = new VerticalLayout();
        content.setWidth("200px");
        content.setHeightFull();

        // TOP
        TextField selectEmployee = new TextField();
        selectEmployee.setLabel("Select Employee");
        TextField selectText = new TextField();
        selectText.setLabel("Select Task");
        DatePicker dueDate = new DatePicker("Due Date");
        Button button = new Button("Assign Task");
        button.setWidthFull();

        // BOTTOM
        Button progress = new Button("% Assigned A Task");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setValue(0.5);

        content.add(selectEmployee, selectText, dueDate, button, progress, progressBar);

        add(content);
    }
}
