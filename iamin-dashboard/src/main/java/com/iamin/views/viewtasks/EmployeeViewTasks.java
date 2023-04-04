package com.iamin.views.viewtasks;

import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;
import com.iamin.data.service.SamplePersonService;
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


@Uses(Icon.class)
@PageTitle("View Tasks")
@Route(value = "manage-tasks", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class EmployeeViewTasks extends HorizontalLayout{

   Grid<Tasks> grid = new Grid<>(Tasks.class, false);

   public EmployeeViewTasks() {
      configureTasks();
   }

   public void configureTasks() {
      VerticalLayout tasks = new VerticalLayout();
      tasks.setWidth("1000px");

      // give grid a class name - as a referece point
      grid.addClassName("manager-assigns-tasks");

      grid.addColumn("person").setAutoWidth(true);
      grid.addColumn("description").setAutoWidth(true);
      grid.addColumn("assignDate").setAutoWidth(true);
      grid.addColumn("deadLine").setAutoWidth(true);
      grid.addColumn("dateModified").setAutoWidth(true);
      grid.addColumn("submittedDate").setAutoWidth(true);
      grid.addColumn("completed").setAutoWidth(true);
  
      tasks.add(grid);
      add(tasks);
  }

}