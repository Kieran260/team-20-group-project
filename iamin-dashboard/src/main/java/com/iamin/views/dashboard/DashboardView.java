package com.iamin.views.dashboard;

import com.iamin.views.MainLayout;
import com.iamin.views.helpers.EmployeeAttendanceCard;
import com.iamin.views.helpers.EmployeesTableCard;
import com.iamin.views.helpers.Styling;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.PermitAll;
import com.vaadin.flow.component.html.Div;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



@CssImport(value = "dashboard-styles.css")
@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class DashboardView extends VerticalLayout {

    String currentUserName;
    String currentUserRole;

    public DashboardView() {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getStyle().set("background-color","rgba(250, 250, 250)");

        // Master Container
        Div cardsContainer = new Div();
        cardsContainer.setClassName("cardContainer");
        
        // Employees Table
        Div card1 = new Div();
        EmployeesTableCard employeesTableCard = new EmployeesTableCard();
        employeesTableCard.createCard(card1);
        
        // Check In / Check Out
        // Absence Request 
        Div card2 = new Div();
        EmployeeAttendanceCard employeeAttendanceCard = new EmployeeAttendanceCard();
        employeeAttendanceCard.createCard(card2,authentication);



        Div card3 = new Div();
        Styling.styleSquareBox(card3);
                
        Div card4 = new Div();
        Styling.styleSquareBox(card4);
                
        Div card5 = new Div();
        Styling.styleSquareBox(card5);
                
        Div card6 = new Div();
        Styling.styleSquareBox(card6);
    
        //cardsLayout.add();

        // All cards

        
        cardsContainer.add(card1,card2,card3,card4,card5,card6);


        // Add the content div to the layout
        add(cardsContainer);
    }




    private void card3Config(Div card3) {
    }

    private void card4Config(Div card4) {
    }







}
