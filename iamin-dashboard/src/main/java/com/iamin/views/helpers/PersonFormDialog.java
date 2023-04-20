package com.iamin.views.helpers;

import com.iamin.data.entity.SamplePerson;

import org.springframework.beans.factory.annotation.Autowired;

import com.iamin.data.entity.Login;
import com.iamin.data.service.SamplePersonRepository;
import com.iamin.data.validation.Validation;
import com.iamin.data.service.LoginRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PersonFormDialog {

    private final SamplePersonRepository samplePersonRepository;
    private final LoginRepository loginRepository;

    @Autowired
    public PersonFormDialog(SamplePersonRepository samplePersonRepository, LoginRepository loginRepository) {
        this.samplePersonRepository = samplePersonRepository;
        this.loginRepository = loginRepository;
    }


    public void showPersonFormDialog() {
        // Create a new Dialog
        Dialog dialog = new Dialog();
        dialog.setWidth("75vw");
        dialog.setMaxWidth("700px");
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);


        // Create a FormLayout to hold the input fields
        FormLayout formLayout = new FormLayout();

        formLayout.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("24em", 2)
        );
    
        // Create text fields for first name, last name, email, phone number, address, occupation, job title and department
        Label welcomeLabel = new Label("Welcome to IAMIN Manager. Please enter your personal details to finish signing up.");
        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        TextField email = new TextField("Email");
        TextField phone = new TextField("Phone Number");
        TextField address = new TextField("Address");
        TextField jobTitle = new TextField("Job Title");
        TextField maxHolidays = new TextField("Holiday Allowance");

        // Create a date picker for date of birth
        DatePicker dateOfBirth = new DatePicker("Date of Birth");
    
        // Add the input fields to the form layout
        formLayout.add(firstName, lastName, email, phone, dateOfBirth, address, jobTitle, maxHolidays);
    
        // Create Save button
        Button saveButton = new Button("Save Details");
    
        // Create a FlexLayout container to hold the button
        FlexLayout buttonContainer = new FlexLayout(saveButton);
        buttonContainer.setWidthFull();
        buttonContainer.getStyle().set("gap","20px");
        buttonContainer.getStyle().set("padding-top","20px");

        buttonContainer.setJustifyContentMode(JustifyContentMode.CENTER);


        // Add the label, form layout and buttons to the dialog
        dialog.add(welcomeLabel, formLayout, buttonContainer);

        // Click listener to the save button
        saveButton.addClickListener(event -> {
            // TODO: Validation for text fields PLEASE README:
            // Validate that email is valid, phone number is valid
            // Validate address is valid
            // Validate that all inputs are the same type that the data entity is expecting e.g. LocalDate or String
            // DO NOT validate occupation, department, job title as we will add these as a drop-down menu in later increment
            
            boolean valid = true;
            String errorMessage = "";
        
            if (firstName.getValue().isEmpty()) {
                errorMessage += "First Name is required.\n";
                valid = false;
            }else if(Validation.isSqlInjection(firstName.getValue())){
                errorMessage += "First Name must not contain SQL injections.\n";
                valid = false;
            }else if(!Validation.isAlpha(firstName.getValue())){
                errorMessage += "First Name must contain alphabetic characters only.\n";
                valid = false;
            }
        
            if (lastName.getValue().isEmpty()) {
                errorMessage += "Last Name is required.\n";
                valid = false;
            }else if(Validation.isSqlInjection(lastName.getValue())){
                errorMessage += "Last Name must not contain SQL injections.\n";
                valid = false;
            }else if(!Validation.isAlpha(lastName.getValue())){
                errorMessage += "Last Name must contain alphabetic characters only.\n";
                valid = false;
            }
        
            if (phone.getValue().isEmpty()) {
                errorMessage += "Phone is required.\n";
                valid = false;
            } else if (!Validation.phoneValidation(phone.getValue())) {
                errorMessage += "Invalid phone format.\n";
                valid = false;
            }
        
            if (dateOfBirth.getValue() == null) {
                errorMessage += "Date Of Birth is required.\n";
                valid = false;
            }else if(!Validation.isAtLeast18YearsAgo(dateOfBirth.getValue()) || Validation.isAfterCurrentDate(dateOfBirth.getValue())){
                errorMessage += "Employee must be at least 18 years old.\n";
            }
        
           
        
            if (jobTitle.getValue().isEmpty()) {
                errorMessage += "Job Title is required.\n";
                valid = false;
            }
            //TODO validate address -talk with Jamie

            // Invalid return from validation class changes valid = false

            if (valid) {
                SamplePerson person = samplePersonRepository.findById(1L).orElse(null);
                if (person != null) {
                    person.setFirstName(firstName.getValue());
                    person.setLastName(lastName.getValue());
                    person.setEmail(email.getValue());
                    person.setPhone(phone.getValue());
                    person.setDateOfBirth(dateOfBirth.getValue());
                    person.setAddress(address.getValue());
                    person.setJobTitle(jobTitle.getValue());
                    person.setMaxHolidays(Integer.parseInt(maxHolidays.getValue()));
                }
                
                // Save the SamplePerson to the repository
                SamplePerson savedPerson = samplePersonRepository.save(person);

                // Get the current logged-in user's Login entity
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String currentUsername = authentication.getName();
                Login userLogin = loginRepository.findByUsername(currentUsername);

                if (userLogin != null) {
                    // Set the SamplePerson for the Login entity and save it to the repository
                    userLogin.setPerson(savedPerson);
                    loginRepository.save(userLogin);
                    new Page(UI.getCurrent()).reload();
                } else {
                    // Handle the case when the userLogin is not found (e.g., show an error message)
                    
                }

                System.out.println("DEBUG INFORMATION FOR CURRENT USER LOGIN");
                System.out.println("PERSON: " + userLogin.getPerson());
                System.out.println("USERNAME: " + userLogin.getUsername());
                System.out.println("First name of CURRENT USER LOGGED IN: " + userLogin.getPerson().getFirstName());
                Notification.show("Data saved");
                dialog.close();
            }else{
                // Display pop-up error message to user
                Notification notification = new Notification();
                Div messageDiv = new Div();
                messageDiv.getStyle().set("white-space", "pre-wrap");
                messageDiv.setText(errorMessage);
                notification.add(messageDiv);
                notification.setPosition(Notification.Position.TOP_CENTER);
                notification.setDuration(3000);
                notification.open();
            }
            
        });
    
        // Open the dialog
        dialog.open();
        
    }
    
}
