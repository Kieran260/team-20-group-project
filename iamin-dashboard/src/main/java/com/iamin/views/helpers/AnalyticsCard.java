package com.iamin.views.helpers;

import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.CheckInOutService;
import com.iamin.data.service.TasksService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.select.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AnalyticsCard {

    private CheckInOutService checkInOutService;
    private TasksService taskService;


    @Autowired
    public AnalyticsCard(CheckInOutService checkInOutService, TasksService taskService) {
        this.checkInOutService = checkInOutService;
        this.taskService = taskService;
    }

    public Div createCard(Div card) {
        card.getStyle().set("display", "flex");
        card.getStyle().set("flex-direction", "column");
        card.getStyle().set("justify-content", "space-between");
    
        Styling.styleSquareBox(card);
    
        // Create div containers
        Div topLeft = createQuadrantDiv();
        Div topRight = createQuadrantDiv();
        Div bottomLeft = createQuadrantDiv();
        Div bottomRight = createQuadrantDiv();
    
        // Create label
        Label cardHeader = new Label("Analytics");
        cardHeader.getStyle().set("font-weight", "bold");
        cardHeader.getStyle().set("font-size", "18px");
    
        FlexLayout cardContent = new FlexLayout();
        cardContent.getStyle().set("display", "flex");
        cardContent.getStyle().set("flex-wrap", "wrap");
        cardContent.getStyle().set("height", "calc(100% - 1.5em)");
        cardContent.getStyle().set("width", "100%");
    
        cardContent.add(topLeft, topRight, bottomLeft, bottomRight);
        card.add(cardHeader, cardContent);
    
        return card;
    }
    

    private Div createQuadrantDiv() {
        Div quadrant = new Div();
        quadrant.getStyle().set("width", "50%");
        quadrant.getStyle().set("height", "50%");
        quadrant.getStyle().set("box-sizing", "border-box");
        quadrant.getStyle().set("padding", "4px");
    
        quadrant.getStyle().set("border", "1px solid red");
    
        return quadrant;
    }
    
}
