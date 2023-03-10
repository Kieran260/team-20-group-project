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




@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll


public class DashboardView extends VerticalLayout {

    public DashboardView() {
        // Define the data for the grid table
        // TO DO: show department employees for manager of specific department.
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
    
        // Create the grid and set its data provider
        Grid<Person> grid = new Grid<>();
        grid.setDataProvider(dataProvider);
    
        // Add columns to the grid
        grid.addColumn(Person::getFirstName).setHeader("First Name");
        grid.addColumn(Person::getLastName).setHeader("Last Name");
    
        // Set the height of the grid to be the height of five rows,
        // or add a scroll bar if there are more than five rows
        int numberOfRows = Math.min(8, people.size());
        grid.setAllRowsVisible(false);
        grid.getStyle().set("max-height", "275px");
        grid.getElement().getStyle().set("overflow-y", "scroll");

        grid.getElement().executeJs("var mouseDown = false;\n" +
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
        "});", grid);


        // Create Labels
        Label label = new Label("Your Department's Employees");
        label.getStyle().set("font-weight", "bold");
        label.getStyle().set("font-size", "24px");


        Div cardsContainer = new Div();
        cardsContainer.getStyle().set("width", "100%");



        FlexLayout cardsLayout = new FlexLayout();
        cardsLayout.setWidthFull();
        //cardsLayout.getStyle().set("border", "2px solid red");
        cardsLayout.setFlexWrap(FlexWrap.WRAP);
        cardsLayout.getStyle().set("justify-content", "space-around");
        cardsLayout.getStyle().set("gap", "20px");

        
        Div card1 = new Div();
        card1.setWidth("350px");
        card1.setHeight("250px");
        card1.getStyle().set("background-color", "blue");
        card1.getStyle().set("border-radius", "10px");

        
        Div card2 = new Div();
        card2.setWidth("350px");
        card2.setHeight("250px");
        card2.getStyle().set("background-color", "green");
        card2.getStyle().set("border-radius", "10px");

        
        Div card3 = new Div();
        card3.setWidth("350px");
        card3.setHeight("250px");
        card3.getStyle().set("background-color", "yellow");
        card3.getStyle().set("border-radius", "10px");

        
        cardsLayout.add(card1, card2, card3);
        cardsContainer.add(cardsLayout);
        
        



        // Add the content div to the layout
        add(cardsContainer);
        add(label);
        add(grid);
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
}
