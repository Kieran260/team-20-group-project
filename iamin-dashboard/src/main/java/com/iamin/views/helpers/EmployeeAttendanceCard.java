package com.iamin.views.helpers;
import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.CheckInOutRepository;
import com.iamin.data.service.LoginService;
import com.iamin.views.helpers.EmployeeAttendanceCard;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Component
public class EmployeeAttendanceCard {
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private CheckInOutRepository checkInOutRepository;

	public EmployeeAttendanceCard(LoginService loginService , CheckInOutRepository checkInOutRepository) {
        this.loginService = loginService;
        this.checkInOutRepository = checkInOutRepository;
    }
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
        // Query the table to see if the user is actually checked in currently or not and update statusLabel 
        
        // TODO: Determine if user is checked in and display to label
        SamplePerson person = loginService.getSamplePersonByUsername(authentication.getName());

        String nameForCheckin;
        try { Optional<String> personNameOptional = loginService.getPersonNameByUsername(authentication.getName());
        nameForCheckin = personNameOptional.get();
        } catch (EntityNotFoundException e) { 
        	nameForCheckin = authentication.getName();
        } 

        LocalDate date = LocalDate.now();
        Label statusLabel = null;
        Optional<CheckInOut> checkInTimeOptional = checkInOutRepository.findCheckInTimeByPersonAndDate(person, date);
        Optional<CheckInOut> checkOutTimeOptional = checkInOutRepository.findCheckOutTimeByPersonAndDate(person, date);
        Button checkInButton = new Button("Check In");
        Button checkOutButton = new Button("Check Out");
        if (checkOutTimeOptional.isPresent()) {
        	 statusLabel = new Label(nameForCheckin + ": You are checked out");
             statusLabel.getStyle().set("font-size", "16px");
             checkInButton.getElement().getStyle().set("opacity", "0.5");
             checkOutButton.getElement().getStyle().set("opacity", "0.5");

             
        }else { 
        	 if (checkInTimeOptional.isPresent() && !checkOutTimeOptional.isPresent()) {
                statusLabel = new Label(nameForCheckin + ": You are checked in");
                statusLabel.getStyle().set("font-size", "16px");
                 
                 checkInButton.getElement().getStyle().set("opacity", "0.5");

             	
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
                         
                         CheckInOut checkInOutObj = checkInOutRepository.findCheckInOutByPersonAndDate(person, date);

                         

                         checkInOutObj.setcheckOutTime();
                         checkInOutRepository.save(checkInOutObj);
                         
                         //TODO: Log check in to database here
                         Notification.show("Success! Checked out at " + formattedTime, 3000, Position.TOP_CENTER);
                         new Page(UI.getCurrent()).reload();
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
                 
             }else if (!checkInTimeOptional.isPresent()){
            	 checkOutButton.getElement().getStyle().set("opacity", "0.5");
             	statusLabel = new Label(nameForCheckin + ": You are currently not checked in");
                 statusLabel.getStyle().set("font-size", "16px");
                 
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
                        CheckInOut checkIn = new CheckInOut();

                      // Set the SamplePerson object
                      checkIn.setPerson(person);

                      // Set the check-in time
                      checkIn.setcheckInTime();

                      // Set the date
                      checkIn.setdate();

                      // Save the CheckInOut instance to the database
                      checkInOutRepository.save(checkIn);
                         
                         Notification.show("Success! Checked in at " + formattedTime, 3000, Position.TOP_CENTER);
                         new Page(UI.getCurrent()).reload();
                         checkOutButton.setEnabled(true);
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

                
             } 
        }
       
       
        checkInButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
      

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
        TextField holidayName = new TextField("Reason for request");
        holidayName.setAutocomplete(Autocomplete.OFF);
        DatePicker fromDate = new DatePicker("Holiday Start");
        DatePicker toDate = new DatePicker("Holiday End");
        Label holidaysSelectedLabel = new Label("Holidays Requested: " + holidaysSelected);

        holidayDialogLayout.add(holidaysRemainingLabel,holidayName,fromDate,toDate,holidaysSelectedLabel);
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
