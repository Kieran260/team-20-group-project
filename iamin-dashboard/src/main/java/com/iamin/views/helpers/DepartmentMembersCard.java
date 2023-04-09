package com.iamin.views.helpers;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.html.Label;
import java.util.ArrayList;
import com.vaadin.flow.component.grid.GridVariant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.SamplePersonService;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.dependency.CssImport;

@Component
public class DepartmentMembersCard {

    private final SamplePersonService samplePersonService;
    @Autowired
    public DepartmentMembersCard(SamplePersonService samplePersonService) {
        this.samplePersonService = samplePersonService;
    }

    public Div createCard(Div card, Authentication authentication) {
        card.getStyle().set("display","flex");
        card.getStyle().set("flex-direction","column");
        card.getStyle().set("justify-content","space-between");
        card.getStyle().set("padding","20px 10px");
        Styling.styleSquareBox(card);

        // Create Label
        Label cardHeader = new Label("All Employees");
        cardHeader.getStyle().set("font-weight", "bold");
        cardHeader.getStyle().set("font-size", "18px");
        cardHeader.getStyle().set("margin-left","10px");

        // Create Grid
        List<Person> people = samplePersonService.getAllSamplePersons().stream()
    .map(samplePerson -> new Person(samplePerson.getFirstName(), samplePerson.getLastName(), samplePerson.getEmail()))
    .collect(Collectors.toList());
    ListDataProvider<Person> dataProvider = new ListDataProvider<>(people);


        // Create the employeeTable and set its data provider
        Grid<Person> employeeTable = new Grid<>();
        employeeTable.setDataProvider(dataProvider);
    
        // Add columns to the employeeTable
        
        employeeTable.addColumn(Person::getFirstName).setHeader("First name").setAutoWidth(true);
        employeeTable.addColumn(Person::getLastName).setHeader("Last name").setAutoWidth(true);
        employeeTable.addColumn(new ComponentRenderer<>(person -> {
            Anchor emailLink = new Anchor("mailto:" + person.getEmail(), person.getEmail());
            emailLink.getStyle().set("text-decoration", "underline");
            emailLink.getStyle().set("color", "#0000ff"); 
            emailLink.getStyle().set("white-space", "nowrap"); 
            return emailLink;
        })).setHeader("Email").setAutoWidth(true);

       
              
        employeeTable.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        employeeTable.setWidth("100%");
        card.add(cardHeader,employeeTable);

        return card;
    }

    private static class Person {
        private final String firstName;
        private final String lastName;
        private final String email;
    
        public Person(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;

        }
    
        public String getFirstName() {
            return firstName;
        }
    
        public String getLastName() {
            return lastName;
        }
        
        public String getEmail() {
            return email;
        }
    }

}
   
