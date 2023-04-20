package com.iamin.views.helpers;

import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.CheckInOutService;
import com.iamin.data.service.LoginService;
import com.iamin.data.service.TasksService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
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
    private LoginService loginService;


    @Autowired
    public AnalyticsCard(CheckInOutService checkInOutService, TasksService taskService, LoginService loginService) {
        this.checkInOutService = checkInOutService;
        this.taskService = taskService;
        this.loginService = loginService;
    }

    public Div createCard(Div card) {
        card.getStyle().set("display", "flex");
        card.getStyle().set("flex-direction", "column");
        card.getStyle().set("justify-content", "space-between");
    
        Styling.styleSquareBox(card);
    
        // Create div containers
        Div topLeft = createQuadrantDiv();
        createTotalEmployees(topLeft);


        Div topRight = createQuadrantDiv();
        createLateArrivals(topRight);



        Div bottomLeft = createQuadrantDiv();
        createTasksCompletionRate(bottomLeft);


        Div bottomRight = createQuadrantDiv();
        createTasksBeyondDeadline(bottomRight);

    
        // Create label
        Label cardHeader = new Label("This Week's Statistics");
        cardHeader.getStyle().set("font-weight", "bold");
        cardHeader.getStyle().set("font-size", "18px");
        cardHeader.getStyle().set("padding-bottom","10px");
    
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
    
    
        return quadrant;
    }

    private Div createTotalEmployees(Div div) {
        div.getStyle().set("display", "flex");
        div.getStyle().set("flex-direction", "column");
        div.getStyle().set("justify-content", "flex-start");

        int num = loginService.count();

        Label subtext = new Label("Organisation Size");
        subtext.getStyle().set("font-size", "14px");
        subtext.getStyle().set("color", "grey");
        Label value = new Label(""+num);
        value.getStyle().set("font-size","50px");


        div.add(subtext, value);    

        return div;
    }

    private Div createTasksCompletionRate(Div div) {
        div.getStyle().set("display", "flex");
        div.getStyle().set("flex-direction", "column");
        div.getStyle().set("justify-content", "flex-start");

        double roundedRate = taskService.calculateOnTimeCompletionPercentageForCurrentWeek();
        double rate = Math.round(roundedRate * 100.0) / 100.0;        Label subtext = new Label("On-Time Task Completion");
        subtext.getStyle().set("font-size", "14px");
        subtext.getStyle().set("color", "grey");
        Label value = new Label(rate+"%");
        value.getStyle().set("font-size","50px");


        div.add(subtext, value);    

        return div;
    }

    private Div createLateArrivals(Div div) {
        div.getStyle().set("display", "flex");
        div.getStyle().set("flex-direction", "column");
        div.getStyle().set("justify-content", "flex-start");

        int countLate = checkInOutService.countLateCheckInsForCurrentWeek();

        Label subtext = new Label("Late Arrivals");
        subtext.getStyle().set("font-size", "14px");
        subtext.getStyle().set("color", "grey");
        Label value = new Label(""+countLate);
        value.getStyle().set("font-size","50px");


        div.add(subtext, value);    

        return div;
    }

    private Div createTasksBeyondDeadline(Div div) {
        div.getStyle().set("display", "flex");
        div.getStyle().set("flex-direction", "column");
        div.getStyle().set("justify-content", "flex-start");

        int countLate = taskService.countTasksBeyondDeadlineForCurrentWeek();

        Label subtext = new Label("Tasks Beyond Schedule");
        subtext.getStyle().set("font-size", "14px");
        subtext.getStyle().set("color", "grey");
        Label value = new Label(""+countLate);
        value.getStyle().set("font-size","50px");


        div.add(subtext, value);    

        return div;
    }
    
}
