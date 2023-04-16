package com.iamin.views.helpers;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iamin.data.entity.Absence;
import com.iamin.data.entity.Holidays;
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
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String denialReason;
    private final LocalDateTime dateAuthorised;
    private final String type;
    private final String reason;
    private final String isApproved;
    private final String isPaid;
    private final String documentURL;

    public Request(String firstName, String lastName, LocalDate startDate,
                   LocalDate endDate, String denialReason,
                   LocalDateTime dateAuthorised, String type, String reason,
                   String isApproved, String isPaid, String documentURL) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.denialReason = denialReason;
        this.dateAuthorised = dateAuthorised;
        this.type = type;
        this.reason = reason;
        this.isApproved = isApproved;
        this.isPaid = isPaid;
        this.documentURL = documentURL;
    }


    public Request(Holidays holiday) {
        this.firstName = holiday.getPerson().getFirstName();
        this.lastName = holiday.getPerson().getLastName();
        this.startDate = holiday.getStartDate();
        this.endDate = holiday.getEndDate();
        this.denialReason = holiday.getDenyReason();
        this.dateAuthorised = holiday.getDateModified();
        this.type = "Holiday";
        this.reason = holiday.getHolidayReason();
        if (holiday.getHolidaysApproval() == null) {
            this.isApproved = "Pending";
        } else {
            this.isApproved = holiday.getHolidaysApproval() ? "Yes" : "No";
        }          
        this.isPaid = "Yes";
        this.documentURL = null;
    }


    public Request(Absence absence) {
        this.firstName = absence.getPerson().getFirstName();
        this.lastName = absence.getPerson().getLastName();
        this.startDate = absence.getStartDate();
        this.endDate = absence.getEndDate();
        this.denialReason = absence.getDenyReason();
        this.dateAuthorised = absence.getDateModified();
        this.type = "Absence";
        this.reason = absence.getAbsenceReason();
        if (absence.getAbsenceApproval() == null) {
            this.isApproved = "Pending";
        } else {
            this.isApproved = absence.getAbsenceApproval() ? "Yes" : "No";
        }        
        this.isPaid = (absence.getAbsenceApproval() == null || !absence.getAbsenceApproval()) ? "No" : "Yes";
        this.documentURL = absence.documentsURL();
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getDenialReason() {
        return denialReason;
    }

    public LocalDate getDateAuthorised() {
        return dateAuthorised.toLocalDate();
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


    public String getDocumentsURL() {
        return documentURL;
    }
}
