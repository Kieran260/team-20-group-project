package com.iamin.views.dashboard;

import com.iamin.views.MainLayout;
import com.iamin.views.helpers.EmployeeAttendanceCard;
import com.iamin.views.helpers.EmployeesTableCard;
import com.iamin.views.helpers.CalendarCard;
import com.iamin.views.helpers.DepartmentMembersCard;
import com.iamin.views.helpers.Styling;
import com.iamin.views.helpers.PersonFormDialog;
import com.iamin.data.entity.Login;
import com.iamin.data.service.LoginRepository;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.PermitAll;
import com.vaadin.flow.component.html.Div;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

@CssImport(value = "dashboard-styles.css")
@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class DashboardView extends VerticalLayout {

    String currentUserName;
    String currentUserRole;

    private final PersonFormDialog personFormDialog;
    private final LoginRepository loginRepository;

    public DashboardView(PersonFormDialog personFormDialog, LoginRepository loginRepository) {
        this.personFormDialog = personFormDialog;
        this.loginRepository = loginRepository;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getStyle().set("background-color","rgba(250, 250, 250)");

        // Checks if current user has a SamplePerson entity and if not shows a sign up dialog
        String currentUsername = authentication.getName();
        Login userLogin = loginRepository.findByUsername(currentUsername);
        if (userLogin != null && userLogin.getPerson() == null) {
            personFormDialog.showPersonFormDialog();
        }
        

        // Master Container
        Div cardsContainer = new Div();
        cardsContainer.setClassName("cardContainer");
        
        // Employees Table Card - Manager View Only
        // This specifically shows all employees that are currently checked in today
        // TODO: Show Check in / Check out times in new column
        Div card1 = new Div();
        EmployeesTableCard employeesTableCard = new EmployeesTableCard();
        employeesTableCard.createCard(card1);
        

        // Check In / Check Out
        // Absence Request 
        Div card2 = new Div();
        EmployeeAttendanceCard employeeAttendanceCard = new EmployeeAttendanceCard();
        employeeAttendanceCard.createCard(card2,authentication);


        // Calendar - All Roles
        // Filter button to decide whether view is today's calendar, weekly or monthly
        // Three calendar views which are interchangable by the filter button
        // Display all events, holidays and absences
        // First increment daily calendar
        Div card3 = new Div();
        CalendarCard calendarCard = new CalendarCard();
        calendarCard.createCard(card3);
        
           
        // Department Members - Employees Only
        // This specifically shows all employees that are in the same department as user
        Div card4 = new Div();
        DepartmentMembersCard departmentMembersCard = new DepartmentMembersCard();
        departmentMembersCard.createCard(card4,authentication);


        // Department Members - Managers Only
        // This specifically shows all employees of a department with an average department attendance
        // Employee can be selected to show individual attendance
        //Div cardx = new Div();
        //styleSquareBox(card4);
                
        // Charts View - All Roles
        // Framework: https://vaadin.com/directory/component/apexchartsjs
        // Task summary
        // Potentially add another card with employee attendance for managers only
        
        Div card5 = new Div();
        Styling.styleSquareBox(card5);
                
        // Notifications card - All Roles
        // Notifies Managers of requests
        // Notifies Employees of denied requests 
        // When notification click, it moves to the necessary view
        Div card6 = new Div();
        Styling.styleSquareBox(card6);
    
        //cardsLayout.add();

        // Tasks card - Employee role only
        // Shows current tasks that are not yet completed
        // Query from "events" database for category "tasks" that are not yet completed
        Div card7 = new Div();
        
        // Get user's role
        String userRole = getUserRole(authentication);

        if ("ROLE_ADMIN".equals(userRole)) {
            // Add cards specific to the admin role
            cardsContainer.add(card1,card2,card3,card4,card5,card6);
        } else if ("ROLE_USER".equals(userRole)) {
            // Add cards specific to the user role
            cardsContainer.add(card2,card3,card4,card5,card6);
        } 

        // All cards

        // Add the content div to the layout
        add(cardsContainer);
    }

    private String getUserRole(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities() != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority.getAuthority();
                if (role != null && role.startsWith("ROLE_")) {
                    return role;
                }
            }
        }
        return null;
    }
}

