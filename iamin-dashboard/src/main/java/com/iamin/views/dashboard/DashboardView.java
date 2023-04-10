package com.iamin.views.dashboard;

import com.iamin.views.MainLayout;
import com.iamin.views.helpers.EmployeeAttendanceCard;
import com.iamin.views.helpers.EmployeeTasksCard;
import com.iamin.views.helpers.EmployeesTableCard;
import com.iamin.views.helpers.NotificationsCard;
import com.iamin.views.helpers.AverageAttendanceCard;
import com.iamin.views.helpers.AverageAttendanceChartsCard;
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

import java.util.Collections;

import javax.annotation.security.PermitAll;
import com.vaadin.flow.component.html.Div;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


@CssImport(value = "dashboard-styles.css")
@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class DashboardView extends VerticalLayout {

    String currentUserName;
    String currentUserRole;

    @Autowired
    private final EmployeeAttendanceCard employeeAttendanceCard;

    @Autowired
    private final DepartmentMembersCard departmentMembersCard;

    private final NotificationsCard notificationsCard;
    private final EmployeeTasksCard employeeTasksCard;

    private final PersonFormDialog personFormDialog;
    private final LoginRepository loginRepository;

    public DashboardView(PersonFormDialog personFormDialog, LoginRepository loginRepository,EmployeeAttendanceCard employeeAttendanceCard,DepartmentMembersCard departmentMembersCard,NotificationsCard notificationsCard,EmployeeTasksCard employeeTasksCard) {
        this.personFormDialog = personFormDialog;
        this.loginRepository = loginRepository;
        this.employeeAttendanceCard = employeeAttendanceCard;
        this.departmentMembersCard = departmentMembersCard;
        this.notificationsCard = notificationsCard;
        this.employeeTasksCard = employeeTasksCard;

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
        // TODO: Show Check in / Check out times from database
        Div card1 = new Div();
        EmployeesTableCard employeesTableCard = new EmployeesTableCard();
        employeesTableCard.createCard(card1);
        

        // Check In / Check Out
        // Absence Request 
        // TODO: Send absence requests to database
        Div card2 = new Div();
        employeeAttendanceCard.createCard(card2,authentication);


        // Calendar - All Roles
        // Display all events, holidays and absences with respect to user role
        Div card3 = new Div();
        CalendarCard calendarCard = new CalendarCard();
        calendarCard.createCard(card3);
        
           
        // Department Members - All Roles
        // This specifically shows all coworkers that are in the same department as user
        // TODO: on click, present user with employee's information including contact details and attendance
        Div card4 = new Div();
        departmentMembersCard.createCard(card4,authentication);


        // Employees Average Attendance - Managers Only
        // This specifically shows the average attendance of all employees
        // TODO: Show department attendance which is the same department as current user (Authentication)
        // TODO: Add a grid of employees from department which shows individual attendance
        Div card5a = new Div();
        AverageAttendanceCard averageAttendanceCard = new AverageAttendanceCard();
        averageAttendanceCard.createCard(card5a,userLogin);
                
        // TODO: When the above tasks are completed
        //Div card5b = new Div();
        //AverageAttendanceCard averageAttendanceCard2 = new AverageAttendanceCard();
       // averageAttendanceCard2.createCard(card5a,userLogin);

        // Charts View - Managers Only
        // Show a bar chart with the average attendance for the last 6 months
        Div card6 = new Div();
        AverageAttendanceChartsCard averageAttendanceChartsCard = new AverageAttendanceChartsCard();
        averageAttendanceChartsCard.createCard(card6,userLogin);

        // Notifications card - All Roles
        // Notifies Managers of requests
        // Notifies Employees of denied requests 
        // When notification click, it moves to the necessary view
        Div card7 = new Div();
        notificationsCard.createCard(card7,userLogin);

        // Tasks card - Employee role only
        // Shows current tasks that are not yet completed
        // Query from "events" database for category "tasks" that are not yet completed
        Div card8 = new Div();
        employeeTasksCard.createCard(card8,userLogin);

        
        // Get user's role
        String userRole = getUserRole(authentication);

        
        if ("ROLE_ADMIN".equals(userRole)) {
            // Add cards specific to the admin role
            // Card 1, card 3, card 4, card 5a, card 6, card 7
            cardsContainer.add(card3,card7,card1,card4,card5a,card6);
        } else if ("ROLE_USER".equals(userRole)) {
            // Add cards specific to the user role
            // Card 2, card 3, card 4, card 5b, card 7, card 8
            cardsContainer.add(card2,card3,card4,card7,card8);
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

