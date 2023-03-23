package com.iamin.views.helpers;
import com.iamin.views.helpers.EmployeeAttendanceCard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class EmployeeAttendanceCard {


    public Div createCard(Div card, Authentication authentication) {
        card.getStyle().set("display","flex");
        card.getStyle().set("flex-direction","column");
        card.getStyle().set("justify-content","flex-start");
        card.getStyle().set("gap","20px");


        //================================================================================
        // Check In / Check Out Card
        //================================================================================

        Div workHoursCard = new Div();
        workHoursCard.getStyle().set("display","flex");
        workHoursCard.getStyle().set("flex-direction","column");
        workHoursCard.getStyle().set("justify-content","flex-start");
        Styling.styleHalfSquareBox(workHoursCard);

        Label card2Header = new Label("Work Hours");
        card2Header.getStyle().set("font-weight", "bold");
        card2Header.getStyle().set("font-size", "18px");


        // TODO: 
        // Change authentication.getName() to fetch the legal first and last name
        // Query the table to see if the user is actually checked in currently or not and update statusLabel 
        
        // TODO: Determine if user is checked in and display to label
        Label statusLabel = new Label(authentication.getName() + ": You are currently not checked in");
        statusLabel.getStyle().set("font-size", "16px");
    
        Button checkInButton = new Button("Check In");
        checkInButton.addClickListener(e -> {
            Dialog confirmDialog = new Dialog();
            confirmDialog.setCloseOnEsc(false);
            confirmDialog.setCloseOnOutsideClick(false);

            VerticalLayout confirmContent = new VerticalLayout();
            Label confirmMessage = new Label("Are you sure you want to check in?");
            confirmContent.add(confirmMessage);

            Button confirmButton = new Button("Yes", event -> {
                confirmDialog.close();

                //TODO: Change LocalDateTime.now() to CheckInOut.getClockInTime();
                LocalDateTime checkOutTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String formattedTime = checkOutTime.format(formatter);

                //TODO: Log check in to database here
                Notification.show("Success! Checked in at " + formattedTime, 3000, Position.TOP_CENTER);
            });

            Button cancelButton = new Button("No", event -> {
                confirmDialog.close();
            });

            FlexLayout buttonLayout = new FlexLayout();
            buttonLayout.getStyle().set("gap","20px");
            buttonLayout.getStyle().set("margin","0 auto");
            confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            buttonLayout.add(confirmButton,cancelButton);

            confirmContent.add(buttonLayout);
            confirmDialog.add(confirmContent);
            confirmDialog.open();
        });

        // TODO: If user is already checked in, grey out checkInButton and disable clicking
        checkInButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button checkOutButton = new Button("Check Out");
        checkOutButton.addClickListener(e -> {
            Dialog confirmDialog = new Dialog();
            confirmDialog.setCloseOnEsc(false);
            confirmDialog.setCloseOnOutsideClick(false);

            VerticalLayout confirmContent = new VerticalLayout();
            Label confirmMessage = new Label("Are you sure you want to check out?");
            confirmContent.add(confirmMessage);

            Button confirmButton = new Button("Yes", event -> {
                confirmDialog.close();

                //TODO: Change LocalDateTime.now() to CheckInOut.getClockInTime();
                LocalDateTime checkOutTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String formattedTime = checkOutTime.format(formatter);

                //TODO: Log check in to database here
                Notification.show("Success! Checked out at " + formattedTime, 3000, Position.TOP_CENTER);
            });

            Button cancelButton = new Button("No", event -> {
                confirmDialog.close();
            });

            FlexLayout buttonLayout = new FlexLayout();
            buttonLayout.getStyle().set("gap","20px");
            buttonLayout.getStyle().set("margin","0 auto");
            confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            buttonLayout.add(confirmButton,cancelButton);

            confirmContent.add(buttonLayout);
            confirmDialog.add(confirmContent);
            confirmDialog.open();
        });

        // TODO: If user is already checked out, grey out checkOutButton and disable clicking
        checkOutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FlexLayout buttonContainer = new FlexLayout();        
        buttonContainer.getStyle().set("gap","10px");
        buttonContainer.getStyle().set("align-items", "start");
        buttonContainer.getStyle().set("margin-top", "10px");
        buttonContainer.add(checkInButton,checkOutButton);

        workHoursCard.add(card2Header,statusLabel,buttonContainer);

        //================================================================================
        // Absence Card
        //================================================================================
        Div absenceCard = new Div();
        Styling.styleHalfSquareBox(absenceCard);


        Label card2BottomHeader = new Label("Request Absence");
        card2BottomHeader.getStyle().set("font-weight", "bold");
        card2BottomHeader.getStyle().set("font-size", "18px");

        FlexLayout absenceButtonContainer = new FlexLayout();
        Button holidayRequestButton = new Button("Holiday Request");
        Button absenceRequestButton = new Button("Other Absence");
        absenceButtonContainer.getStyle().set("margin-top","10px");
        absenceButtonContainer.getStyle().set("gap", "10px");   
        absenceButtonContainer.add(holidayRequestButton, absenceRequestButton);


        //================================================================================
        // Holiday Request
        //================================================================================

        Dialog holidayDialog = new Dialog();
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
        DatePicker fromDate = new DatePicker("Holiday Start");
        DatePicker toDate = new DatePicker("Holiday End");
        Label holidaysSelectedLabel = new Label("Holidays Requested: " + holidaysSelected);

        holidayDialogLayout.add(holidaysRemainingLabel,fromDate,toDate,holidaysSelectedLabel);
        holidayDialog.add(holidayDialogLayout);

        // TODO:
        // Validate holidaysSelected to be LESS OR EQUAL to holidaysRemaining
        // Validate that all fields are NOT empty when submit clicked
        Button holidaySubmitButton = new Button("Submit");
        holidaySubmitButton.addClickListener(e -> {
            
        });

        Button holidayCancelButton = new Button("Cancel", ee -> holidayDialog.close());
        holidayDialog.getFooter().add(holidaySubmitButton, holidayCancelButton);
       
        holidayRequestButton.addClickListener(e -> {
            holidayDialog.open();
        });

        holidaySubmitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        //================================================================================
        // Absense Request
        //================================================================================

        VerticalLayout absenceDialogLayout = new VerticalLayout();
        Dialog absenceDialog = new Dialog();
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
        // Validation: check that all fields are not empty
        // AFTER VALIDATION submit absence request to DB
        Button absenceSubmitButton = new Button("Submit");

        absenceSubmitButton.addClickListener(e -> {
            
        });

        Button absenceCancelButton = new Button("Cancel", ee -> absenceDialog.close());
        absenceDialog.getFooter().add(absenceSubmitButton, absenceCancelButton);
       
        absenceRequestButton.addClickListener(e -> {
            absenceDialog.open();
        });

        absenceSubmitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Add cards and return 
        absenceCard.add(card2BottomHeader,absenceButtonContainer);
        card.add(workHoursCard,absenceCard);
        return(card);
    }
}
