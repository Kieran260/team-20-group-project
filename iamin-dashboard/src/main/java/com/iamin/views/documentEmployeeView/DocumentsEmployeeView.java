package com.iamin.views.documentEmployeeView;

import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.iamin.data.entity.Document;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.DocumentService;
import com.iamin.data.service.LoginService;
import com.iamin.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.Uses;
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

@Uses(Icon.class)
@PageTitle("View Documents")
@Route(value = "view-documents", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class DocumentsEmployeeView extends VerticalLayout {

    private LoginService loginService;
    private DocumentService documentService;
 
    @Autowired
    public DocumentsEmployeeView(LoginService loginService, DocumentService documentService) {
        this.loginService = loginService;
        this.documentService = documentService;
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
       documentGrid.addColumn(Document::getDocumentTitle).setHeader("Title").setAutoWidth(true);
       documentGrid.addColumn(Document::getDocumentDescription).setHeader("Description").setAutoWidth(true);
       documentGrid.addColumn(Document::getUploadDate).setHeader("Upload Date").setAutoWidth(true);
       documentGrid.addColumn(Document::getSubmitDate).setHeader("Submit Date").setAutoWidth(true);
       documentGrid.addColumn(Document::getSigned).setHeader("Signed").setAutoWidth(true);
       documentGrid.addColumn(new ComponentRenderer<Component , Document>(document -> {
        if (document.getDocumentUrl() != null && !document.getDocumentUrl().isEmpty()) {
            Anchor anchor = new Anchor(document.getDocumentUrl(), "View Document");
            anchor.setTarget("_blank");
            return anchor;
        } else {
            return new Div(); 
        }
    })).setHeader("URL").setAutoWidth(true);
    
    
      documentGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
 
 
       documentLayout.add(documentGrid);
       add(documentLayout);
   }
 
 }
 