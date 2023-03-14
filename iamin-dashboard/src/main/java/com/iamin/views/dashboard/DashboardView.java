package com.iamin.views.dashboard;

import com.iamin.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.PermitAll;
import java.util.List;
import com.vaadin.flow.component.html.Label;
import java.util.ArrayList;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.dialog.Dialog;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;


@CssImport(value = "dashboard-styles.css")
@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll


public class DashboardView extends VerticalLayout {

    String currentUserName;
    String currentUserRole;

    // Card 2
    Dialog holidayDialog = new Dialog();
    Dialog absenceDialog = new Dialog();


    public DashboardView() {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        getStyle().set("background-color","rgba(250, 250, 250)");

        // Master Container
        Div cardsContainer = new Div();
        cardsContainer.setClassName("cardContainer");
        
        // Employees Table Card    
        Div card1 = new Div();
        card1Config(card1);
        
        // Check In / Check Out Card
        Div card2 = new Div();
        card2Config(card2,authentication);



        Div card3 = new Div();
        styleSquareBox(card3);
                
        Div card4 = new Div();
        styleSquareBox(card4);
                
        Div card5 = new Div();
        styleSquareBox(card5);
                
        Div card6 = new Div();
        styleSquareBox(card6);
    
        //cardsLayout.add();
        cardsContainer.add(card1,card2,card3,card4,card5,card6);


        add(holidayDialog,absenceDialog);


        // Add the content div to the layout
        add(cardsContainer);
    }








    private void card1Config(Div card1) {

        // Card1 Styles
        card1.getStyle().set("display","flex");
        card1.getStyle().set("flex-direction","column");
        card1.getStyle().set("justify-content","space-between");
        card1.getStyle().set("padding","20px 10px");
        styleSquareBox(card1);


        // TODO: Populate people with employees
        List<Person> people = new ArrayList<Person>();
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
    
        ListDataProvider<Person> dataProvider = new ListDataProvider<>(people);

        // Create the employeeTable and set its data provider
        Grid<Person> employeeTable = new Grid<>();
        employeeTable.setDataProvider(dataProvider);
    
        // Add columns to the employeeTable
        employeeTable.addColumn(Person::getFirstName).setHeader("First Name");
        employeeTable.addColumn(Person::getLastName).setHeader("Last Name");
    
        // Set the height of the employeeTable to be the height of five rows,
        // or add a scroll bar if there are more than five rows
        int numberOfRows = Math.min(8, people.size());
        employeeTable.setAllRowsVisible(false);
        employeeTable.getStyle().set("overflow", "hidden");
        employeeTable.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        

        // Create Labels
        Label card1Header = new Label("Your Department's Employees");
        card1Header.getStyle().set("font-weight", "bold");
        card1Header.getStyle().set("font-size", "18px");
        card1Header.getStyle().set("margin-left","10px");

        card1.add(card1Header,employeeTable);
    }
    
    private void card2Config(Div card2, Authentication authentication) {
        card2.getStyle().set("display","flex");
        card2.getStyle().set("flex-direction","column");
        card2.getStyle().set("justify-content","flex-start");
        card2.getStyle().set("gap","20px");

        Div card2Top = new Div();
        card2Top.getStyle().set("display","flex");
        card2Top.getStyle().set("flex-direction","column");
        card2Top.getStyle().set("justify-content","flex-start");
        styleHalfSquareBox(card2Top);

        Label card2Header = new Label("Work Hours");
        card2Header.getStyle().set("font-weight", "bold");
        card2Header.getStyle().set("font-size", "18px");


        // TODO: 
        // Change authentication.getName() to fetch the legal first and last name
        // Query the table to see if the user is actually checked in currently or not and update statusLabel 
        
        Label statusLabel = new Label(authentication.getName() + ": You are currently not checked in");
        statusLabel.getStyle().set("font-size", "16px");
    
        Button checkInButton = new Button("Check In");
        checkInButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button checkOutButton = new Button("Check Out");
        checkOutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button editTimesheetButton = new Button("Edit");

        FlexLayout buttonContainer = new FlexLayout();        
        buttonContainer.getStyle().set("gap","10px");
        buttonContainer.getStyle().set("align-items", "start");
        buttonContainer.getStyle().set("margin-top", "10px");
        buttonContainer.add(checkInButton,checkOutButton,editTimesheetButton);

        card2Top.add(card2Header,statusLabel,buttonContainer);

        Div card2Bottom = new Div();
        styleHalfSquareBox(card2Bottom);


        Label card2BottomHeader = new Label("Request Absence");
        card2BottomHeader.getStyle().set("font-weight", "bold");
        card2BottomHeader.getStyle().set("font-size", "18px");

        FlexLayout absenceButtonContainer = new FlexLayout();
        Button holidayRequestButton = new Button("Holiday Request");
        Button absenceRequestButton = new Button("Other Absence");
        absenceButtonContainer.getStyle().set("margin-top","10px");
        absenceButtonContainer.getStyle().set("gap", "10px");   
        absenceButtonContainer.add(holidayRequestButton, absenceRequestButton);


        // Holiday Dialog
        VerticalLayout holidayDialogLayout = new VerticalLayout();
        holidayDialogLayout.getStyle().set("width","280px");
        holidayDialogLayout.getStyle().set("height","350px");
        holidayDialogLayout.getStyle().set("justify-content","center");
        holidayDialogLayout.getStyle().set("align-items","center");

        holidayDialog.setHeaderTitle("Holiday Request");
        
        // TODO:
        // Fetch and calculate holidays remaining for current user from database
        int holidaysRemaining = 0;
        int holidaysSelected = 0;

        Label holidaysRemainingLabel = new Label("You have " + holidaysRemaining + " holidays remaining");
        TextField holidayName = new TextField("Reason for request");
        holidayName.setAutocomplete(Autocomplete.OFF);
        DatePicker fromDate = new DatePicker("Holiday Start");
        DatePicker toDate = new DatePicker("Holiday End");
        Label holidaysSelectedLabel = new Label("Holidays Requested: " + holidaysSelected);


        holidayDialogLayout.add(holidaysRemainingLabel,holidayName,fromDate,toDate,holidaysSelectedLabel);

        holidayDialog.add(holidayDialogLayout);

        Button holidaySubmitButton = new Button("Submit", ee -> holidayDialog.close());
        Button holidayCancelButton = new Button("Cancel", ee -> holidayDialog.close());
        holidayDialog.getFooter().add(holidaySubmitButton, holidayCancelButton);
       
        holidayRequestButton.addClickListener(e -> {
            holidayDialog.open();
        });

        holidaySubmitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        // Absence Dialog
        VerticalLayout absenceDialogLayout = new VerticalLayout();
        absenceDialogLayout.getStyle().set("width","280px");
        absenceDialogLayout.getStyle().set("justify-content","center");
        absenceDialogLayout.getStyle().set("align-items","center");
        absenceDialog.setHeaderTitle("Absence Request");

        TextField absenceName = new TextField("Reason for absence");
        absenceName.setAutocomplete(Autocomplete.OFF);
        DatePicker absenceFromDate = new DatePicker("Absence Start");
        DatePicker absenceToDate = new DatePicker("Absence End");
        absenceDialogLayout.add(absenceName,absenceFromDate,absenceToDate);
        absenceDialog.add(absenceDialogLayout);

        // TODO: 
        // AFTER VALIDATION submit absence request to DB
        Button absenceSubmitButton = new Button("Submit", ee -> 
            absenceDialog.close()
        );

        Button absenceCancelButton = new Button("Cancel", ee -> absenceDialog.close());
        absenceDialog.getFooter().add(absenceSubmitButton, absenceCancelButton);
       
        absenceRequestButton.addClickListener(e -> {
            absenceDialog.open();
        });

        absenceSubmitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);






        // Add cards
        card2Bottom.add(card2BottomHeader,absenceButtonContainer);
        card2.add(card2Top,card2Bottom);
        


    }

    private void card3Config(Div card3) {
    }

    private void card4Config(Div card4) {
    }




    private static class Person {
        private final String firstName;
        private final String lastName;

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }

    private void styleSquareBox(Div div) {
        div.getStyle().set("background-color", "white");
        div.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.25)");
        div.setClassName("card");
    }

    private void styleHalfSquareBox(Div div) {
        div.getStyle().set("background-color", "white");
        div.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.25)");
        div.getStyle().set("height", "120px");

        div.setClassName("card");
    }
}
