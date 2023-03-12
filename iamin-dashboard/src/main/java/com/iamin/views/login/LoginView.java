package com.iamin.views.login;

import com.iamin.data.entity.User;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.iamin.data.service.UserRepository;
import com.iamin.security.AuthenticatedUser;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;
import com.iamin.data.Role;
import java.util.HashSet;



@PageTitle("Login")
@Route(value = "login")
@AnonymousAllowed



public class LoginView extends VerticalLayout {
    // TODO: 
    // Add all fields for sign up
    // Test animation on all browsers


    // Authentication Vars
    private final AuthenticatedUser authenticatedUser;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
   



    // Login Vars
    private TextField username = new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private Button signInButton = new Button("Sign in");
    private Button registerButton = new Button("Register");


    // Register Vars
    private Label registerText = new Label("Sign up to IAMIN as a manager");
    private TextField emailField = new TextField("Email Address");
    private PasswordField passwordField = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm Password");
    private Button registerConfirmButton = new Button("Sign Up");
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
        registerConfirmButton.getStyle().set("background-color", "blue");
        registerConfirmButton.getStyle().set("color", "white");
        registerConfirmButton.getStyle().set("background-color","#005eec");

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
        



        // Authentication

        // Login Form
        this.authenticatedUser = authenticatedUser;
        var login = new LoginForm();
        login.setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));
        login.setForgotPasswordButtonVisible(true);
        
        // Add login form and buttons to login layout, then add to login container
        loginLayout.add(login,registerButton);
        loginContainer.add(loginLayout);

        



        // Flex Layout for register screen - this controls the layout of the items inside
        FlexLayout registerLayout = new FlexLayout();
        styleRegisterLayout(registerLayout);

        registerLayout.add(registerText,emailField,passwordField,confirmPassword,registerConfirmButton,returnButton);
        registerContainer.add(registerLayout);

        // Styles for card flip animation
        styleContainerAnimation(loginContainer,registerContainer);

    
        // Add click listener to register button

        registerButton.addClickListener(e -> {
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

            getStyle().set("background-color", "#ff4d4d");
            getStyle().set("transition", "background-color 500ms linear");
            getStyle().set("-webkit-transition", "background-color 500ms linear");
        });

        returnButton.addClickListener(e -> {
            animationToLogin(loginContainer,registerContainer);
        });
          
          
        



        // AUTHENTICATION: Sign Up

        registerConfirmButton.addClickListener(event -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();
            String confirmedPassword = confirmPassword.getValue();
        
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

            user.setUsername(email);
            user.setHashedPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            
            
            Notification.show("Account created successfully!", 3000, Position.TOP_CENTER);
            try {
                Thread.sleep(3000); // Sleep for 2 seconds (2000 milliseconds)
            } catch (InterruptedException e) {
                // Handle the exception
            }
            
            // Clear fields after sign up
            emailField.setValue("");
            passwordField.setValue("");
            confirmPassword.setValue("");

            animationToLogin(loginContainer, registerContainer);
        });
        
    

        // Add final container
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
        loginLayout.getStyle().set("height", "450px");
        loginLayout.getStyle().set("margin","10vh auto");
        loginLayout.getStyle().set("padding","25px");
        loginLayout.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.25)");
        loginLayout.getStyle().set("background-color", "white");
    }

    public void styleRegisterLayout(FlexLayout registerLayout) {
        registerLayout.getStyle().set("display","flex");
        registerLayout.getStyle().set("gap", "20px");
        registerLayout.getStyle().set("flex-direction", "column");
        registerLayout.getStyle().set("transform","rotateY(180deg)");
        registerLayout.getStyle().set("-webkit-transform","rotateY(180deg)");
        registerLayout.getStyle().set("background-color","white");
        registerLayout.getStyle().set("border-radius", "10px");
        registerLayout.getStyle().set("width", "250px");
        registerLayout.getStyle().set("height", "450px");
        registerLayout.getStyle().set("margin","10vh auto");
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

        getStyle().set("background-color", "#8000ff");
        getStyle().set("transition", "background-color 500ms linear");
        getStyle().set("-webkit-transition", "background-color 500ms linear");
    }



    

    
}
