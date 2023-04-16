package com.iamin.views.viewRequests;
import com.google.cloud.storage.Blob;
import com.iamin.views.MainLayout;
import com.iamin.views.helpers.Styling;
import com.iamin.data.service.AbsenceRepository;
import com.iamin.data.service.HolidaysRepository;
import com.iamin.views.helpers.ViewRequestsTableCard;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.cloud.StorageClient;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import com.google.auth.oauth2.GoogleCredentials;
import com.iamin.FirebaseInitializer;
import com.vaadin.flow.component.html.Div;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.button.Button;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.iamin.views.helpers.RequestDialog;
import com.google.cloud.storage.BlobId;
import com.google.firebase.cloud.StorageClient;
import com.iamin.data.entity.Absence;
import com.iamin.data.entity.Holidays;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.AbsenceService;
import com.iamin.data.service.HolidaysService;
import com.iamin.data.service.LoginService;
import com.iamin.views.helpers.Request;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;

@CssImport(value = "dashboard-styles.css")
@PageTitle("View Requests")
@Route(value = "viewRequests", layout = MainLayout.class)
@RolesAllowed("USER")
public class ViewRequestsView extends Div {

    String currentUserName;
    String currentUserRole;



    private HolidaysService holidaysService;
    private AbsenceService absenceService;
    private LoginService loginService;
    private FirebaseInitializer firebaseInitializer;


    public ViewRequestsView(AbsenceService absenceService, HolidaysService holidaysService, LoginService loginService, FirebaseInitializer firebaseInitializer) {
        this.absenceService = absenceService;
        this.holidaysService = holidaysService;
        this.loginService = loginService;
        this.firebaseInitializer = firebaseInitializer;

        // Master Container
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        // Top Table
        Div tables = configureContent();
        layout.add(tables);

        add(layout);
    }


    public Div configureContent() {
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

        activeRequests.addColumn(Request::getFirstName).setHeader("First Name").setAutoWidth(true);
        activeRequests.addColumn(Request::getLastName).setHeader("Last Name").setAutoWidth(true);
        activeRequests.addColumn(Request::getStartDate).setHeader("Start date").setAutoWidth(true);
        activeRequests.addColumn(Request::getEndDate).setHeader("End date").setAutoWidth(true);
        activeRequests.addColumn(Request::getType).setHeader("Type").setAutoWidth(true);
        activeRequests.addColumn(Request::getReason).setHeader("Reason").setAutoWidth(true);
        activeRequests.addColumn(new ComponentRenderer<>(Request::getApprovedComponent)).setHeader("Approved").setAutoWidth(true);
        activeRequests.addColumn(new ComponentRenderer<>(document -> {
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

        pastRequests.addColumn(Request::getFirstName).setHeader("First Name").setAutoWidth(true);
        pastRequests.addColumn(Request::getLastName).setHeader("Last Name").setAutoWidth(true);
        pastRequests.addColumn(Request::getStartDate).setHeader("Start date").setAutoWidth(true);
        pastRequests.addColumn(Request::getEndDate).setHeader("End date").setAutoWidth(true);
        pastRequests.addColumn(Request::getType).setHeader("Type").setAutoWidth(true);
        pastRequests.addColumn(Request::getReason).setHeader("Reason").setAutoWidth(true);
        pastRequests.addColumn(new ComponentRenderer<>(Request::getApprovedComponent)).setHeader("Approved").setAutoWidth(true);
        pastRequests.addColumn(new ComponentRenderer<>(document -> {
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SamplePerson person = loginService.getSamplePersonByUsername(authentication.getName());
        List<Holidays> allHolidays = holidaysService.getHolidaysForPerson(person);
        List<Absence> allAbsences = absenceService.getAbsencesForPerson(person);
    
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