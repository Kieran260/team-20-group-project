package com.iamin.views.CreateEmployeeView;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H1;
import com.iamin.data.service.SamplePersonRepository;
import com.iamin.data.Role;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.iamin.data.service.LoginRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.HasValue;

@PageTitle("Add Employee")
@Route(value = "AddUsers", layout= MainLayout.class)
@RolesAllowed("ADMIN")

public class CreateEmployeeView extends FormLayout{
    @Autowired
    SamplePersonRepository samplePersonRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    
    //form fields
    private TextField  firstName   = new TextField();
    private TextField  lastName    = new TextField();
    private TextField  phone       = new TextField();
    private EmailField email       = new EmailField();
    private DatePicker dateOfBirth = new DatePicker();
    private TextField  occupation  = new TextField();
    private TextField  jobTitle    = new TextField();
    private TextField  department  = new TextField();
    private TextField  address     = new TextField(); 
    Button save = new Button("Save");
    private RadioButtonGroup<String> role = new RadioButtonGroup<>();
    
    
    //default values
    private String defaultPassword = "1234"; 
    private Integer defaultMaxHoliday = 20;
    private String successMessage = "New employee has been added successfuly. They can access their "+ 
                                    "account under the following username:\n";
    
    //constructor
    public CreateEmployeeView() {
        //set role options
        role.setItems("Employee", "Manager");
        
        //add fields to form
        addFormItem(firstName, "First Name");
        addFormItem(lastName, "Last Name");
        addFormItem(phone, "Phone");
        addFormItem(email, "Email");
        addFormItem(dateOfBirth, "Date Of Birth");
        addFormItem(occupation, "Occupation");
        addFormItem(jobTitle, "Job Title");
        addFormItem(department, "Department");
        addFormItem(role, "Role");
        addFormItem(address, "Address");
        
        //add save button
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        save.addClickShortcut(Key.ENTER);
        add(save);
        
        //do things when form is submitted
        save.addClickListener(event -> {
            if(requiredFields()){
                //TODO wrap in try-catch block
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
                //TODO setDepartment
                
                //Save to the SamplePersonRepository
                SamplePerson savedPerson = samplePersonRepository.save(person);
                
                //create login credentials 
                String generatedUsername = genUserName(firstName.getValue(), lastName.getValue());
                Login credentials = new Login();
                

                Set<Role> roleChosen = new HashSet<>();
                if(role.getValue() == "Employee") roleChosen.add(Role.USER);
                if(role.getValue() == "Manager")  roleChosen.add(Role.ADMIN);
                credentials.setRoles(roleChosen);

                credentials.setUsername(generatedUsername);

                credentials.setHashedPassword(passwordEncoder.encode(defaultPassword));

                credentials.setPerson(savedPerson);
                loginRepository.save(credentials);

                //notify on success and show generated username
                Notification.show(successMessage+generatedUsername).setPosition(Notification.Position.MIDDLE);  
            }
        });
        
    }
    //TODO
    private String genUserName(String fName, String lName){
        /*
         * Username format: 
         *      three letters from fName
         *      three letters from lName
         *      two numbers  
         */
        return "";
    }

    private boolean requiredFields() {
        boolean valid = true;
        String errorMessage = "";
    
        if (firstName.getValue().isEmpty()) {
            errorMessage += "First Name is required.\n";
            valid = false;
        }
    
        if (lastName.getValue().isEmpty()) {
            errorMessage += "Last Name is required.\n";
            valid = false;
        }
    
        if (phone.getValue().isEmpty()) {
            errorMessage += "Phone is required.\n";
            valid = false;
        } //TO DO: integrate with validation class 
        /*else if () {
            errorMessage += ".\n";
            valid = false;
        }*/
    
        if (dateOfBirth.getValue() == null) {
            errorMessage += "Date Of Birth is required.\n";
            valid = false;
        }
    
        if (occupation.getValue().isEmpty()) {
            errorMessage += "Occupation is required.\n";
            valid = false;
        }
    
        if (jobTitle.getValue().isEmpty()) {
            errorMessage += "Job Title is required.\n";
            valid = false;
        }
    
        if (department.getValue().isEmpty()) {
            errorMessage += "Department is required.\n";
            valid = false;
        }
    
        if (!valid) {
            // Display pop-up error message to user
            Notification.show(errorMessage).setPosition(Notification.Position.MIDDLE);
        }
        return valid;
    }
    
}
