package com.iamin.views.helpers;

import com.iamin.data.entity.SamplePerson;

import org.springframework.beans.factory.annotation.Autowired;

import com.iamin.data.entity.Login;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Label;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.icon.Icon;

@Component
public class RequestDialog {

    @Autowired
    public void RequestFormDialog() {
    }

    public void showRequestDialog(Request req) {
        // Create a new Dialog
        Dialog dialog = new Dialog();
        dialog.setWidth("75vw");
        dialog.setMaxWidth("700px");
        dialog.setCloseOnOutsideClick(true);
        dialog.setCloseOnEsc(true);

        dialog.setHeaderTitle("Request details");

        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getHeader().add(closeButton);

        VerticalLayout verticalLayout = new VerticalLayout();

        // No manager responsible yet
        //Label authorisedByLabel = new Label("Responsible manager: " + req.getResponsibleManager());

        // Check if request is holiday or absence before populating dialog

        if (req.getType().equals("Holiday")) {
            // add holiday info
            if (req.getIsApproved().equals("Yes")) {
                Label dateAuthorisedLabel = new Label("Date authorised: " + req.getDateAuthorised());
                verticalLayout.add(dateAuthorisedLabel);
            } else {
                Label deniedReasonLabel = new Label("Denial reason: " + req.getDenialReason());
                verticalLayout.add(deniedReasonLabel);
            }

        } else {
            // add absence info
            // authorisedBy, payAuthorisation
            Label deniedReasonLabel = new Label("Denial reason: " + req.getDenialReason());
            verticalLayout.add(deniedReasonLabel);
        }
        dialog.add(verticalLayout);
        // Open the dialog
        dialog.open();
    }

}
