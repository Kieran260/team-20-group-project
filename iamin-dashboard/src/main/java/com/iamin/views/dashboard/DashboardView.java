package com.iamin.views.dashboard;

import com.iamin.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
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
    
        ListDataProvider<Person> dataProvider = new ListDataProvider<>(people);
    
        // Create the grid and set its data provider
        Grid<Person> grid = new Grid<>();
        grid.setDataProvider(dataProvider);
    
        // Add columns to the grid
        grid.addColumn(Person::getFirstName).setHeader("First Name");
        grid.addColumn(Person::getLastName).setHeader("Last Name");
    
        // Set the height of the grid to be the height of five rows,
        // or add a scroll bar if there are more than five rows
        int numberOfRows = Math.min(5, people.size());
        grid.setAllRowsVisible(false);
        grid.getStyle().set("max-height", "200px");
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


        // Create Label 
        Label label = new Label("Your Department's Employees");
        label.getStyle().set("font-weight", "bold");

        // Add the grid to the layout
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
