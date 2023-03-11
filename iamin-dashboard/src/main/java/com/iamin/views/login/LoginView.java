package com.iamin.views.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.iamin.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.PermitAll;
import java.util.Arrays;
import java.util.List;
import com.vaadin.flow.component.html.Label;
import java.util.ArrayList;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import elemental.json.JsonValue;



@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout {
    // TODO: 
    // Add more fields for sign up
    // Test animation on all browsers
    // Implement a back button to return from register screen to login container
    // Hook up with database authentication


    // Login Items
    private Label welcomeText = new Label("IAMIN Dashboard");
    private TextField username = new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private Button signInButton = new Button("Sign in");
    private Button registerButton = new Button("Register");


    // Register Items
    private Label registerText = new Label("Sign up to get started");
    private TextField email = new TextField("Email Address");
    private PasswordField newPassword = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm Password");
    private Button registerConfirmButton = new Button("Next");


    public LoginView() {

        // Background Styles
        getStyle().set("background-color", "#8000ff");
        getStyle().set("width","100%");
        getStyle().set("height","100%");

        // Login Items Styles
        registerButton.getStyle().set("background-color", "blue");
        registerButton.getStyle().set("color", "white");
        welcomeText.getStyle().set("font-size","28px");
        welcomeText.getStyle().set("font-weight","700");


        // Register Items Styles
        registerText.getStyle().set("font-weight","600");
        registerConfirmButton.getStyle().set("background-color", "blue");
        registerConfirmButton.getStyle().set("color", "white");


        // Master login container for all fields and buttons
        Div loginContainer = new Div();
     
        loginContainer.getStyle().set("background-color","transparent");

        // Master register container for all fields and buttons
        Div registerContainer = new Div();
        registerContainer.getStyle().set("background-color","transparent");




        // Container which contains the fields only
        Div fieldsContainer = new Div();
        fieldsContainer.getStyle().set("border", "2px solid blue");
        fieldsContainer.getStyle().set("height", "100px");

        // Container which contains the buttons only
        Div buttonsContainer = new Div();
        buttonsContainer.add(signInButton,registerButton);
        buttonsContainer.getStyle().set("display","flex");
        buttonsContainer.getStyle().set("flex-direction","column");

        // Flex Layout for login screen - this controls the layout of the items inside
        FlexLayout loginLayout = new FlexLayout();
        loginLayout.getStyle().set("display","flex");
        loginLayout.getStyle().set("flex-direction","column");
        loginLayout.getStyle().set("gap", "20px");
        loginLayout.getStyle().set("border-radius", "10px");
        loginLayout.getStyle().set("width", "300px");
        loginLayout.getStyle().set("height", "400px");
        loginLayout.getStyle().set("margin","15vh auto");
        loginLayout.getStyle().set("padding","20px");
        loginLayout.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.25)");
        loginLayout.getStyle().set("background-color", "white");

        loginLayout.add(welcomeText,username,password,buttonsContainer);
        loginContainer.add(loginLayout);
        
        // Flex Layout for register screen - this controls the layout of the items inside
        FlexLayout registerLayout = new FlexLayout();
        registerLayout.getStyle().set("display","flex");
        registerLayout.getStyle().set("gap", "20px");
        registerLayout.getStyle().set("flex-direction", "column");
        registerLayout.getStyle().set("transform","rotateY(180deg)");
        registerLayout.getStyle().set("-webkit-transform","rotateY(180deg)");
        registerLayout.getStyle().set("background-color","white");
        registerLayout.getStyle().set("border-radius", "10px");
        registerLayout.getStyle().set("width", "300px");
        registerLayout.getStyle().set("height", "400px");
        registerLayout.getStyle().set("margin","15vh auto");
        registerLayout.getStyle().set("padding","20px");
        registerLayout.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.25)");

        registerLayout.add(registerText,email,newPassword,confirmPassword,registerConfirmButton);
        registerContainer.add(registerLayout);

        // Animation Control

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
        
        // Add final container
        add(loginContainer);
        add(registerContainer);

    }
}
