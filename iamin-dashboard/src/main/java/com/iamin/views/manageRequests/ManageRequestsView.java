package com.iamin.views.manageRequests;
import com.google.cloud.storage.Blob;
import com.iamin.views.MainLayout;
import com.iamin.views.helpers.Styling;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.cloud.StorageClient;
import com.iamin.data.entity.Absence;
import com.iamin.data.entity.Holidays;
import com.iamin.data.entity.Login;
import com.iamin.data.service.AbsenceRepository;
import com.iamin.data.service.AbsenceService;
import com.iamin.data.service.HolidaysRepository;
import com.iamin.data.service.HolidaysService;
import com.iamin.data.service.LoginRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.google.auth.oauth2.GoogleCredentials;
import com.iamin.FirebaseInitializer;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@CssImport(value = "dashboard-styles.css")
@PageTitle("Manage Requests")
@Route(value = "manage-requests", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ManageRequestsView extends Div implements BeforeEnterObserver{
    //Checks if current user has a SamplePerson entity and if not shows a sign up dialog
    @Autowired
    LoginRepository loginRepository;
    
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Login userLogin;
    String currentUsername = authentication.getName();
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        try{
            userLogin = loginRepository.findByUsername(currentUsername);
        }catch(Exception e){
            userLogin = null;
        }
        // Check your condition and redirect if necessary
        boolean Redirect = (userLogin != null && userLogin.getPerson() == null);
        if (Redirect) {
            UI.getCurrent().getPage().executeJs("location.href = 'dashboard'");
        }
    }
    //end check
    
    String currentUserName;
    String currentUserRole;
    private final AbsenceService absenceService;
    private final HolidaysService holidayService;
    private final FirebaseInitializer firebaseInitializer;

    @Autowired
    public ManageRequestsView(AbsenceService absenceService, HolidaysService holidayService, FirebaseInitializer firebaseInitializer) {
        this.absenceService = absenceService;
        this.holidayService = holidayService;
        this.firebaseInitializer = firebaseInitializer;


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
        requestsTable.addColumn(Request::getFirstName).setHeader("First Name").setAutoWidth(true);
        requestsTable.addColumn(Request::getLastName).setHeader("Last Name").setAutoWidth(true);
        requestsTable.addColumn(Request::getStartDate).setHeader("Start date").setAutoWidth(true);
        requestsTable.addColumn(Request::getEndDate).setHeader("End date").setAutoWidth(true);
        requestsTable.addColumn(Request::getReason).setHeader("Reason").setAutoWidth(true);
        requestsTable.addColumn(Request::getRequestType).setHeader("Request Type").setAutoWidth(true);
        requestsTable.addColumn(new ComponentRenderer<>(document -> {
            if (document.getDocumentsURL() != null && !document.getDocumentsURL().isEmpty()) {
                Button viewButton = new Button("View Document", clickEvent -> {
                    try {
                        String documentPath = document.getDocumentsURL(); 
                        BlobId blobId = BlobId.of(StorageClient.getInstance().bucket().getName(), documentPath);
                        Storage storage = StorageOptions.newBuilder().setCredentials(GoogleCredentials.fromStream(FirebaseInitializer.class.getClassLoader().getResourceAsStream("iamin-381803-138505f81084.json"))).build().getService();
                        Blob blob = storage.get(blobId);
        
                        URL url = blob.signUrl(30, TimeUnit.MINUTES, Storage.SignUrlOption.httpMethod(HttpMethod.GET), Storage.SignUrlOption.withV4Signature(), Storage.SignUrlOption.signWith(firebaseInitializer.getServiceAccountSigner()));
                        UI.getCurrent().getPage().open(url.toString(), "_blank");
                    } catch (Exception e) {
                        Notification.show("Error retrieving file", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                });
                return viewButton;
            } else {
                return new Div(); 
            }
        })).setHeader(" ").setAutoWidth(true);


    
        requestsTable.addComponentColumn(request -> {
            Button approveButton = new Button("Approve");
            approveButton.addClickListener(event -> {
                if ("absence".equals(request.getRequestType())) {
                    Optional<Absence> absence = absenceService.findById(request.getRequestId());
                    if (absence.isPresent()) {
                        absence.get().setAbsenceApproval(true);
                        absence.get().setDateModified();
                    absenceService.createAbsenceRequest(absence.get());
                    }
                } else if ("holiday".equals(request.getRequestType())) {
                    Optional<Holidays> holiday = holidayService.findById(request.getRequestId());
                    if (holiday.isPresent()) {
                        holiday.get().setHolidaysApproval(true);
                        holiday.get().setDateModified();
                        holidayService.createHolidayRequest(holiday.get());
                    }
                }
                dataProvider.refreshAll();
                new Page(UI.getCurrent()).reload();
            });
            return approveButton;
        }).setHeader("Approve").setAutoWidth(true);

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
                        absence.get().setDateModified();
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
                        FlexLayout denyReasonLayout = new FlexLayout();
                        denyReasonLayout.getStyle().set("flex-direction", "column");
                        denyReasonLayout.getStyle().set("gap", "20px");
                        denyReasonTextArea.setPlaceholder("Enter deny reason...");
                        Button confirmButton = new Button("Confirm", event2 -> {
                            if (denyReasonTextArea.getValue().isEmpty()) {
                                Notification.show("Please enter a reason for denying the request");
                                return;
                            }
                            holiday.get().setHolidaysApproval(false);
                            holiday.get().setDenyReason(denyReasonTextArea.getValue()); 
                            holiday.get().setDateModified();
                            holidayService.createHolidayRequest(holiday.get());
                            requestsTable.getDataProvider().refreshAll();
                            new Page(UI.getCurrent()).reload();
                            denyReasonDialog.close();
                        });
                        denyReasonLayout.add(denyReasonTextArea, confirmButton);
                        denyReasonDialog.add(denyReasonLayout);
                        denyReasonDialog.open();
                    }
                }
            });
            return denyButton;
        }).setHeader("Deny").setAutoWidth(true);
    
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
