package com.iamin.views.helpers;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.List;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.button.Button;
import java.util.ArrayList;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;

// Handle absence and holiday in separate tables?
// Pull records from DB and create Request object with important details?

public class ViewRequestsTableCard {

    public Div createCard(Div card) {

        // TODO: Populate "requests" with absences/holidays
        // Order table by earliest start date

        // Dummy data
        List<Request> requests = new ArrayList<Request>();
        Request dummyReq = new Request("John", "Doe",
                "12-03-23", "18-03-23", "Fishing", "Yes");

        for (int i = 0; i < 10; i++) {
            requests.add(dummyReq);
        }

        ListDataProvider<Request> dataProvider = new ListDataProvider<>(requests);

        // Create the table and set its data provider
        Grid<Request> requestsTable = new Grid<>();
        requestsTable.setDataProvider(dataProvider);

        // Create table header
        Label card1Header = new Label("My Requests");
        card1Header.getStyle().set("font-weight", "bold");
        card1Header.getStyle().set("font-size", "18px");
        card1Header.getStyle().set("margin-left", "10px");

        // Styling
        card.getStyle().set("display", "flex");
        card.getStyle().set("flex-direction", "column");
        card.getStyle().set("justify-content", "space-between");
        card.getStyle().set("padding", "20px 10px");
        Styling.styleSquareBox(card);
        requestsTable.setAllRowsVisible(true);
        requestsTable.getStyle().set("width", "100%");
        requestsTable.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // Add columns to the table
        requestsTable.addColumn(Request::getFirstName).setHeader("First Name");
        requestsTable.addColumn(Request::getLastName).setHeader("Last Name");
        requestsTable.addColumn(Request::getStartDate).setHeader("Start date");
        requestsTable.addColumn(Request::getEndDate).setHeader("End date");
        requestsTable.addColumn(Request::getReason).setHeader("Reason");
        requestsTable.addColumn(Request::getApproved).setHeader("Approved");

        card.add(card1Header, requestsTable);
        return card;
    }

    private static class Request {
        private final String firstName;
        private final String lastName;
        private final String startDate;
        private final String endDate;
        private final String reason;
        private final String approved;

        public Request(String firstName, String lastName, String startDate,
                String endDate, String reason, String approved) {

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

        public String getApproved() {
            return approved;
        }
    }

}