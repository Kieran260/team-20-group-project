package com.iamin.views.CreateEmployeeView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import javax.annotation.security.RolesAllowed;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.iamin.data.service.SamplePersonRepository;
import com.iamin.data.Role;
import com.iamin.data.entity.Department;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.views.MainLayout;
import com.iamin.data.service.DepartmentRepository;
import com.iamin.data.service.LoginRepository;
import com.iamin.data.validation.Validation;


import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Add Employee")
@Route(value = "add-employee", layout= MainLayout.class)
@RolesAllowed("ADMIN")
public class CreateEmployeeView extends VerticalLayout implements BeforeEnterObserver{

    

    @Autowired
    SamplePersonRepository samplePersonRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DepartmentRepository departmentRepository;
    
    // Checks if current user has a SamplePerson entity and if not shows a sign up dialog
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
    private Label      titleLabel  = new Label("Create Employee");
    private TextField  firstName   = new TextField();
    private TextField  lastName    = new TextField();
    private TextField  phone       = new TextField();
    private EmailField email       = new EmailField();
    private DatePicker dateOfBirth = new DatePicker();
    private TextField  occupation  = new TextField();
    private TextField  jobTitle    = new TextField();
    private TextField  address     = new TextField(); 
    private RadioButtonGroup<String> role = new RadioButtonGroup<>();
    ComboBox<String> departmentComboBox = new ComboBox<>();
    Button save = new Button("Save");

    //default values
    private String defaultPassword = "123456789"; 
    private Integer defaultMaxHoliday = 20;
    private String successMessage = "New employee has been added successfuly. They can access their "+ 
                                    "account under the following username:\n";
    
    //constructor
    public CreateEmployeeView() {
        //get department options
        if(departmentRepository != null){
            List<Department> departments = departmentRepository.findAll();
            List<String> deptNames = new ArrayList<String>();
            for (Department dept : departments) {
                deptNames.add(dept.getDepartmentName());
            }
            departmentComboBox.setItems(deptNames);
        }
        
         
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
        jobInfoForm.addFormItem(occupation, "Occupation");
        jobInfoForm.addFormItem(jobTitle, "Job Title");
        jobInfoForm.addFormItem(role, "Role");
        jobInfoForm.addFormItem(departmentComboBox, "Department");
        
        //add save button
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        save.addClickShortcut(Key.ENTER);   
        
        //combine the two forms
        FormLayout miniFormsCombined = new FormLayout(personalInfoForm, jobInfoForm);
        miniFormsCombined.setResponsiveSteps(
            // Use one column by default
            new ResponsiveStep("0", 1),
            // Use two columns, if the layout's width exceeds 320px
            new ResponsiveStep("400px", 2));

        //add the save button
        VerticalLayout mainLayout = new VerticalLayout(titleLabel, miniFormsCombined, save);
        mainLayout.getStyle().set("width","100%");
        mainLayout.getStyle().set("max-width","1000px");
        titleLabel.getStyle().set("font-weight","bold");

        
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
                person.setOccupation(occupation.getValue());
                person.setJobTitle(jobTitle.getValue());
                person.setMaxHolidays(defaultMaxHoliday);

                //TODO set department info -Check with Khaled
                
                //Save to the SamplePersonRepository
                SamplePerson savedPerson = samplePersonRepository.save(person);
                
                //create login credentials 
                String generatedUsername = genUserName(firstName.getValue(), lastName.getValue());
                Login credentials = new Login();
                
                //set role
                Set<Role> roleChosen = new HashSet<>();
                if(role.getValue() == "Employee") roleChosen.add(Role.USER);
                if(role.getValue() == "Manager")  roleChosen.add(Role.ADMIN);
                credentials.setRoles(roleChosen);

                //set username
                credentials.setUsername(generatedUsername);
                
                //set password as default pass
                credentials.setHashedPassword(passwordEncoder.encode(defaultPassword));

                //set person association 
                credentials.setPerson(savedPerson);

                //save
                loginRepository.save(credentials);

                //notify on success and show generated username
                Notification.show(successMessage+generatedUsername).setPosition(Notification.Position.TOP_CENTER);  
            }
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
    
        if (occupation.getValue().isEmpty()) {
            errorMessage += "Occupation is required.\n";
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
    
}
