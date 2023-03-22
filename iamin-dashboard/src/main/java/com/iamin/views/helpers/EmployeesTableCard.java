package com.iamin.views.helpers;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.util.List;
import com.vaadin.flow.component.html.Label;
import java.util.ArrayList;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.grid.GridVariant;


public class EmployeesTableCard {

    public Div createCard(Div card) {

        // Card Styles
        card.getStyle().set("display","flex");
        card.getStyle().set("flex-direction","column");
        card.getStyle().set("justify-content","space-between");
        card.getStyle().set("padding","20px 10px");
        Styling.styleSquareBox(card);


        // TODO: Populate "people" with employees from same manager as department who have checked in today
        // Order table by latest check in at the top and earliest at the bottom
        List<Person> people = new ArrayList<Person>();
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
        people.add(new Person("John", "Doe"));
    
        ListDataProvider<Person> dataProvider = new ListDataProvider<>(people);

        // Create the employeeTable and set its data provider
        Grid<Person> employeeTable = new Grid<>();
        employeeTable.setDataProvider(dataProvider);
    
        // Add columns to the employeeTable
        employeeTable.addColumn(Person::getFirstName).setHeader("First Name");
        employeeTable.addColumn(Person::getLastName).setHeader("Last Name");
        employeeTable.addColumn(Person::getCheckInTime).setHeader("Check-In Time");
    
        // Set the height of the employeeTable to be the height of five rows,
        // or add a scroll bar if there are more than five rows
        int numberOfRows = Math.min(8, people.size());
        employeeTable.setAllRowsVisible(false);
        employeeTable.getStyle().set("overflow", "hidden");
        employeeTable.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        

        // Create Labels
        Label card1Header = new Label("Employee Check-Ins");
        card1Header.getStyle().set("font-weight", "bold");
        card1Header.getStyle().set("font-size", "18px");
        card1Header.getStyle().set("margin-left","10px");

        card.add(card1Header,employeeTable);
        return card;
    }


    private static class Person {
        private final String firstName;
        private final String lastName;

    
        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    
        public String getFirstName() {
            return firstName;
        }
    
        public String getLastName() {
            return lastName;
        }

        public String getCheckInTime() {
            return "9:00";
        }
    }
    

}

