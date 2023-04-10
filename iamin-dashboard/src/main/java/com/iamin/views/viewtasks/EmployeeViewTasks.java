package com.iamin.views.viewtasks;

import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;
import com.iamin.data.service.LoginService;
import com.iamin.data.service.SamplePersonService;
import com.iamin.data.service.TasksService;
import com.iamin.views.MainLayout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Uses(Icon.class)
@PageTitle("View Tasks")
@Route(value = "view-tasks", layout = MainLayout.class)
@RolesAllowed("USER")
public class EmployeeViewTasks extends VerticalLayout{

   private LoginService loginService;
   private TasksService tasksService;

   @Autowired
   public EmployeeViewTasks(LoginService loginService, TasksService tasksService) {
       this.loginService = loginService;
       this.tasksService = tasksService;
      }

      @PostConstruct
      private void onPostConstruct() {
          configureTasks();
      }
   

   public void configureTasks() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      SamplePerson person = loginService.getSamplePersonByUsername(authentication.getName());
      List<Tasks> tasks = tasksService.findTasksForPerson(person);
      tasks.sort(Comparator.comparing(Tasks::getDeadLine));
      ListDataProvider<Tasks> dataProvider = new ListDataProvider<>(tasks);   
      Grid<Tasks> tasksGrid = new Grid<>();
      VerticalLayout tasksLayout = new VerticalLayout();
      tasksLayout.setWidth("100%");
      setSizeFull();
      setPadding(false); 
      setMargin(false); 
      setAlignItems(Alignment.STRETCH); 
      tasksLayout.setHeightFull();
      tasksLayout.setPadding(false); 
      tasksLayout.setMargin(false);
      tasksGrid.setSizeFull();

      // give grid a class name - as a referece point
      tasksGrid.addClassName("view-tasks");
      tasksGrid.setDataProvider(dataProvider);
      tasksGrid.addColumn(Tasks::getDescription).setHeader("Description").setAutoWidth(true);
      tasksGrid.addColumn(Tasks::getAssignDate).setHeader("Assign Date").setAutoWidth(true);
      tasksGrid.addColumn(Tasks::getDeadLine).setHeader("Deadline").setAutoWidth(true);
      tasksGrid.addColumn(Tasks::getSubmittedDate).setHeader("Submitted Date").setAutoWidth(true);
      
    Column<Tasks> completedColumn = tasksGrid.addComponentColumn(task -> {
      Button completedButton = new Button("Complete");
      completedButton.setEnabled(!task.isCompleted());

      completedButton.addClickListener(event -> {
          Dialog dialog = new Dialog();
          dialog.add(new Label("Are you sure the task is completed ?"));

          Button confirmButton = new Button("Yes", e -> {
              task.setCompleted(true);
              task.setSubmittedDate(LocalDate.now());
              tasksService.update(task);
              completedButton.setEnabled(false);
              dataProvider.refreshItem(task);
              dialog.close();
          });

          Button cancelButton = new Button("No", e -> dialog.close());
          HorizontalLayout buttonLayout = new HorizontalLayout();
          buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
          buttonLayout.add(confirmButton, cancelButton);
          dialog.add(buttonLayout);
          dialog.open();
      });

      return completedButton;
  }).setHeader("Complete");
  
  tasksGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);


      tasksLayout.add(tasksGrid);
      add(tasksLayout);
  }

}