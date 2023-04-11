package com.iamin.views.helpers;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.List;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.button.Button;
import java.util.ArrayList;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.dataview.GridListDataView;

// Handle absence and holiday in separate tables?
// Pull records from DB and create Request object with important details?

public class ViewRequestsTableCard {

    public Div createCard() {
        // Dummy data
        List<Request> activeRequestsList = new ArrayList<>();
        List<Request> pastRequestsList = new ArrayList<>();

        Request dummyReq = new Request("John", "Doe",
                "12-03-23", "18-03-23", "Fishing", true);

        Request dummyReq2 = new Request("John", "Doe",
                "12-03-23", "18-03-23", "frolicking", false);

        for (int i = 0; i < 5; i++) {
            activeRequestsList.add(dummyReq);
            pastRequestsList.add(dummyReq);
        }
        for (int i = 0; i < 5; i++) {
            activeRequestsList.add(dummyReq2);
            pastRequestsList.add(dummyReq2);
        }

        // Create the data providers for each table
        ListDataProvider<Request> activeRequestsProvider = new ListDataProvider<>(activeRequestsList);
        ListDataProvider<Request> pastRequestsProvider = new ListDataProvider<>(pastRequestsList);

        // Create the tables and set their data providers
        Grid<Request> activeRequests = new Grid<>();
        activeRequests.setDataProvider(activeRequestsProvider);
        activeRequests.setWidth("100%");

        Grid<Request> pastRequests = new Grid<>();
        pastRequests.setDataProvider(pastRequestsProvider);
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

        ComponentRenderer<Image, ViewRequestsTableCard.Request> approvedRendererA = new ComponentRenderer<>(request -> {
            if (request.isApproved()) {
                return new Image("approved.png", "approved");
            } else {
                return new Image("rejected.png", "rejected");
            }
        });

        ComponentRenderer<Image, ViewRequestsTableCard.Request> approvedRendererP = new ComponentRenderer<>(request -> {
            if (request.isApproved()) {
                return new Image("approved.png", "approved");
            } else {
                return new Image("rejected.png", "rejected");
            }
        });

        // Add columns to the tables

        activeRequests.addColumn(Request::getFirstName).setHeader("First Name");
        activeRequests.addColumn(Request::getLastName).setHeader("Last Name");
        activeRequests.addColumn(Request::getStartDate).setHeader("Start date");
        activeRequests.addColumn(Request::getEndDate).setHeader("End date");
        activeRequests.addColumn(Request::getReason).setHeader("Reason");
        activeRequests.addColumn(approvedRendererA).setHeader("Approved");
        // need images in resources

        pastRequests.addColumn(Request::getFirstName).setHeader("First Name");
        pastRequests.addColumn(Request::getLastName).setHeader("Last Name");
        pastRequests.addColumn(Request::getStartDate).setHeader("Start date");
        pastRequests.addColumn(Request::getEndDate).setHeader("End date");
        pastRequests.addColumn(Request::getReason).setHeader("Reason");
        pastRequests.addColumn(approvedRendererP).setHeader("Approved");

        container.add(activeHeader, activeRequests, pastHeader, pastRequests);
        return container;
    }

    private static class Request {
        private final String firstName;
        private final String lastName;
        private final String startDate;
        private final String endDate;
        private final String reason;
        private final boolean approved;

        public Request(String firstName, String lastName, String startDate,
                String endDate, String reason, Boolean approved) {

            this.firstName = firstName;
            this.lastName = lastName;
            this.startDate = startDate;
            this.endDate = endDate;
            this.reason = reason;
            this.approved = approved;
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

        public String getReason() {
            return reason;
        }

        public Boolean isApproved() {
            return approved;
        }
    }

}