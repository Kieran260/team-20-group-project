package com.iamin.views.dashboard;

import com.iamin.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.PermitAll;
import java.util.Arrays;
import java.util.List;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.grid.GridVariant;
import java.time.LocalTime;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.iamin.data.entity.User;
import com.iamin.security.AuthenticatedUser;
import java.time.LocalTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@CssImport(value = "dashboard-styles.css")
@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll


public class DashboardView extends VerticalLayout {

    String currentUserName;
    String currentUserRole;

    public DashboardView() {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Dialog holidayDialog = new Dialog();
        Dialog absenceDialog = new Dialog();
   

        // Define the data for the employeeTable table
        // TO DO: show department employees for manager of specific department.

        getStyle().set("background-color","rgba(250, 250, 250)");

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
        Div cardsContainer = new Div();

        //cardsContainer.getStyle().set("border", "2px solid red");
        cardsContainer.setClassName("cardContainer");
        
        // Employees Table Card    
        Div card1 = new Div();
        card1.getStyle().set("display","flex");
        card1.getStyle().set("flex-direction","column");
        card1.getStyle().set("justify-content","space-between");
        card1.getStyle().set("padding","20px 10px");
        styleSquareBox(card1);

        // Create the employeeTable and set its data provider
        Grid<Person> employeeTable = new Grid<>();
        employeeTable.setDataProvider(dataProvider);
    
        // Add columns to the employeeTable
        employeeTable.addColumn(Person::getFirstName).setHeader("First Name");
        employeeTable.addColumn(Person::getLastName).setHeader("Last Name");
    
        // Set the height of the employeeTable to be the height of five rows,
        // or add a scroll bar if there are more than five rows
        int numberOfRows = Math.min(8, people.size());
        employeeTable.setAllRowsVisible(false);
        employeeTable.getStyle().set("overflow", "hidden");
        employeeTable.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        

        employeeTable.getElement().executeJs("var mouseDown = false;\n" +
        "var scrollTop;\n" +
        "var scrollLeft;\n" +
        "var startX;\n" +
        "var startY;\n" +
        "var scrollable = $0.$.table;\n" +
        "scrollable.addEventListener('mousedown', function(e) {\n" +
        "    mouseDown = true;\n" +
        "    startX = e.clientX;\n" +
        "    startY = e.clientY;\n" +
        "    scrollTop = scrollable.scrollTop;\n" +
        "    scrollLeft = scrollable.scrollLeft;\n" +
        "});\n" +
        "scrollable.addEventListener('mouseup', function(e) {\n" +
        "    mouseDown = false;\n" +
        "});\n" +
        "scrollable.addEventListener('mousemove', function(e) {\n" +
        "    if (mouseDown) {\n" +
        "        var xDiff = e.clientX - startX;\n" +
        "        var yDiff = e.clientY - startY;\n" +
        "        scrollable.scrollTop = scrollTop - yDiff;\n" +
        "        scrollable.scrollLeft = scrollLeft - xDiff;\n" +
        "    }\n" +
        "});", employeeTable);

        // Create Labels
        Label card1Header = new Label("Your Department's Employees");
        card1Header.getStyle().set("font-weight", "bold");
        card1Header.getStyle().set("font-size", "24px");
        card1Header.getStyle().set("margin-left","10px");
        
        card1.add(card1Header,employeeTable);
        

        // Check In / Check Out Card
        Div card2 = new Div();
        card2.getStyle().set("display","flex");
        card2.getStyle().set("flex-direction","column");
        card2.getStyle().set("justify-content","flex-start");
        card2.getStyle().set("gap","20px");

        Div card2Top = new Div();
        card2Top.getStyle().set("display","flex");
        card2Top.getStyle().set("flex-direction","column");
        card2Top.getStyle().set("justify-content","flex-start");

        styleHalfSquareBox(card2Top);

        Label card2Header = new Label("Work Hours");
        card2Header.getStyle().set("font-weight", "bold");
        card2Header.getStyle().set("font-size", "18px");


        // TODO: 
        // Change authentication.getName() to fetch the legal first and last name
        // Query the table to see if the user is actually checked in currently or not 
        Label card2SubHeader = new Label(authentication.getName() + ": You are currently not checked in");
        card2SubHeader.getStyle().set("font-size", "16px");
    
        Button checkInButton = new Button("Check In");
        Button checkOutButton = new Button("Check Out");
        Button editTimesheetButton = new Button("Edit");

        FlexLayout buttonContainer = new FlexLayout();        
        buttonContainer.getStyle().set("gap","10px");
        buttonContainer.getStyle().set("align-items", "start");
        buttonContainer.getStyle().set("margin-top", "10px");
        buttonContainer.add(checkInButton,checkOutButton,editTimesheetButton);

        card2Top.add(card2Header,card2SubHeader,buttonContainer);

        Div card2Bottom = new Div();
        styleHalfSquareBox(card2Bottom);


        Label card2BottomHeader = new Label("Request Absence");
        card2BottomHeader.getStyle().set("font-weight", "bold");
        card2BottomHeader.getStyle().set("font-size", "18px");

        FlexLayout absenceButtonContainer = new FlexLayout();
        Button holidayRequestButton = new Button("Holiday Request");
        Button absenceRequestButton = new Button("Other Absence");
        absenceButtonContainer.getStyle().set("margin-top","10px");
        absenceButtonContainer.getStyle().set("gap", "10px");   
        absenceButtonContainer.add(holidayRequestButton, absenceRequestButton);


        // Holiday Dialog
        VerticalLayout holidayDialogLayout = new VerticalLayout();
        holidayDialogLayout.getStyle().set("width","300px");
        holidayDialogLayout.getStyle().set("height","300px");
        holidayDialog.add(holidayDialogLayout);
        holidayDialog.setHeaderTitle("Holiday Request");

        Button holidayCancelButton = new Button("Cancel", ee -> holidayDialog.close());
        holidayDialog.getFooter().add(holidayCancelButton);
       
        holidayRequestButton.addClickListener(e -> {
            holidayDialog.open();
        });


        // Absence Dialog
        VerticalLayout absenceDialogLayout = new VerticalLayout();
        absenceDialogLayout.getStyle().set("width","300px");
        absenceDialogLayout.getStyle().set("height","300px");
        absenceDialog.add(absenceDialogLayout);
        absenceDialog.setHeaderTitle("Absence Request");

        Button absenceCancelButton = new Button("Cancel", ee -> absenceDialog.close());
        absenceDialog.getFooter().add(absenceCancelButton);
       
        absenceRequestButton.addClickListener(e -> {
            absenceDialog.open();
        });


        // Add cards
        card2Bottom.add(card2BottomHeader,absenceButtonContainer);
        card2.add(card2Top,card2Bottom);







        Div card3 = new Div();
        styleSquareBox(card3);
                
        Div card4 = new Div();
        styleSquareBox(card4);
                
        Div card5 = new Div();
        styleSquareBox(card5);
                
        Div card6 = new Div();
        styleSquareBox(card6);
    
        //cardsLayout.add();
        cardsContainer.add(card1,card2,card3,card4,card5,card6);


        add(holidayDialog,absenceDialog);


        // Add the content div to the layout
        add(cardsContainer);
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
    }



    private void styleSquareBox(Div div) {
        div.getStyle().set("background-color", "white");
        div.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.25)");
        div.setClassName("card");
    }

    private void styleHalfSquareBox(Div div) {
        div.getStyle().set("background-color", "white");
        div.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.25)");
        div.getStyle().set("height", "120px");

        div.setClassName("card");
    }
    

}
