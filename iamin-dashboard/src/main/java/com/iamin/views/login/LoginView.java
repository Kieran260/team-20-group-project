package com.iamin.views.login;

import com.iamin.data.entity.User;
import com.iamin.data.service.UserRepository;
import com.iamin.data.Role;
import com.iamin.security.AuthenticatedUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.notification.Notification.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;
import java.util.HashSet;

@PageTitle("Login")
@Route(value = "login")
@AnonymousAllowed

// This class displays a custom login and registration screen. A user can login using their credentials or a manager
// can sign up to use the system. The registration is for managers only therefore every registered user is given
// the ADMIN role.
//
// Sections of code which deal with authentication are labelled with "AUTHENTICATION START" and "END" comments.
public class LoginView extends VerticalLayout {
    // TODO: 
    // Add password requirements - e.g. 8+ characters, alphanumeric, etc
    // Test password requirements
    // Test layout on all browsers and devices
    // Add feedback to incorrect login details

    // Authentication Vars
    private final AuthenticatedUser authenticatedUser;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Login Vars
    private Button registerButton = new Button("Register");


    // Register Vars
    private Label registerText = new Label("Sign up to IAMIN as a manager");
    private TextField usernameField = new TextField("Username");
    private PasswordField passwordField = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm Password");
    private Button signUpButton = new Button("Sign Up");
    private Button returnButton = new Button("Return to Login");



    public LoginView(AuthenticatedUser authenticatedUser) {
        // Background Styles
        getStyle().set("background-color", "#8000ff");
        getStyle().set("width","100%");
        getStyle().set("height","100%");

        // Login Iteuses Styles
        registerButton.getStyle().set("background-color", "light-grey");
        registerButton.getStyle().set("color", "#005eec");
        registerButton.getStyle().set("width", "250px");
        registerButton.getStyle().set("align-self", "center");

        // Register Items Styles
        registerText.getStyle().set("font-weight","600");
        signUpButton.getStyle().set("background-color", "blue");
        signUpButton.getStyle().set("color", "white");
        signUpButton.getStyle().set("background-color","#005eec");
        signUpButton.getStyle().set("margin-top","20px");


        // Container Divs
        Div loginContainer = new Div(); // Login container Div
        loginContainer.getStyle().set("background-color","transparent");
       
        Div registerContainer = new Div(); // Master container Div
        registerContainer.getStyle().set("background-color","transparent");
        
        Div fieldsContainer = new Div(); // Container which contains the fields only
        fieldsContainer.getStyle().set("border", "2px solid blue");
        fieldsContainer.getStyle().set("height", "100px");


        

        // Flex Layout for login screen - this controls the layout of the items inside
        FlexLayout loginLayout = new FlexLayout();
        styleLoginLayout(loginLayout);
        



    // AUTHENTICATION START: Login Form
        this.authenticatedUser = authenticatedUser;

        LoginI18n i18n = LoginI18n.createDefault();
        LoginForm login = new LoginForm(i18n);
        login.setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));
        login.setForgotPasswordButtonVisible(false);
        
        // Add login form and buttons to login layout, then add to login container
        loginLayout.add(login,registerButton);
        loginContainer.add(loginLayout);

    // AUTHENTICATION END: Login Form
    
        // Flex Layout for register screen - this controls the layout of the items inside
        FlexLayout registerLayout = new FlexLayout();
        styleRegisterLayout(registerLayout);
        registerLayout.add(registerText,usernameField,passwordField,confirmPassword,signUpButton,returnButton);
        registerContainer.add(registerLayout);

        // Styles for card flip animation
        styleContainerAnimation(loginContainer,registerContainer);

        // Turn card to register form when registerButton clicked
        registerButton.addClickListener(e -> {
            animationToRegister(loginContainer, registerContainer);

        });

        // Turn card back to login form when returnButton clicked
        returnButton.addClickListener(e -> {
            animationToLogin(loginContainer,registerContainer);
        });
          
          
    // AUTHENTICATION START: Sign Up
        signUpButton.addClickListener(event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            String confirmedPassword = confirmPassword.getValue();
        
            // TODO: Sign Up Validation
            // Password is 8+ characters AND contains at least 1 number
            // Check that username does not already exist from user database
            // Check that the fields are NOT empty
            if (!password.equals(confirmedPassword)) {
                Notification.show("Passwords do not match", 3000, Position.TOP_CENTER);
                passwordField.setValue("");
                confirmPassword.setValue("");
                return;
            }
        
            User user = new User();
        
            Set<Role> roles = new HashSet<>();
            roles.add(Role.ADMIN);
            user.setRoles(roles);
            user.setUsername(username);
            user.setHashedPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            
            
            Notification.show("Account created successfully!", 3000, Position.TOP_CENTER);
            try {
                Thread.sleep(2000); // Sleep for 2 seconds (2000 milliseconds)
            } catch (InterruptedException e) {
                // Handle the exception
            }
            
            // Clear fields after sign up
            usernameField.setValue("");
            passwordField.setValue("");
            confirmPassword.setValue("");

            animationToLogin(loginContainer, registerContainer);
        });
        
    // AUTHENTICATION END: Sign Up

        // Add final containers
        add(loginContainer);
        add(registerContainer);
    }

    // Styles Functions
    public void styleLoginLayout(FlexLayout loginLayout) {
        loginLayout.getStyle().set("display","flex");
        loginLayout.getStyle().set("flex-direction","column");
        loginLayout.getStyle().set("justify-content","space-between");
        loginLayout.getStyle().set("align-items","center");
        loginLayout.getStyle().set("border-radius", "10px");
        loginLayout.getStyle().set("width", "300px");
        loginLayout.getStyle().set("height", "500px");
        loginLayout.getStyle().set("margin","15% auto");
        loginLayout.getStyle().set("padding","25px");
        loginLayout.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.25)");
        loginLayout.getStyle().set("background-color", "white");
    }

    public void styleRegisterLayout(FlexLayout registerLayout) {
        registerLayout.getStyle().set("display","flex");
        registerLayout.getStyle().set("flex-direction", "column");
        registerLayout.getStyle().set("transform","rotateY(180deg)");
        registerLayout.getStyle().set("-webkit-transform","rotateY(180deg)");
        registerLayout.getStyle().set("background-color","white");
        registerLayout.getStyle().set("border-radius", "10px");
        registerLayout.getStyle().set("width", "250px");
        registerLayout.getStyle().set("height", "500px");
        registerLayout.getStyle().set("margin","15% auto");
        registerLayout.getStyle().set("padding","25px 50px");
        registerLayout.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.25)");
    }

    public void styleContainerAnimation(Div loginContainer, Div registerContainer) {

        // Login Animation
        loginContainer.getStyle().set("transform-style", "preserve-3d");
        loginContainer.getStyle().set("transition", "transform 1s");
        loginContainer.getStyle().set("backface-visibility","hidden");
        loginContainer.getStyle().set("align-self","center");
        loginContainer.getStyle().set("-webkit-transform-style", "preserve-3d");
        loginContainer.getStyle().set("-webkit-transition", "transform 1s");
        loginContainer.getStyle().set("-webkit-backface-visibility", "hidden");
        loginContainer.getStyle().set("z-index","2");


        // Register Animation
        registerContainer.getStyle().set("transform-style", "preserve-3d");
        registerContainer.getStyle().set("transition", "transform 1s");
        registerContainer.getStyle().set("position","absolute");
        registerContainer.getStyle().set("align-self","center");
        registerContainer.getStyle().set("z-index","0");
        registerContainer.getStyle().set("-webkit-transform-style", "preserve-3d");
        registerContainer.getStyle().set("-webkit-transition", "transform 1s");
        registerContainer.getStyle().set("opacity","0");
        registerContainer.setId("register-container"); // Used for JavaScript access

    }

    // Function to animate the card to turn back to the login form
    public void animationToLogin(Div loginContainer, Div registerContainer) {
        loginContainer.getStyle().set("transform", "rotateY(0deg)");
        loginContainer.getStyle().set("-webkit-transform", "rotateY(0deg)");

        // Delay appearance of registerContainer by 250
        // This is used to debug animation errors in Safari
        registerContainer.getElement().executeJs(
            "setTimeout(function() {\n"
            + "var registerContainer = document.getElementById('" + registerContainer.getId().orElse("") + "');\n"
            + "registerContainer.style.opacity = '0';\n"
            + "}, 300);\n");

        registerContainer.getStyle().set("-webkit-transform", "rotateY(0)");
        registerContainer.getStyle().set("transform", "rotateY(0deg)");

        // Change background colour transition
        getStyle().set("background-color", "#8000ff");
        getStyle().set("transition", "background-color 500ms linear");
        getStyle().set("-webkit-transition", "background-color 500ms linear");
    }

    // Function to animate the card to turn to the register form
    public void animationToRegister(Div loginContainer, Div registerContainer) {
        loginContainer.getStyle().set("transform", "rotateY(180deg)");
        loginContainer.getStyle().set("-webkit-transform", "rotateY(180deg)");

        // Delay appearance of registerContainer by 250
        // This is used to debug animation errors in Safari
        registerContainer.getElement().executeJs(
            "setTimeout(function() {\n"
            + "var registerContainer = document.getElementById('" + registerContainer.getId().orElse("") + "');\n"
            + "registerContainer.style.opacity = '1';\n"
            + "}, 250);\n");

        registerContainer.getStyle().set("-webkit-transform", "rotateY(180deg)");
        registerContainer.getStyle().set("transform", "rotateY(180deg)");
        
        // Change background colour transition
        getStyle().set("background-color", "#ff4d4d");
        getStyle().set("transition", "background-color 500ms linear");
        getStyle().set("-webkit-transition", "background-color 500ms linear");
    }
}
