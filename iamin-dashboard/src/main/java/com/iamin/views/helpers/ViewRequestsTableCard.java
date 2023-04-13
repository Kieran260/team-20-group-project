package com.iamin.views.helpers;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.button.Button;
import java.util.ArrayList;
import java.util.List;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.iamin.views.helpers.RequestDialog;
import com.iamin.views.helpers.Request;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.Component;

// Handle absence and holiday in separate tables?
// Pull records from DB and create Request object with important details?

public class ViewRequestsTableCard {

    public Div createCard() {
        // Dummy data
        List<Request> activeRequestsList = new ArrayList<>();
        List<Request> pastRequestsList = new ArrayList<>();
        RequestDialog reqDialog = new RequestDialog();

        for (int i = 0; i < 10; i++) {
            String approved = (i % 2 == 0) ? "Yes" : "No";
            activeRequestsList.add(new Request("John", "Doe", "12-03-23", "18-03-23", "Pending", "Pending", "Pending",
                    "Holiday", "Fishing", "Pending", "Pending"));
            pastRequestsList.add(new Request("John", "Doe", "12-03-23", "18-03-23", "poor excuse", "Mr Smith", "N/A",
                    "Holiday", "Fishing", "No", "No"));
            activeRequestsList.add(new Request("John", "Doe", "12-03-23", "18-03-23", "Pending", "Pending", "Pending",
                    "Absence", "Fishing", "Pending", "Pending"));
            pastRequestsList.add(new Request("John", "Doe", "12-03-23", "18-03-23", "N/A", "Mr Smith", "12-02-23",
                    "Absence", "Fishing", "Yes", "Yes"));

        }

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
        activeHeader.getStyle().set("margin-left", "10px");

        Label pastHeader = new Label("Past Requests");
        pastHeader.getStyle().set("font-weight", "bold");
        pastHeader.getStyle().set("font-size", "18px");
        pastHeader.getStyle().set("margin-left", "10px");

        // Create container to hold the tables
        Div container = new Div();
        // Styling
        container.getStyle().set("display", "flex");
        container.getStyle().set("flex-direction", "column");
        container.getStyle().set("justify-content", "space-between");
        container.getStyle().set("padding", "20px 10px");
        Styling.styleSquareBox(container);
        container.setWidth("100%");
        container.setHeight("50%");

        // Add columns to the tables

        activeRequests.addColumn(Request::getFirstName).setHeader("First Name");
        activeRequests.addColumn(Request::getLastName).setHeader("Last Name");
        activeRequests.addColumn(Request::getStartDate).setHeader("Start date");
        activeRequests.addColumn(Request::getEndDate).setHeader("End date");
        activeRequests.addColumn(Request::getType).setHeader("Type");
        activeRequests.addColumn(Request::getReason).setHeader("Reason");
        activeRequests.addColumn(new ComponentRenderer<>(Request::getApprovedComponent)).setHeader("Approved");

        pastRequests.addColumn(Request::getFirstName).setHeader("First Name");
        pastRequests.addColumn(Request::getLastName).setHeader("Last Name");
        pastRequests.addColumn(Request::getStartDate).setHeader("Start date");
        pastRequests.addColumn(Request::getEndDate).setHeader("End date");
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

}