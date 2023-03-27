package com.iamin.views.ManageRequests;

import com.iamin.views.MainLayout;
import com.iamin.views.helpers.Styling;
import com.iamin.data.service.AbsenceRepository;
import com.iamin.data.service.HolidaysRepository;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.PermitAll;
import com.vaadin.flow.component.html.Div;

import org.springframework.beans.factory.annotation.Autowired;

@CssImport(value = "dashboard-styles.css")
@PageTitle("Manage Requests")
@Route(value = "manageRequests", layout = MainLayout.class)
@PermitAll
public class ManageRequestsView extends VerticalLayout {

    String currentUserName;
    String currentUserRole;

    @Autowired

    private final AbsenceRepository absenceRepo;
    private final HolidaysRepository holidayRepo;

    public ManageRequestsView(AbsenceRepository absenceRepo, HolidaysRepository holidayRepo) {

        this.absenceRepo = absenceRepo;
        this.holidayRepo = holidayRepo;

        getStyle().set("background-color", "rgba(250, 250, 250)");

        // Master Container
        Div cardsContainer = new Div();
        cardsContainer.setClassName("requestCardContainer");

    }
}
