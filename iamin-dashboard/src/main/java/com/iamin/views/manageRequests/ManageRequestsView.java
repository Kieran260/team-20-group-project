package com.iamin.views.manageRequests;

import com.iamin.views.MainLayout;
import com.iamin.views.helpers.Styling;
import com.iamin.data.entity.Absence;
import com.iamin.data.entity.Holidays;
import com.iamin.data.service.AbsenceRepository;
import com.iamin.data.service.AbsenceService;
import com.iamin.data.service.HolidaysRepository;
import com.iamin.data.service.HolidaysService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.data.renderer.ComponentRenderer;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;

import org.springframework.beans.factory.annotation.Autowired;

@CssImport(value = "dashboard-styles.css")
@PageTitle("Manage Requests")
@Route(value = "manageRequests", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ManageRequestsView extends Div {

    String currentUserName;
    String currentUserRole;
    private final AbsenceService absenceService;
    private final HolidaysService holidayService;

    @Autowired
    public ManageRequestsView(AbsenceService absenceService, HolidaysService holidayService) {
        this.absenceService = absenceService;
        this.holidayService = holidayService;


        getStyle().set("background-color", "rgba(250, 250, 250)");

        // Master Container
        Div cardsContainer = new Div();
        cardsContainer.setClassName("requestCardContainer");
        cardsContainer.getStyle().set("height", "100%");
        cardsContainer.getStyle().set("width", "100%");

        Grid<Request> requestsTable = createRequestsTable();

        cardsContainer.add(requestsTable);

        add(cardsContainer);
    }
    public Grid<Request> createRequestsTable() {
        List<Absence> unapprovedAbsences = absenceService.findAllUnapproved();
        List<Holidays> unapprovedHolidays = holidayService.findAllUnapproved();
        List<Request> requests = new ArrayList<>();
        for (Absence absence : unapprovedAbsences) {
            requests.add(new Request(absence.getPerson().getFirstName(),
                    absence.getPerson().getLastName(),
                    absence.getStartDate(),
                    absence.getEndDate(),
                    absence.getAbsenceReason(),
                    "absence",
                    absence.getId(),
                    absence.documentsURL()));
        }
        for (Holidays holiday : unapprovedHolidays) {
            requests.add(new Request(holiday.getPerson().getFirstName(),
                    holiday.getPerson().getLastName(),
                    holiday.getStartDate(),
                    holiday.getEndDate(),
                    holiday.getHolidayReason(),
                    "holiday",
                    holiday.getId(),
                    null));
        }
    
        // Order the list by start date
        requests.sort(Comparator.comparing(Request::getStartDate));
    
        ListDataProvider<Request> dataProvider = new ListDataProvider<>(requests);
    
        // Create the table and set its data provider
        Grid<Request> requestsTable = new Grid<>();
        requestsTable.setDataProvider(dataProvider);
    
        // Styling
        requestsTable.setAllRowsVisible(true);
        requestsTable.getStyle().set("width", "100%");
        requestsTable.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    
        // Add columns to the table
        requestsTable.addColumn(Request::getFirstName).setHeader("First Name");
        requestsTable.addColumn(Request::getLastName).setHeader("Last Name");
        requestsTable.addColumn(Request::getStartDate).setHeader("Start date");
        requestsTable.addColumn(Request::getEndDate).setHeader("End date");
        requestsTable.addColumn(Request::getReason).setHeader("Reason");
        requestsTable.addColumn(Request::getRequestType).setHeader("Request Type");
        requestsTable.addColumn(new ComponentRenderer<>(request -> {
            if (request.getDocumentsURL() != null && !request.getDocumentsURL().isEmpty()) {
                Anchor anchor = new Anchor(request.getDocumentsURL(), "View Document");
                anchor.setTarget("_blank");
                return anchor;
            } else {
                return new Div(); // Return an empty Div when there is no documentsURL
            }
        })).setHeader(" ");


    
        requestsTable.addComponentColumn(request -> {
            Button approveButton = new Button("Approve");
            approveButton.addClickListener(event -> {
                if ("absence".equals(request.getRequestType())) {
                    Optional<Absence> absence = absenceService.findById(request.getRequestId());
                    if (absence.isPresent()) {
                        absence.get().setAbsenceApproval(true);
                    absenceService.createAbsenceRequest(absence.get());
                    }
                } else if ("holiday".equals(request.getRequestType())) {
                    Optional<Holidays> holiday = holidayService.findById(request.getRequestId());
                    if (holiday.isPresent()) {
                        holiday.get().setHolidaysApproval(true);
                        holidayService.createHolidayRequest(holiday.get());
                    }
                }
                dataProvider.refreshAll();
                new Page(UI.getCurrent()).reload();
            });
            return approveButton;
        }).setHeader("Approve");

        requestsTable.addComponentColumn(request -> {
            Button denyButton = new Button("Deny");
            denyButton.addClickListener(event -> {
                if ("absence".equals(request.getRequestType())) {
                    Optional<Absence> absence = absenceService.findById(request.getRequestId());
                    if (absence.isPresent()) {
                        Dialog denyReasonDialog = new Dialog();
                        TextArea denyReasonTextArea = new TextArea();
                        denyReasonTextArea.setPlaceholder("Enter deny reason...");
                        Button confirmButton = new Button("Confirm", event2 -> {
                        if (denyReasonTextArea.getValue().isEmpty()) {
                            Notification.show("Please enter a reason for denying the request");
                            return;
                        }
                        absence.get().setAbsenceApproval(false);
                        absence.get().setDenyReason(denyReasonTextArea.getValue()); 
                        absenceService.createAbsenceRequest(absence.get());
                        requestsTable.getDataProvider().refreshAll();
                        new Page(UI.getCurrent()).reload();
                        denyReasonDialog.close();
                    });
                    denyReasonDialog.add(denyReasonTextArea, confirmButton);
                    denyReasonDialog.open();
                }} else if ("holiday".equals(request.getRequestType())) {
                    Optional<Holidays> holiday = holidayService.findById(request.getRequestId());
                    if (holiday.isPresent()) {
                        Dialog denyReasonDialog = new Dialog();
                        TextArea denyReasonTextArea = new TextArea();
                        denyReasonTextArea.setPlaceholder("Enter deny reason...");
                        Button confirmButton = new Button("Confirm", event2 -> {
                            if (denyReasonTextArea.getValue().isEmpty()) {
                                Notification.show("Please enter a reason for denying the request");
                                return;
                            }
                            holiday.get().setHolidaysApproval(false);
                            holiday.get().setDenyReason(denyReasonTextArea.getValue()); 
                            holidayService.createHolidayRequest(holiday.get());
                            requestsTable.getDataProvider().refreshAll();
                            new Page(UI.getCurrent()).reload();
                            denyReasonDialog.close();
                        });
                        denyReasonDialog.add(denyReasonTextArea, confirmButton);
                        denyReasonDialog.open();
                    }
                }
            });
            return denyButton;
        }).setHeader("Deny");
    
        return requestsTable;
    }
    private static class Request {
        private final String firstName;
        private final String lastName;
        private final LocalDate startDate;
        private final LocalDate endDate; 
        private final String reason;
        private final Long requestId;
        private final String requestType;
        private final String documentsURL;
    public Request(String firstName, String lastName, LocalDate startDate,
                   LocalDate endDate, String reason, String requestType, Long requestId, String documentsURL ) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.requestId = requestId;
        this.requestType = requestType;
        this.documentsURL = documentsURL;
    }
        public String getDocumentsURL() {
            return documentsURL;
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
            
            public String getReason() {
            return reason;
            }
            public Long getRequestId() {
            return requestId;
            }
            public String getRequestType() {
            return requestType;
            }
            
    }
}
