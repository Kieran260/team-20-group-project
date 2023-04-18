package com.iamin.views.documentEmployeeView;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import com.vaadin.flow.component.button.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.google.firebase.cloud.StorageClient;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.StorageOptions;
import com.iamin.data.entity.Document;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.DocumentService;
import com.iamin.data.service.LoginService;
import com.iamin.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.iamin.FirebaseInitializer;


@Uses(Icon.class)
@PageTitle("View Documents")
@Route(value = "view-documents", layout = MainLayout.class)
@RolesAllowed("USER")
public class DocumentsEmployeeView extends VerticalLayout {

    private LoginService loginService;
    private DocumentService documentService;
    private FirebaseInitializer firebaseInitializer;
 
    @Autowired
    public DocumentsEmployeeView(LoginService loginService, DocumentService documentService, FirebaseInitializer firebaseInitializer) {
        this.loginService = loginService;
        this.documentService = documentService;
        this.firebaseInitializer = firebaseInitializer;
       }
 
       @PostConstruct
       private void onPostConstruct() {
           configureDocuments();
       }
    
 
    public void configureDocuments() {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       SamplePerson person = loginService.getSamplePersonByUsername(authentication.getName());
       List<Document> documents = documentService.findDocumentsForPerson(person);
       documents.sort(Comparator.comparing(Document::getSubmitDate));
       ListDataProvider<Document> dataProvider = new ListDataProvider<>(documents);   
       Grid<Document> documentGrid = new Grid<>();
       VerticalLayout documentLayout = new VerticalLayout();
       documentLayout.setWidth("100%");
       setSizeFull();
       setPadding(false); 
       setMargin(false); 
       setAlignItems(Alignment.STRETCH); 
       documentLayout.setHeightFull();
       documentLayout.setPadding(false); 
       documentLayout.setMargin(false);
       documentGrid.setSizeFull();
      

       documentGrid.addClassName("view-documents");
       documentGrid.setDataProvider(dataProvider);
       documentGrid.addColumn(Document::getDocumentTitle).setHeader("Document Name").setAutoWidth(true);
       documentGrid.addColumn(Document::getDocumentDescription).setHeader("Description").setAutoWidth(true);
       documentGrid.addColumn(Document::getUploadDate).setHeader("Date Received").setAutoWidth(true);
       documentGrid.addColumn(Document::getSubmitDate).setHeader("Submission Deadline").setAutoWidth(true);
       documentGrid.addColumn(Document::getSigned).setHeader("Signed").setAutoWidth(true);
       documentGrid.addColumn(new ComponentRenderer<>(document -> {
        if (document.getDocumentUrl() != null && !document.getDocumentUrl().isEmpty()) {
            Button viewButton = new Button("View Document", clickEvent -> {
                Notification.show("Please download the file and re-upload the signed document.", 3000, Notification.Position.TOP_CENTER);
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
    })).setHeader("View Document").setAutoWidth(true);

    documentGrid.addColumn(new ComponentRenderer<>(document -> {
        Button uploadButton = new Button("Upload Document");
        if (document.getSigned() == true) {
            uploadButton.setEnabled(false);
        } else {
            uploadButton.setEnabled(true);
        }
        uploadButton.addClickListener(e -> {
            Dialog uploadDialog = new Dialog();
            uploadDialog.setWidth("400px");
            uploadDialog.setResizable(true);

            VerticalLayout dialogLayout = new VerticalLayout();
            dialogLayout.setAlignItems(Alignment.CENTER);

            MemoryBuffer buffer = new MemoryBuffer();
            Upload upload = new Upload(buffer);
            upload.setAcceptedFileTypes("application/pdf");
            upload.setWidthFull();
            Label uploadLabel = new Label("Please upload the signed document.");
            Button submitButton = new Button("Submit");
            submitButton.setWidthFull();

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
                } catch (Exception ex) {
                    Notification.show("Error uploading file", 3000, Notification.Position.TOP_CENTER).addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });

            submitButton.addClickListener(event -> {
                if (fileUrl[0] != "") {
                    document.setDocumentUrl(fileUrl[0]);
                    document.setSigned(true);
                    document.setSignDate(LocalDate.now());
                    documentService.updateDocument(document);
                    Notification.show("Document uploaded successfully.", 3000, Notification.Position.TOP_CENTER);
                    fileUrl[0] = "";
                    upload.getElement().setProperty("value", null);
                    uploadDialog.close();
                    configureDocuments();
                } else {
                    Notification.show("Please upload a file", 3000, Notification.Position.TOP_CENTER);
                }
            });

            dialogLayout.add(uploadLabel, upload, submitButton);
            uploadDialog.add(dialogLayout);
            uploadDialog.open();
        });
        return uploadButton;
        
    })).setHeader("Upload Signed").setAutoWidth(true);
    
    
      documentGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
 
 
       documentLayout.add(documentGrid);
       add(documentLayout);
   }
 
 }
 