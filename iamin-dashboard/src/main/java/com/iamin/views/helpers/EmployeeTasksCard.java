package com.iamin.views.helpers;

import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;
import com.iamin.data.service.TasksService;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.function.ValueProvider;

@Component
public class EmployeeTasksCard {

    @Autowired
    private TasksService tasksService;

    public Div createCard(Div card, Login login) {
        Styling.styleSquareBox(card);

        // Labels
        Label title = new Label("Current Tasks");
        title.getStyle().set("font-weight", "bold");
        title.getStyle().set("font-size", "18px");
        title.getStyle().set("margin-bottom","10px");

        // Create a list of notifications
        List<Tasks> tasks = getTasks(login.getPerson());
        
    
        // Create a grid to display the list of notifications
        Grid<Tasks> grid = new Grid<>();
        grid.setHeight("80%");
        grid.setItems(tasks);

        // Set grid width to 100%
        grid.setWidthFull();
        
        // Set columns to taskDescription and deadLine
        grid.addColumn((ValueProvider<Tasks, String>) Tasks::getDescription)
            .setHeader("Description")
            .setSortable(true);

        String dateFormatPattern = "dd/MM";

        LocalDateRenderer<Tasks> deadlineRenderer = new LocalDateRenderer<>(
            (ValueProvider<Tasks, LocalDate>) Tasks::getDeadLine,
            dateFormatPattern
        );
        grid.addColumn(deadlineRenderer)
            .setHeader("Deadline")
            .setSortable(true);

        grid.addColumn(new ComponentRenderer<>(task -> {
            Button button = new Button("Info");
            button.addClickListener(event -> {
                Dialog dialog = new Dialog();
                dialog.add(new Paragraph("Person: " + task.getPerson().getFirstName() + " " + task.getPerson().getLastName()));
                dialog.add(new Paragraph("Description: " + task.getDescription()));
                dialog.add(new Paragraph("Assign Date: " + task.getAssignDate()));
                dialog.add(new Paragraph("Deadline: " + task.getDeadLine()));
                dialog.add(new Paragraph("Completed: " + task.isCompleted()));
                dialog.open();
            });
            return button;
        })).setHeader("More Information");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);

        card.add(title, grid);

        return card;
    }

    public List<Tasks> getTasks(SamplePerson person) {
        // Get tasks that are due for person
        List<Tasks> tasks = tasksService.findCompletedFalseForPerson(person);        

        return tasks;
    }

}
