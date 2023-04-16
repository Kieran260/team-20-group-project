package com.iamin.views.helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.textfield.PasswordField;
import com.iamin.data.entity.Login;
import com.iamin.data.service.LoginRepository;
import com.iamin.data.validation.Validation;

@Component
public class PasswordDialog {
    
    PasswordEncoder passwordEncoder;
    private LoginRepository loginRepository;
    private Validation validation;

    @Autowired
    public PasswordDialog(LoginRepository loginRepository, PasswordEncoder passwordEncoder, Validation validation) {
            
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
        this.validation = validation;
    }
    
    public void showPasswordChangeDialog() {
        Dialog passwordDialog = new Dialog();
        passwordDialog.setCloseOnOutsideClick(false);
        passwordDialog.setCloseOnEsc(false);

        Label changePasswordLabel = new Label("Please change your password:");
        PasswordField newPasswordField = new PasswordField("New Password");
        PasswordField confirmNewPasswordField = new PasswordField("Confirm new Password");
        Button changePasswordButton = new Button("Change Password");

        newPasswordField.setHelperText("Password must be at least 8 characters, at least one letter and one digit.");


        changePasswordButton.addClickListener(event -> {
            String newPassword = newPasswordField.getValue();
            String confirmNewPassword = confirmNewPasswordField.getValue();
            String passwordErrorMsg = validation.passwordValidation(newPassword , confirmNewPassword);
            if (!passwordErrorMsg.isEmpty()) {
                Notification.show(passwordErrorMsg, 3000, Position.TOP_CENTER);
                return;
            }else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            Login userLogin = loginRepository.findByUsername(currentUsername);
            userLogin.setHashedPassword(passwordEncoder.encode(newPassword));
            userLogin.setPasswordSetFlag(true);
            loginRepository.save(userLogin);
            // Save the new password and close the dialog
            passwordDialog.close();
            Notification.show("Password changed successfully", 3000, Position.TOP_CENTER);
            }
        });

        VerticalLayout dialogLayout = new VerticalLayout(changePasswordLabel, newPasswordField, confirmNewPasswordField);
        dialogLayout.setAlignItems(Alignment.CENTER);
        dialogLayout.setSpacing(true);

        FlexLayout buttonContainer = new FlexLayout(changePasswordButton);
        buttonContainer.setWidthFull();
        buttonContainer.getStyle().set("gap", "20px");
        buttonContainer.getStyle().set("padding-top", "20px");
        buttonContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        passwordDialog.add(dialogLayout, buttonContainer);

        // Open the dialog
        passwordDialog.open();
    }
    
}