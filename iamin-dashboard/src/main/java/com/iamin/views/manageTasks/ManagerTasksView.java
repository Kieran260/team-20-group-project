package com.iamin.views.ManageTasks;

import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;
import com.iamin.data.service.LoginRepository;
import com.iamin.data.service.SamplePersonService;
import com.iamin.data.service.TasksService;
import com.iamin.data.validation.Validation;
import com.iamin.views.MainLayout;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Uses(Icon.class)
@PageTitle("Manage Tasks")
@Route(value = "manage-tasks", layout = MainLayout.class)
@RolesAllowed("ADMIN")

public class ManagerTasksView extends HorizontalLayout implements BeforeEnterObserver{

    @Autowired
    LoginRepository loginRepository;
    // Checks if current user has a SamplePerson entity and if not shows a sign up dialog
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Login userLogin;
    String currentUsername = authentication.getName();
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        try{
            userLogin = loginRepository.findByUsername(currentUsername);
        }catch(Exception e){
            userLogin = null;
        }
        // Check your condition and redirect if necessary
        boolean Redirect = (userLogin != null && userLogin.getPerson() == null);
        if (Redirect) {
            UI.getCurrent().getPage().executeJs("location.href = 'dashboard'");
        }
    }
    
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
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setWidth("80%");
        
        grid.addColumn("title").setAutoWidth(true);
        grid.addColumn("description").setAutoWidth(true);
        grid.addColumn("assignDate").setAutoWidth(true);
        grid.addColumn("deadLine").setAutoWidth(true);
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
    
        splitLayout.addToPrimary(grid);
    }
    

    public void configureAssignBar() {
        VerticalLayout content = new VerticalLayout();
        content.setWidth("20%");

        Select<SamplePerson> selectEmployee = new Select<>();
        selectEmployee.setLabel("Select Employee");
        selectEmployee.setItemLabelGenerator(samplePerson -> samplePerson.getFirstName() + " " + samplePerson.getLastName());

        List<SamplePerson> persons = samplePersonService.getAllSamplePersons();
        selectEmployee.setItems(persons);

        TextField titleField = new TextField();
        titleField.setLabel("Task Title");
        TextField descriptionField = new TextField();
        descriptionField.setLabel("Task Description");
        DatePicker dueDate = new DatePicker("Deadline");

        selectEmployee.setWidthFull();
        titleField.setWidthFull();
        descriptionField.setWidthFull();
        dueDate.setWidthFull();

        Button button = new Button("Assign Task", event -> {
            SamplePerson selectedPerson = selectEmployee.getValue();
            String taskDescription = descriptionField.getValue();
            String taskTitle = titleField.getValue();
            LocalDate deadline = dueDate.getValue();

            if (selectedPerson != null && taskDescription != null && !taskDescription.trim().isEmpty() && deadline != null) {
                if(Validation.isAfterCurrentDate(deadline)){
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
                }else{
                    dueDate.clear();
                    Notification.show("Deadline must be in the future.", 3000, Notification.Position.TOP_CENTER);
                }
            } else {
                Notification.show("Please fill all required fields.", 3000, Notification.Position.TOP_CENTER);
            }
        });

        button.setWidthFull();

        content.add(selectEmployee,titleField,descriptionField, dueDate, button);

        splitLayout.addToSecondary(content);
    }
}
