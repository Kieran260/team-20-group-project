package com.iamin.views.helpers;
import com.google.firebase.cloud.StorageClient;
import com.iamin.data.entity.Absence;
import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.Holidays;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.AbsenceService;
import com.iamin.data.service.CheckInOutRepository;
import com.iamin.data.service.HolidaysRepository;
import com.iamin.data.service.HolidaysService;
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
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityNotFoundException;

@Component
public class EmployeeAttendanceCard {

	@Autowired
	private LoginService loginService;
	@Autowired
	private CheckInOutRepository checkInOutRepository;
    @Autowired
    private HolidaysService holidaysService;
    @Autowired
    private AbsenceService absenceService;

    Integer holidaysSelected = 0;

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

        // retrive the person info 
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
        // if user checked out 
        if (checkOutTimeOptional.isPresent()) {
        	 statusLabel = new Label(nameForCheckin + ": You are checked out");
             statusLabel.getStyle().set("font-size", "16px");
             // grey the buttons 
             checkInButton.getElement().getStyle().set("opacity", "0.5");
             checkOutButton.getElement().getStyle().set("opacity", "0.5");

             
        }else { 
        	// if user checked in but did not checked out 
        	 if (checkInTimeOptional.isPresent() && !checkOutTimeOptional.isPresent()) {
                statusLabel = new Label(nameForCheckin + ": You are checked in");
                statusLabel.getStyle().set("font-size", "16px");
                 
                 checkInButton.getElement().getStyle().set("opacity", "0.5");

             	// if user click on check out 
                 checkOutButton.addClickListener(e -> {
                     Dialog confirmDialog = new Dialog();
                     confirmDialog.setCloseOnEsc(false);
                     confirmDialog.setCloseOnOutsideClick(false);

                     VerticalLayout confirmContent = new VerticalLayout();
                     Label confirmMessage = new Label("Are you sure you want to check out?");
                     confirmContent.add(confirmMessage);

                     Button confirmButton = new Button("Yes", event -> {
                         confirmDialog.close();

                         LocalDateTime checkOutTime = LocalDateTime.now();
                         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                         String formattedTime = checkOutTime.format(formatter);
                         // retrive the row for check in to add the ckeck out 
                         CheckInOut checkInOutObj = checkInOutRepository.findCheckInOutByPersonAndDate(person, date);

                         

                         checkInOutObj.setcheckOutTime();
                         checkInOutRepository.save(checkInOutObj);
                         
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
             // if user did not checked in 
             }else if (!checkInTimeOptional.isPresent()){
            	 checkOutButton.getElement().getStyle().set("opacity", "0.5");
             	statusLabel = new Label(nameForCheckin + ": You are not checked in");
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

                         LocalDateTime checkOutTime = LocalDateTime.now();
                         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                         String formattedTime = checkOutTime.format(formatter);

                        CheckInOut checkIn = new CheckInOut();

                      checkIn.setPerson(person);

                      checkIn.setcheckInTime();

                      checkIn.setdate();

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

        holidayRequestButton.addClickListener(e -> {
            holidayDialog.open();
        });


        holidayDialog.setHeaderTitle("Holiday Request");
        int holidaysRemaining;

        if (person != null) {
            holidaysRemaining = holidaysService.getRemainingHolidays(person);
        } else {
            holidaysRemaining = 0;
        }
        Label holidaysRemainingLabel = new Label("You have " + holidaysRemaining + " holidays remaining");
        TextField holidayReason = new TextField("Reason for request");
        holidayReason.setAutocomplete(Autocomplete.OFF);
        DatePicker fromDate = new DatePicker("Holiday Start");
        DatePicker toDate = new DatePicker("Holiday End");
        holidayReason.setRequired(true);
        fromDate.setRequired(true);
        toDate.setRequired(true);
        Label holidaysSelectedLabel = new Label("Holidays Requested: " + holidaysSelected);
        // change the holidays selected label when the date is changed 
        fromDate.addValueChangeListener(x -> updateHolidaysSelectedLabel(fromDate, toDate, holidaysSelectedLabel, holidaysService));
        toDate.addValueChangeListener(x -> updateHolidaysSelectedLabel(fromDate, toDate, holidaysSelectedLabel, holidaysService));

        holidayDialogLayout.add(holidaysRemainingLabel,holidayReason,fromDate,toDate,holidaysSelectedLabel);
        holidayDialog.add(holidayDialogLayout);

        Button holidaySubmitButton = new Button("Submit");
        
        holidaySubmitButton.addClickListener(x -> {
            // check if the user has enough holidays remaining and if all fields are filled in
            if (holidaysSelected <= holidaysRemaining && !holidayReason.isEmpty() && !fromDate.isEmpty() && !toDate.isEmpty() && toDate.getValue().isAfter(fromDate.getValue())) {
                Holidays holiday = new Holidays();
                holiday.setPerson(person);
                holiday.setHolidayReason(holidayReason.getValue());
                holiday.setStartDate(fromDate.getValue());
                holiday.setEndDate(toDate.getValue());
                holiday.setTotalDays(holidaysSelected);
                holiday.setDateModified();
                holidaysService.createHolidayRequest(holiday);
                holidayDialog.close();

                holidaysSelected = 0;
                Notification.show("Success! Holiday request submitted", 3000, Position.TOP_CENTER);
                new Page(UI.getCurrent()).reload();
                // if the user has not filled in all fields
            } else if (holidayReason.isEmpty() || fromDate.isEmpty() || toDate.isEmpty()) {
                Notification.show("Error! Please fill in all fields", 3000, Position.TOP_CENTER);
                // if the user has selected an invalid date range
            } else if (!toDate.getValue().isAfter(fromDate.getValue())) {
                Notification.show("Error! Please select a valid date range", 3000, Position.TOP_CENTER);
            } 
            // if the user does not has enough holidays remaining
            else {
                Notification.show("Error! You do not have enough holidays remaining", 3000, Position.TOP_CENTER);
            }
        });

        Button holidayCancelButton = new Button("Cancel", ee -> holidayDialog.close());
        holidayDialog.getFooter().add(holidaySubmitButton, holidayCancelButton);
       
           
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
        absenceName.setRequired(true);
        absenceFromDate.setRequired(true);
        absenceToDate.setRequired(true);
        final String[] fileUrl = {""};
        MemoryBuffer buffer = new MemoryBuffer();
        Upload fileUpload = new Upload(buffer);
        fileUpload.addSucceededListener(event -> {
            try {
                InputStream inputStream = buffer.getInputStream();
                String uuid = UUID.randomUUID().toString();
                String fileExtension = event.getFileName().substring(event.getFileName().lastIndexOf('.'));
                String firebaseStorageFileName = "absence_requests/" + uuid + fileExtension;
        
                // Upload the file to Firebase Storage
                StorageClient.getInstance().bucket().create(firebaseStorageFileName, inputStream, event.getMIMEType());
        
                // Store the file URL in the Absence object
                String filerl = StorageClient.getInstance().bucket().get(firebaseStorageFileName).signUrl(30, TimeUnit.MINUTES).toString();
                fileUrl[0] = filerl;
                Notification.show("File uploaded successfully", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (Exception e) {
                Notification.show("Error uploading file to Firebase Storage", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
                e.printStackTrace();
                
            }
        });


        fileUpload.setAcceptedFileTypes("application/pdf");
        fileUpload.setMaxFiles(1);
        fileUpload.setMaxFileSize(10485760); // 10MB
        fileUpload.setUploadButton(new Button("Upload PDF"));
        fileUpload.setDropAllowed(true);
        fileUpload.setVisible(true);
        Label fileUploadlLabel = new Label("Upload a PDF file of your absence request");
        fileUploadlLabel.getStyle().set("font-size", "12px");
        fileUploadlLabel.getStyle().set("color", "grey");
        absenceDialogLayout.add(absenceName,absenceFromDate,absenceToDate, fileUpload , fileUploadlLabel);
        absenceDialog.add(absenceDialogLayout);
      
        // TODO: 
        // Validation: check that all fields are not empty
        // AFTER VALIDATION submit absence request to DB
        Button absenceSubmitButton = new Button("Submit");

        absenceSubmitButton.addClickListener(e -> {
            // check if all fields are filled in
            if (!absenceName.isEmpty() && !absenceFromDate.isEmpty() && !absenceToDate.isEmpty() && absenceToDate.getValue().isAfter(absenceFromDate.getValue())) {
                Absence absence = new Absence();
                absence.setPerson(person);
                absence.setAbsenceReason(absenceName.getValue());
                absence.setStartDate(absenceFromDate.getValue());
                absence.setEndDate(absenceToDate.getValue());
                absence.setDocumentsURL(fileUrl[0]);
                absence.setDateModified();
                absenceService.createAbsenceRequest(absence);
                absenceDialog.close();
                Notification.show("Success! Absence request submitted", 3000, Position.TOP_CENTER);
            } 
            else if (!absenceToDate.getValue().isAfter(absenceFromDate.getValue())) {
                Notification.show("Error! Please select a valid date range", 3000, Position.TOP_CENTER);
            }
            else {
                Notification.show("Error! Please fill in all fields", 3000, Position.TOP_CENTER);
            }
            
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
    private void updateHolidaysSelectedLabel(DatePicker fromDate, DatePicker toDate, Label holidaysSelectedLabel, HolidaysService holidaysService) {
        if (fromDate.getValue() != null && toDate.getValue() != null) {
            holidaysSelected = holidaysService.calculateTotalDaysOff(fromDate.getValue(), toDate.getValue());
            holidaysSelectedLabel.setText("Holidays Requested: " + holidaysSelected);
        } else {
            holidaysSelectedLabel.setText("Holidays Requested: 0");
        }
    }
}
