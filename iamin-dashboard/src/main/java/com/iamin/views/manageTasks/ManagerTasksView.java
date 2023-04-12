package com.iamin.views.manageTasks;

import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;
import com.iamin.data.service.SamplePersonService;
import com.iamin.data.service.TasksService;
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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@Uses(Icon.class)
@PageTitle("Manage Tasks")
@Route(value = "manage-tasks", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ManagerTasksView extends HorizontalLayout{

    Grid<Tasks> grid = new Grid<>(Tasks.class, false);

    SplitLayout splitLayout = new SplitLayout();
    private final TasksService tasksService;
    @Autowired
    private SamplePersonService samplePersonService;

    public ManagerTasksView(TasksService tasksService , SamplePersonService samplePersonService) {
        this.samplePersonService = samplePersonService;
        this.tasksService = tasksService;
        addClassName("list-view");

        splitLayout.getStyle().set("width","100%");

        configureTasks();
        configureAssignBar();
        add(splitLayout);
    }

    
    public void configureTasks() {
        VerticalLayout tasks = new VerticalLayout();
        tasks.setWidth("80%");

        // give grid a class name - as a referece point
        grid.addClassName("manager-assigns-tasks");

        
        grid.addColumn("description").setAutoWidth(true);
        grid.addColumn("assignDate").setAutoWidth(true);
        grid.addColumn("deadLine").setAutoWidth(true);
        grid.addColumn("dateModified").setAutoWidth(true);
        grid.addColumn("submittedDate").setAutoWidth(true);
        grid.addColumn("completed").setAutoWidth(true);
        grid.addColumn(task -> {
            SamplePerson person = task.getPerson();
            return person != null ? person.getFirstName() + " " + person.getLastName() : "";
        }).setHeader("Employee").setAutoWidth(true);
    
        grid.setItems(query -> tasksService.list(
            PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
            .stream());
        tasks.add(grid);
    
        splitLayout.addToPrimary(tasks);
    }
    

    public void configureAssignBar() {
        VerticalLayout content = new VerticalLayout();
        content.setWidth("20%");

        Select<SamplePerson> selectEmployee = new Select<>();
        selectEmployee.setLabel("Select Employee");
        selectEmployee.setItemLabelGenerator(samplePerson -> samplePerson.getFirstName() + " " + samplePerson.getLastName());

        List<SamplePerson> persons = samplePersonService.getAllSamplePersons();
        selectEmployee.setItems(persons);

        TextField selectText = new TextField();
        selectText.setLabel("Task Description");
        DatePicker dueDate = new DatePicker("Deadline");
        selectEmployee.setWidthFull();
        selectText.setWidthFull();
        dueDate.setWidthFull();
        Button button = new Button("Assign Task", event -> {
            SamplePerson selectedPerson = selectEmployee.getValue();
            String taskDescription = selectText.getValue();
            LocalDate deadline = dueDate.getValue();

            if (selectedPerson != null && taskDescription != null && !taskDescription.trim().isEmpty() && deadline != null) {
                Tasks task = new Tasks();
                task.setPerson(selectedPerson);
                task.setDescription(taskDescription);
                task.setDeadLine(deadline);
                task.setAssignDate(LocalDate.now());
                task.setCompleted(false);

                tasksService.create(task);

                Notification.show("Task assigned successfully.", 3000, Notification.Position.TOP_CENTER);
                selectText.clear();
                dueDate.clear();
                grid.getDataProvider().refreshAll();
            } else {
                Notification.show("Please fill all required fields.", 3000, Notification.Position.TOP_CENTER);
            }
        });

        button.setWidthFull();

        content.add(selectEmployee, selectText, dueDate, button);

        splitLayout.addToSecondary(content);
    }
}
