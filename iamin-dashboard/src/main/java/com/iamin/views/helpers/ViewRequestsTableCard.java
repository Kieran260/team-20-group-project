package com.iamin.views.helpers;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.button.Button;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.iamin.views.helpers.RequestDialog;
import com.iamin.data.entity.Absence;
import com.iamin.data.entity.Holidays;
import com.iamin.data.service.AbsenceService;
import com.iamin.data.service.HolidaysService;
import com.iamin.views.helpers.Request;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.Component;

// Handle absence and holiday in separate tables?
// Pull records from DB and create Request object with important details?
public class ViewRequestsTableCard {

    private HolidaysService holidaysService;
    
    private AbsenceService absenceService;



    

    public Div createCard() {
        // Dummy data
        List<Request> activeRequestsList = new ArrayList<>();
        List<Request> pastRequestsList = new ArrayList<>();
        RequestDialog reqDialog = new RequestDialog();

        populateRequestLists(activeRequestsList, pastRequestsList);

        ListDataProvider<Request> activeDataProvider = new ListDataProvider<>(activeRequestsList);
        ListDataProvider<Request> pastDataProvider = new ListDataProvider<>(pastRequestsList);

        // Create the tables and set their data providers
        Grid<Request> activeRequests = new Grid<>();
        activeRequests.setDataProvider(activeDataProvider);
        activeRequests.setWidth("100%");

        Grid<Request> pastRequests = new Grid<>();
        pastRequests.setDataProvider(pastDataProvider);
        pastRequests.setWidth("100%");

        // Create table headers
        Label activeHeader = new Label("Active Requests");
        activeHeader.getStyle().set("font-weight", "bold");
        activeHeader.getStyle().set("font-size", "18px");
        activeHeader.getStyle().set("margin-bottom", "10px");
        activeHeader.getStyle().set("margin-top", "10px");
        activeHeader.getStyle().set("margin-left", "10px");


        Label pastHeader = new Label("Past Requests");
        pastHeader.getStyle().set("font-weight", "bold");
        pastHeader.getStyle().set("font-size", "18px");
        pastHeader.getStyle().set("margin-bottom", "10px");
        pastHeader.getStyle().set("margin-top", "10px");
        pastHeader.getStyle().set("margin-left", "10px");


        // Create container to hold the tables
        Div container = new Div();
        
        // Styling
        container.getStyle().set("display", "flex");
        container.getStyle().set("flex-direction", "column");
        container.getStyle().set("justify-content", "space-between");
        container.getStyle().set("gap", "10px");

        container.setWidth("100%");
        container.setHeight("50%");

        // Add columns to the tables
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");

        activeRequests.addColumn(Request::getFirstName).setHeader("First Name");
        activeRequests.addColumn(Request::getLastName).setHeader("Last Name");
        activeRequests.addColumn(req -> req.getStartDate().format(dateFormatter)).setHeader("Start date");
        activeRequests.addColumn(req -> req.getEndDate().format(dateFormatter)).setHeader("End date");
        activeRequests.addColumn(Request::getType).setHeader("Type");
        activeRequests.addColumn(Request::getReason).setHeader("Reason");
        activeRequests.addColumn(new ComponentRenderer<>(Request::getApprovedComponent)).setHeader("Approved");

        pastRequests.addColumn(Request::getFirstName).setHeader("First Name");
        pastRequests.addColumn(Request::getLastName).setHeader("Last Name");
        pastRequests.addColumn(req -> req.getStartDate().format(dateFormatter)).setHeader("Start date");
        pastRequests.addColumn(req -> req.getEndDate().format(dateFormatter)).setHeader("End date");
        pastRequests.addColumn(Request::getType).setHeader("Type");
        pastRequests.addColumn(Request::getReason).setHeader("Reason");
        pastRequests.addColumn(new ComponentRenderer<>(Request::getApprovedComponent)).setHeader("Approved");

        // when a row is selected show dialog
        activeRequests.asSingleSelect().addValueChangeListener(event -> {
            Request selectedRequest = event.getValue();
            if (selectedRequest != null) {
                reqDialog.showRequestDialog(selectedRequest);
            }
        });

        pastRequests.asSingleSelect().addValueChangeListener(event -> {
            Request selectedRequest = event.getValue();
            if (selectedRequest != null) {
                reqDialog.showRequestDialog(selectedRequest);
            }
        });

        container.add(activeHeader, activeRequests, pastHeader, pastRequests);
        return container;
    }

    private void populateRequestLists(List<Request> activeRequestsList, List<Request> pastRequestsList) {
        List<Holidays> allHolidays = holidaysService.getAllHolidays();
        List<Absence> allAbsences = absenceService.getAllAbsences();
    
        List<Request> allRequests = new ArrayList<>();
    
        allRequests.addAll(allHolidays.stream().map(holiday -> new Request(holiday)).collect(Collectors.toList()));
        allRequests.addAll(allAbsences.stream().map(absence -> new Request(absence)).collect(Collectors.toList()));
    
        for (Request request : allRequests) {
            if (request.getIsApproved().equals("Pending")) {
                activeRequestsList.add(request);
            } else {
                pastRequestsList.add(request);
            }
        }
    }
    
    

}