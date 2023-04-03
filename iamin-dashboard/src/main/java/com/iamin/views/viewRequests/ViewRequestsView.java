package com.iamin.views.viewRequests;

import com.iamin.views.MainLayout;
import com.iamin.views.helpers.Styling;
import com.iamin.data.service.AbsenceRepository;
import com.iamin.data.service.HolidaysRepository;
import com.iamin.views.helpers.ViewRequestsTableCard;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.PermitAll;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;

import com.vaadin.flow.component.html.Div;

import org.springframework.beans.factory.annotation.Autowired;

@CssImport(value = "dashboard-styles.css")
@PageTitle("Manage Requests")
@Route(value = "manageRequests", layout = MainLayout.class)
@PermitAll
public class ViewRequestsView extends Div {

    String currentUserName;
    String currentUserRole;

    @Autowired

    private final AbsenceRepository absenceRepo;
    private final HolidaysRepository holidayRepo;

    public ViewRequestsView(AbsenceRepository absenceRepo, HolidaysRepository holidayRepo) {

        this.absenceRepo = absenceRepo;
        this.holidayRepo = holidayRepo;

        getStyle().set("background-color", "rgba(250, 250, 250)");

        // Master Container
        Div cardsContainer = new Div();
        cardsContainer.setClassName("requestCardContainer");
        cardsContainer.getStyle().set("height", "100%");
        cardsContainer.getStyle().set("width", "100%");

        Div card = new Div();
        ViewRequestsTableCard reqTableCard = new ViewRequestsTableCard();
        reqTableCard.createCard(card);

        cardsContainer.add(card);

        add(cardsContainer);
    }
}
