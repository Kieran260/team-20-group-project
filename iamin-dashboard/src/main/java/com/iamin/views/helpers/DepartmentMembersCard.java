package com.iamin.views.helpers;



import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.html.Label;
import java.util.ArrayList;
import com.vaadin.flow.component.grid.GridVariant;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.iamin.data.Role;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.avatar.Avatar;

@Component
public class DepartmentMembersCard {

    @Autowired
    private LoginService loginService;

    public Div createCard(Div card, Authentication authentication) {
        card.getStyle().set("display","flex");
        card.getStyle().set("flex-direction","column");
        card.getStyle().set("justify-content","space-between");
        card.getStyle().set("padding","20px 10px");
        Styling.styleSquareBox(card);

        // Create Label
        Label cardHeader = new Label("Your Employees");
        cardHeader.getStyle().set("font-weight", "bold");
        cardHeader.getStyle().set("font-size", "18px");
        cardHeader.getStyle().set("margin-left","10px");

        // Create Grid

        List<SamplePerson> people = loginService.findAllByRole(Role.USER).stream().map(Login::getPerson).collect(Collectors.toList());

        ListDataProvider<SamplePerson> dataProvider = new ListDataProvider<>(people);

        // Create the employeeTable and set its data provider
        Grid<SamplePerson> employeeTable = new Grid<>();
        employeeTable.setItems(dataProvider);

        // Add columns to the employeeTable
        employeeTable.addColumn(SamplePerson::getFirstName).setHeader("First Name");
        employeeTable.addColumn(SamplePerson::getLastName).setHeader("Last Name");
        employeeTable.addColumn(SamplePerson::getJobTitle).setHeader("Job Title");
        employeeTable.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        card.add(cardHeader, employeeTable);

        return card;
    }

}
