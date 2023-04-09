package com.iamin.views.helpers;

import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.CheckInOutRepository;
import com.iamin.data.service.LoginService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.html.Label;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.grid.GridVariant;

@Component
public class EmployeesTableCard {
    
    private CheckInOutRepository checkInOutRepository;
    private LoginService loginService;

    @Autowired
    public EmployeesTableCard(CheckInOutRepository checkInOutRepository, LoginService loginService) {
        this.checkInOutRepository = checkInOutRepository;
        this.loginService = loginService;
    }
    public Div createCardBasedOnRole(Div card, Authentication authentication) {
        if (hasRoleAdmin(authentication)) {
            return createCard(card);
        } else {
            SamplePerson person = loginService.getSamplePersonByUsername(authentication.getName());

            return createUserCard(card, person);
        }
    }

    private boolean hasRoleAdmin(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
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
    

    public Div createUserCard(Div card, SamplePerson currentUser) {
        // Card Styles
        card.getStyle().set("display", "flex");
        card.getStyle().set("flex-direction", "column");
        card.getStyle().set("justify-content", "space-between");
        card.getStyle().set("padding", "20px 10px");
        Styling.styleSquareBox(card);
    
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        List<CheckInOut> checkInOuts = checkInOutRepository.findByPersonAndDateBetween(currentUser, startDate, endDate);
    
        List<AttendanceRecord> attendanceRecords = checkInOuts.stream()
                .map(checkInOut -> new AttendanceRecord(checkInOut.getdate(), checkInOut.getcheckInTime(), checkInOut.getcheckOutTime()))
                .collect(Collectors.toList());
        ListDataProvider<AttendanceRecord> dataProvider = new ListDataProvider<>(attendanceRecords);
    
        Grid<AttendanceRecord> attendanceTable = new Grid<>();
        attendanceTable.setDataProvider(dataProvider);
    
        // Add columns to the attendanceTable
        attendanceTable.addColumn(AttendanceRecord::getDate).setHeader("Date");
        attendanceTable.addColumn(AttendanceRecord::getCheckInTime).setHeader("Check-In Time");
        attendanceTable.addColumn(AttendanceRecord::getCheckOutTime).setHeader("Check-Out Time");
    
        
        attendanceTable.setAllRowsVisible(false);
        attendanceTable.getStyle().set("overflow", "hidden");
        attendanceTable.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        
        // Create Labels
        Label cardHeader = new Label("Attendance for This Month");
        cardHeader.getStyle().set("font-weight", "bold");
        cardHeader.getStyle().set("font-size", "18px");
        cardHeader.getStyle().set("margin-left", "10px");
    
        card.add(cardHeader, attendanceTable);
        return card;
    }
    
    
    private static class AttendanceRecord {
        private final LocalDate date;
        private final LocalTime checkInTime;
        private final LocalTime checkOutTime;
    
        public AttendanceRecord(LocalDate date, LocalTime checkInTime, LocalTime checkOutTime) {
            this.date = date;
            this.checkInTime = checkInTime;
            this.checkOutTime = checkOutTime;
        }
    
        public LocalDate getDate() {
            return date;
        }
    
        public LocalTime getCheckInTime() {
            return checkInTime;
        }
    
        public LocalTime getCheckOutTime() {
            return checkOutTime;
        }
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

