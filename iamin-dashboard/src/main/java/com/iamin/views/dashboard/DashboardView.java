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


@CssImport(value = "dashboard-styles.css")
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
    
        


        Div cardsContainer = new Div();

        //cardsContainer.getStyle().set("border", "2px solid red");
        cardsContainer.setClassName("cardContainer");
  
/* 
        FlexLayout cardsLayout = new FlexLayout();
        cardsLayout.setWidthFull();
        cardsLayout.getStyle().set("border", "2px solid red");
        cardsLayout.setFlexWrap(FlexWrap.WRAP);
        cardsLayout.getStyle().set("justify-content", "space-between");
        cardsLayout.getStyle().set("gap", "5px");
        cardsLayout.getStyle().set("max-width", "1399px");
        cardsLayout.getStyle().set("margin", "0 auto");
*/
        
        Div card1 = new Div();
        card1.getStyle().set("display","flex");
        card1.getStyle().set("flex-direction","column");
        card1.getStyle().set("justify-content","space-between");

        styleBoxes(card1);

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
        grid.getStyle().set("max-height", "80%");
        grid.getStyle().set("width", "100%");
        grid.getStyle().set("overflow", "hidden");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        

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
        label.getStyle().set("margin-left","10px");
        card1.add(label,grid);
        




        Div card2 = new Div();
        styleBoxes(card2);

        
        Div card3 = new Div();
        styleBoxes(card3);
                
        Div card4 = new Div();
        styleBoxes(card4);
                
        Div card5 = new Div();
        styleBoxes(card5);
                
        Div card6 = new Div();
        styleBoxes(card6);
    
        //cardsLayout.add();
        cardsContainer.add(card1,card2,card3,card4,card5,card6);





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



    private void styleBoxes(Div div) {
        div.getStyle().set("background-color", "rgba(250, 250, 250)");
        div.getStyle().set("border-radius", "2.5px");
        div.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.25)");
        div.setClassName("card");
    }
}
