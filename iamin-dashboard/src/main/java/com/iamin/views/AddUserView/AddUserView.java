package com.iamin.views.AddUserView;

import javax.annotation.security.RolesAllowed;

import com.iamin.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.Key;

@PageTitle("AddUsers")
@Route(value = "AddUsers", layout= MainLayout.class)
@RolesAllowed("ADMIN")

public class AddUserView extends FormLayout{
    private TextField  firstName   = new TextField("First Name");
    private TextField  lastName    = new TextField("Last Name");
    private TextField  phone       = new TextField("Phone");
    private DatePicker dateOfBirth = new DatePicker("Date Of Birth");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    
    public AddUserView(){
        add(firstName,
            lastName,
            phone,
            dateOfBirth,
            createButtonsLayout());
    }

    private void CreateForm(FlexLayout layout){
        Div addForm = new Div();
        FormLayout formLayout = new FormLayout();
        
        //important = new Checkbox("Important");
        formLayout.add(firstName, lastName, phone, dateOfBirth);
        addForm.add(formLayout);
        layout.add(addForm);
    }
    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY); 
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    
        save.addClickShortcut(Key.ENTER); 
        close.addClickShortcut(Key.ESCAPE);
    
        return new HorizontalLayout(save, delete, close); 
      }
}