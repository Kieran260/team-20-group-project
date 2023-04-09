package com.iamin.views.helpers;

import com.iamin.data.entity.CheckInOut;
import com.iamin.data.service.CheckInOutRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.html.Label;

import java.time.LocalDate;
import java.util.ArrayList;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.grid.GridVariant;

@Component
public class EmployeesTableCard {
    
    private CheckInOutRepository checkInOutRepository;

    @Autowired
    public EmployeesTableCard(CheckInOutRepository checkInOutRepository) {
        this.checkInOutRepository = checkInOutRepository;
    }

    public Div createCard(Div card) {

        // Card Styles
        card.getStyle().set("display","flex");
        card.getStyle().set("flex-direction","column");
        card.getStyle().set("justify-content","space-between");
        card.getStyle().set("padding","20px 10px");
        Styling.styleSquareBox(card);


        // TODO: Populate "people" with employees from  who have checked in today
        // Order table by latest check in at the top and earliest at the bottom
        List<CheckInOut> todaysCheckIns = checkInOutRepository.findByDateOrderByCheckInTimeDesc(LocalDate.now());

        List<Person> people = todaysCheckIns.stream()
        .map(checkInOut -> new Person(checkInOut.getPerson().getFirstName(), checkInOut.getPerson().getLastName(), checkInOut.getcheckInTime().toString()))
        .collect(Collectors.toList());
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
        private final String checkInTime;

    
        public Person(String firstName, String lastName, String checkInTime) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.checkInTime = checkInTime;
        }
    
        public String getFirstName() {
            return firstName;
        }
    
        public String getLastName() {
            return lastName;
        }

        public String getCheckInTime() {
            return checkInTime;
        }
    }
    

}

