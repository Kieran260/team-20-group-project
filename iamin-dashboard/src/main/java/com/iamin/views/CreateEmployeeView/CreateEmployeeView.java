package com.iamin.views.CreateEmployeeView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;


import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.iamin.data.service.SamplePersonRepository;
import com.iamin.data.Role;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.iamin.data.service.LoginRepository;
import com.iamin.data.validation.Validation;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.iamin.data.service.LoginRepository;
import com.iamin.data.service.LoginService;
import com.vaadin.flow.component.UI;

import com.iamin.data.service.LoginRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Create Account")
@Route(value = "create-account", layout= MainLayout.class)
@RolesAllowed("ADMIN")
public class CreateEmployeeView extends VerticalLayout implements BeforeEnterObserver{

    

    @Autowired
    SamplePersonRepository samplePersonRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    LoginService loginService;

    @Autowired
    PasswordEncoder passwordEncoder;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Login userLogin;
    String currentUsername = authentication.getName();
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        try{
            userLogin = loginRepository.findByUsername(currentUsername);
        }catch(Exception e){
            userLogin = null;
        }
        // Check your condition and redirect if necessary
        boolean Redirect = (userLogin != null && userLogin.getPerson() == null);
        if (Redirect) {
            UI.getCurrent().getPage().executeJs("location.href = 'dashboard'");
        }
    }


    //form fields
    private H4         titleLabel  = new H4("Create Account");
    private TextField  firstName   = new TextField();
    private TextField  lastName    = new TextField();
    private TextField  phone       = new TextField();
    private EmailField email       = new EmailField();
    private DatePicker dateOfBirth = new DatePicker();
    private TextField  holidays  = new TextField();
    private TextField  jobTitle    = new TextField();
    private TextField  address     = new TextField(); 
    private RadioButtonGroup<String> role = new RadioButtonGroup<>();
    ComboBox<String> departmentComboBox = new ComboBox<>();
    Button save = new Button("Save");

    //default values
    private Integer defaultMaxHoliday = 21;
    private String successMessage = "New account has been added successfully. Please click pending accounts to view the login credentials.";
    
    //constructor
    public CreateEmployeeView() {
         
        //set role options
        role.setItems("Employee", "Manager");
        
        //create personal info form, add relevant fields
        FormLayout personalInfoForm = new FormLayout();
        personalInfoForm.addFormItem(firstName, "First Name");
        personalInfoForm.addFormItem(lastName, "Last Name");
        personalInfoForm.addFormItem(phone, "Phone");
        personalInfoForm.addFormItem(email, "Email");
        personalInfoForm.addFormItem(address, "Address");
        personalInfoForm.addFormItem(dateOfBirth, "Date Of Birth");
        
        //create job info form, add relevant fields
        FormLayout jobInfoForm = new FormLayout();
        jobInfoForm.addFormItem(holidays, "Holidays");
        jobInfoForm.addFormItem(jobTitle, "Job Title");
        jobInfoForm.addFormItem(role, "Role");
        
        //add save button
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        save.addClickShortcut(Key.ENTER);   
        
        //combine the two forms
        FormLayout miniFormsCombined = new FormLayout(personalInfoForm, jobInfoForm);
        miniFormsCombined.setResponsiveSteps(
            // Use one column by default
            new ResponsiveStep("0", 1),
            // Use two columns, if the layout's width exceeds 400px
            new ResponsiveStep("400px", 2)
        );

        //add the save button
        VerticalLayout mainLayout = new VerticalLayout(titleLabel, miniFormsCombined);
        mainLayout.getStyle().set("width","100%");
        mainLayout.getStyle().set("max-width","1000px");
        titleLabel.getStyle().set("margin-top","0");
        titleLabel.getStyle().set("padding-top","0");

    
        
        add(mainLayout);
        //do stuff when form is submitted
        save.addClickListener(event -> {
            if(requiredFields()){
                //TODO wrap in try-catch block
                //do when integrating input validation

                SamplePerson person = new SamplePerson();
                person.setFirstName(firstName.getValue());
                person.setLastName(lastName.getValue());
                person.setEmail(email.getValue());
                person.setPhone(phone.getValue());
                person.setDateOfBirth(dateOfBirth.getValue());
                person.setAddress(address.getValue());
                person.setJobTitle(jobTitle.getValue());
                person.setMaxHolidays(defaultMaxHoliday);

                //TODO set department info -Check with Khaled
                
                //Save to the SamplePersonRepository
                SamplePerson savedPerson = samplePersonRepository.save(person);
                
                //create login credentials 
                String generatedUsername = genUserName(firstName.getValue(), lastName.getValue());
                String generatedPassword = genPassword();
                Login credentials = new Login();
                
                //set role
                Set<Role> roleChosen = new HashSet<>();
                if(role.getValue() == "Employee") roleChosen.add(Role.USER);
                if(role.getValue() == "Manager")  roleChosen.add(Role.ADMIN);
                credentials.setRoles(roleChosen);

                //set username
                credentials.setUsername(generatedUsername);
                
                //set password as default pass
                credentials.setHashedPassword(passwordEncoder.encode(generatedPassword));
                credentials.setPasswordSetFlag(false);

                //set person association 
                credentials.setPerson(savedPerson);

                //save
                loginRepository.save(credentials);

                //notify on success and show generated username
                Notification.show(successMessage).setPosition(Notification.Position.TOP_CENTER);  
            
                // Clear the form
                firstName.clear();
                lastName.clear();
                phone.clear();
                email.clear();
                email.setInvalid(false);
                address.clear();
                dateOfBirth.clear();
                holidays.clear();
                jobTitle.clear();
                role.clear();
            }
        });
        // Add a reset password button
        Button resetPasswordButton = new Button("Reset Password");
        resetPasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);

        // Add a pending accounts button
        Button pendingAccountsButton = new Button("Pending Accounts");
        pendingAccountsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        // Add the reset password button to the mainLayout
        HorizontalLayout buttonLayout = new HorizontalLayout(save, resetPasswordButton, pendingAccountsButton);
        mainLayout.add(buttonLayout);

        // Add a click listener to the reset password button
        resetPasswordButton.addClickListener(event -> {
            // Create a dialog
            Dialog resetPasswordDialog = new Dialog();

            // Add a label and a TextField for entering the username
            Label resetPasswordLabel = new Label("Enter the existing username to reset the password:");
            TextField usernameField = new TextField();
            usernameField.setPlaceholder("Username");

            // Add a "Submit" button to confirm the reset action
            Button confirmResetButton = new Button("Submit", e -> {
                String username = usernameField.getValue();
                Login login = loginRepository.findByUsername(username);

                if (login != null) {
                    String generatedPassword = genPassword();
                    login.setHashedPassword(passwordEncoder.encode(generatedPassword));
                    login.setPasswordSetFlag(false);
                    loginRepository.save(login);
                    Notification.show("Password changed successfully. Please click pending accounts to view the login credentials.").setPosition(Notification.Position.TOP_CENTER);
                } else {
                    Notification.show("Username not found.").setPosition(Notification.Position.TOP_CENTER);
                }
                resetPasswordDialog.close();
            });
            confirmResetButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            // Add a "Cancel" button to close the dialog
            Button cancelResetButton = new Button("Cancel", e -> resetPasswordDialog.close());

            // Create a layout for the buttons and add the buttons to it
            HorizontalLayout resetPasswordButtonLayout = new HorizontalLayout();
            resetPasswordButtonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
            resetPasswordButtonLayout.add(confirmResetButton, cancelResetButton);

            // Create a VerticalLayout to organize the components
            VerticalLayout resetPasswordLayout = new VerticalLayout();
            resetPasswordLayout.add(resetPasswordLabel, usernameField, resetPasswordButtonLayout);
            resetPasswordLayout.setAlignItems(Alignment.CENTER);
            resetPasswordLayout.setSpacing(true);
            resetPasswordLayout.setPadding(true);

            // Add the VerticalLayout to the dialog
            resetPasswordDialog.add(resetPasswordLayout);

            // Open the dialog
            resetPasswordDialog.open();
        });

        // Pending Accounts Button
        pendingAccountsButton.addClickListener(event -> {
            Dialog pendingAccountsDialog = new Dialog();
            pendingAccountsDialog.setMaxWidth("60%");
            pendingAccountsDialog.setMinWidth("400px");
            pendingAccountsDialog.setWidth("600px");

            Grid<Accounts> pendingAccountsGrid = new Grid<>();
            List<Accounts> pendingAccounts = getPendingAccounts();

            pendingAccountsGrid.setItems(pendingAccounts);
            Label pendingAccountsLabel = new Label("Note: each time you click 'View Password' a new password will be generated for the user.");
            pendingAccountsGrid.addColumn(Accounts::getPersonName).setHeader("Name");
            pendingAccountsGrid.addColumn(Accounts::getUsername).setHeader("Username");

            pendingAccountsGrid.addComponentColumn(account -> {
                Button viewPasswordButton = new Button("View Password");
                viewPasswordButton.addClickListener(e -> {
            
                    // Generate the password for the person in the row that has been clicked
                    String password = genPassword();
                    List<Login> currentPendingLogins = loginService.findAllPendingLogins();
                    Optional<Login> loginOpt = currentPendingLogins.stream()
                            .filter(login -> login.getUsername().equals(account.getUsername()))
                            .findFirst();
                    if (loginOpt.isPresent()) {
                        Login login = loginOpt.get();
                        login.setHashedPassword(passwordEncoder.encode(password));
                        loginRepository.save(login);
            
                        Dialog passwordDialog = new Dialog();
                        Label passwordLabel = new Label("Password: " + password);
                        Label nameLabel = new Label("Username: " + account.getUsername());
                        Button closeButton = new Button("Close", e2 -> passwordDialog.close());
            
                        VerticalLayout layout = new VerticalLayout(nameLabel, passwordLabel, closeButton);
                        layout.setAlignItems(Alignment.CENTER);
                        passwordDialog.add(layout);
                        passwordDialog.open();
                    }
                });
                return viewPasswordButton;
            }).setHeader("Password");

            pendingAccountsDialog.add(pendingAccountsGrid);

            Button closeButton = new Button("Close", e -> pendingAccountsDialog.close());
            closeButton.getElement().getStyle().set("margin-top", "20px");
            VerticalLayout pendingAccountsLayout = new VerticalLayout();
            pendingAccountsLayout.setAlignItems(Alignment.CENTER);
            pendingAccountsLayout.add(pendingAccountsLabel, closeButton);
            pendingAccountsDialog.add(pendingAccountsLayout);

            pendingAccountsDialog.open();
        });


        
    }

    private String genUserName(String fName, String lName){
        /*
         * Assumption: fName and lName are each at least
         * three characters long 
         * Username format: 
         *      three letters from lName
         *      three letters from fName
         *      two numbers  
         */
        String username = "";
        //first three chars
        username += lName.substring(0, 3).toLowerCase();
        //second three chars
        username += fName.substring(0, 3).toLowerCase();
        //last two characters 
        
        //number of users with the same first six characters who
        //are already in the system
        long n = loginRepository.countByUsernameContaining(username);
        
        //n =0 -> aa, 25 -> az, 26 -> ba, and so on 
        char firstChar = (char) ('a' + (n / 26) % 26);
        char secondChar = (char) ('a' + n % 26);
        
        username += "" + firstChar + secondChar;
        return username;
    }
    private String genPassword(){
        /*
         * Assumption: password is at least 8 characters long
         * Password format: 
         *      3 random lower case letters
         *      3 random numbers
         *      2 random special upper case characters
         */
        String password = "";
        //first three chars
        for(int i = 0; i < 3; i++){
            password += (char) ('a' + (int) (Math.random() * 26));
        }
        //second three chars
        for(int i = 0; i < 3; i++){
            password += (int) (Math.random() * 10);
        }
        //last two characters 
        for(int i = 0; i < 2; i++){
            password += (char) ('A' + (int) (Math.random() * 26));
        }
        return password;
    }

    private boolean requiredFields() {
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
    
        if (holidays.getValue().isEmpty()) {
            errorMessage += "Holidays is required.\n";
            valid = false;
        }
    
        if (jobTitle.getValue().isEmpty()) {
            errorMessage += "Job Title is required.\n";
            valid = false;
        }
        //TODO validate address -talk with Jamie
    
        if (!valid) {
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
        return valid;
    }

    public List<Accounts> getPendingAccounts() {
        List<Login> pendingLogins = loginService.findAllPendingLogins();
        if (!pendingLogins.isEmpty()) {
            return pendingLogins.stream().map(login -> {
                SamplePerson person = login.getPerson();
                String name = person.getFirstName() + " " + person.getLastName();
                String username = login.getUsername();
                //String password = genPassword();
                //Reset the password to the generated one
                /* 
                login.setHashedPassword(passwordEncoder.encode(password));
                loginRepository.save(login);
                */
                return new Accounts(name, username);
                
            }).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
    
    
    public class Accounts {
        private String personName;
        private String username;
    
        public Accounts(String personName, String username) {
            this.personName = personName;
            this.username = username;
        }
    
        public String getPersonName() {
            return personName;
        }
    
        public void setPersonName(String personName) {
            this.personName = personName;
        }
    
        public String getUsername() {
            return username;
        }
    
        public void setUsername(String username) {
            this.username = username;
        }

    }
    
}
