package com.iamin.views.helpers;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.Component;

public class Request {
    public Component getApprovedComponent() {
        Icon icon;
        if ("Yes".equals(isApproved)) {
            icon = new Icon(VaadinIcon.CHECK);
            icon.getStyle().set("color", "green");
        } else if ("No".equals(isApproved)) {
            icon = new Icon(VaadinIcon.CLOSE);
            icon.getStyle().set("color", "red");
        } else {
            icon = new Icon(VaadinIcon.QUESTION);
            icon.getStyle().set("color", "black");
        }
        return icon;
    }

    private final String firstName;
    private final String lastName;
    private final String startDate;
    private final String endDate;
    private final String denialReason;
    private final String responsibleManager;
    private final String dateAuthorised;
    private final String type;
    private final String reason;
    private final String isApproved;
    private final String isPaid;

    public Request(String firstName, String lastName, String startDate,
            String endDate, String denialReason, String responsibleManager,
            String dateAuthorised, String type, String reason,
            String isApproved, String isPaid) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.denialReason = denialReason;
        this.responsibleManager = responsibleManager;
        this.dateAuthorised = dateAuthorised;
        this.type = type;
        this.reason = reason;
        this.isApproved = isApproved;
        this.isPaid = isPaid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDenialReason() {
        return denialReason;
    }

    public String getResponsibleManager() {
        return responsibleManager;
    }

    public String getDateAuthorised() {
        return dateAuthorised;
    }

    public String getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public String getIsPaid() {
        return isPaid;
    }
}
