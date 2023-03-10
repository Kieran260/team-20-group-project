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



@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout {

    private Label welcomeText = new Label("IAMIN Dashboard");
    private TextField username = new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private Button signInButton = new Button("Sign in");
    private Button registerButton = new Button("Register");



    public LoginView() {
        // Items Styles
        registerButton.getStyle().set("background-color", "blue");
        registerButton.getStyle().set("color", "white");
        welcomeText.getStyle().set("font-size","28px");
        welcomeText.getStyle().set("font-weight","700");


        // Master container for all fields and buttons
        Div loginContainer = new Div();
        loginContainer.getStyle().set("border", "2px solid red");
        loginContainer.getStyle().set("width", "300px");
        loginContainer.getStyle().set("height", "400px");
        loginContainer.getStyle().set("margin","10vh auto");


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


        loginLayout.add(welcomeText,username,password,buttonsContainer);
        loginContainer.add(loginLayout);
        add(loginContainer);

    }
}
