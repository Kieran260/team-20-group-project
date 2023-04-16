package com.iamin.views.documentUploadView;
import com.google.cloud.storage.BlobId;
import com.google.firebase.cloud.StorageClient;
import com.iamin.data.entity.Document;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.DocumentService;
import com.iamin.data.service.SamplePersonService;
import com.iamin.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.UI;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import javax.annotation.security.RolesAllowed;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.cloud.StorageClient;
import com.vaadin.flow.component.UI;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import com.iamin.FirebaseInitializer;
import com.google.auth.ServiceAccountSigner;
import com.google.auth.oauth2.GoogleCredentials;
@Route(value = "upload", layout = MainLayout.class)
@PageTitle("Assign Documents")
@RolesAllowed("ADMIN")
public class DocumentUploadView extends Div {

    private DocumentService documentService;
    private SamplePersonService personService;
    private FirebaseInitializer firebaseInitializer;
    private SplitLayout splitLayout = new SplitLayout();

    public DocumentUploadView(DocumentService documentService, SamplePersonService personService, FirebaseInitializer firebaseInitializer) {
        this.documentService = documentService;
        this.personService = personService;
        this.firebaseInitializer = firebaseInitializer;

        addClassName("document-upload-view");
        splitLayout.getStyle().set("width", "100%");

        configureUploadContainer();

        configureDocumentGrid();

        add(splitLayout);
    }

    private void configureDocumentGrid() {
        VerticalLayout documentGridContainer = new VerticalLayout();
        documentGridContainer.setWidth("80%");

        Grid<Document> documentGrid = new Grid<>();
        documentGrid.addClassName("tasks-grid");
        
        List<Document> signedDocuments = documentService.getSignedDocuments();

        ListDataProvider<Document> dataProvider = new ListDataProvider<>(signedDocuments);
        documentGrid.setDataProvider(dataProvider);
        documentGrid.addColumn(Document::getDocumentTitle).setHeader("Title").setAutoWidth(true);
        documentGrid.addColumn(Document::getDocumentDescription).setHeader("Description").setAutoWidth(true);
        documentGrid.addColumn(task -> {
            SamplePerson person = task.getPerson();
            return person != null ? person.getFirstName() + " " + person.getLastName() : "";
        }).setHeader("Employee").setAutoWidth(true);
        documentGrid.addColumn(Document::getSigned).setHeader("Signed").setAutoWidth(true);
        documentGrid.addColumn(Document::getUploadDate).setHeader("Upload Date").setAutoWidth(true);
        documentGrid.addColumn(Document::getSubmitDate).setHeader("Submit Date").setAutoWidth(true);    
        documentGrid.addColumn(new ComponentRenderer<>(document -> {
            if (document.getDocumentUrl() != null && !document.getDocumentUrl().isEmpty()) {
                Button viewButton = new Button("View Document", clickEvent -> {
                    try {
                        String documentPath = document.getDocumentUrl(); 
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
        })).setHeader("URL").setAutoWidth(true);
        
        H4 title = new H4("Signed Documents");
        documentGridContainer.add(title , documentGrid);
        splitLayout.addToPrimary( documentGridContainer);
    }

    private void configureUploadContainer() {
        VerticalLayout uploadContainer = new VerticalLayout();
        uploadContainer.setWidth("20%");

        MemoryBuffer buffer = new MemoryBuffer();
        TextField titleField = new TextField("Document Title");
        TextField descriptionField = new TextField("Document Description");
        DatePicker datePicker = new DatePicker("Submit Date");
        descriptionField.getStyle().set("margin-top", "16px");
        titleField.setWidthFull();
        descriptionField.setWidthFull();
        datePicker.setWidthFull();

        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("application/pdf"); 
        Button submitButton = new Button("Submit");

        List<SamplePerson> employees = personService.getAllSamplePersons(); 
        MultiSelectComboBox<SamplePerson> employeeList = new MultiSelectComboBox<>("Select Employees");
        employeeList.setDataProvider(new ListDataProvider<>(employees));
        upload.setWidthFull();
        submitButton.setWidthFull();
        employeeList.setWidthFull();
        employeeList.setRenderer(new ComponentRenderer<>(person -> {
            HorizontalLayout row = new HorizontalLayout();
            row.setAlignItems(FlexComponent.Alignment.CENTER);
        
            Span name = new Span(person.getFirstName()+ " " + person.getLastName());
            Span profession = new Span(person.getJobTitle());
            profession.getStyle()
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-size", "var(--lumo-font-size-s)");
        
            VerticalLayout column = new VerticalLayout(name, profession);
            column.setPadding(false);
            column.setSpacing(false);
        
            row.add(column);
            row.getStyle().set("line-height", "var(--lumo-line-height-m)");
            return row;
        }));

        
        final String[] fileUrl = {""};

        upload.addSucceededListener(event -> {
            try {
                InputStream inputStream = buffer.getInputStream();
                String uuid = UUID.randomUUID().toString();
                String fileExtension = event.getFileName().substring(event.getFileName().lastIndexOf('.'));
                String firebaseStorageFileName = "documents/" + uuid + fileExtension;

                StorageClient.getInstance().bucket().create(firebaseStorageFileName, inputStream, event.getMIMEType());

                String filelink = StorageClient.getInstance().bucket().get(firebaseStorageFileName).signUrl(30, TimeUnit.MINUTES).toString();
                fileUrl[0] = firebaseStorageFileName;
                Notification.show("File uploaded successfully", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (Exception e) {
                Notification.show("Error uploading file", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        submitButton.addClickListener(event -> {
            String title = titleField.getValue();
            String description = descriptionField.getValue();
            if (fileUrl[0] != null && title != null && description != null && datePicker.getValue() != null && employeeList.getValue() != null) {
                
        
                Document document = new Document();
                document.setDocumentTitle(title);
                document.setDocumentDescription(description);
                document.setDocumentUrl(fileUrl[0]);
                document.setUploadDate(LocalDate.now());
                document.setSubmitDate(datePicker.getValue());
                document.setSigned(false);
        
                Set<SamplePerson> selectedEmployees = employeeList.getSelectedItems();
                for (SamplePerson employee : selectedEmployees) {
                    document.setPerson(employee);
                    documentService.createDocument(document);
                }
                Notification.show("Document submitted successfully", 3000, Notification.Position.TOP_CENTER);
                titleField.setValue("");
                descriptionField.setValue("");
                datePicker.clear();
                employeeList.clear();
                fileUrl[0] = "";
                upload.getElement().setProperty("value", null);
            } else {
                Notification.show("Please fill all fields", 3000, Notification.Position.TOP_CENTER);
            }
        });


        uploadContainer.add( titleField, descriptionField, datePicker,  employeeList, upload, submitButton);
        splitLayout.addToSecondary(uploadContainer);
    }
    
    

}

