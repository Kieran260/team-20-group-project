package com.iamin.views;

import com.iamin.components.appnav.AppNav;
import com.iamin.components.appnav.AppNavItem;
import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.Events;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.entity.Tasks;
import com.iamin.data.service.LoginService;
import com.iamin.data.service.SamplePersonService;
import com.iamin.data.service.TasksService;
import com.iamin.data.service.CheckInOutService;
import com.iamin.data.service.EventService;
import com.iamin.data.Role;
import com.iamin.security.AuthenticatedUser;
import com.iamin.views.dashboard.DashboardView;
import com.iamin.views.documentEmployeeView.DocumentsEmployeeView;
import com.iamin.views.documentUploadView.DocumentUploadView;
import com.iamin.views.login.LoginView;
import com.iamin.views.manageemployees.ManageEmployeesView;
import com.iamin.views.viewRequests.ViewRequestsView;
import com.iamin.views.CreateEmployeeView.CreateEmployeeView;
import com.iamin.views.ManageEvents.ManageEventsView;
import com.iamin.views.dashboard.DashboardView;
import com.iamin.views.manageemployees.ManageEmployeesView;
import com.iamin.views.timetable.TimetableView;
import com.iamin.views.manageRequests.ManageRequestsView;
import com.iamin.views.manageTasks.ManagerTasksView;
import com.iamin.views.viewtasks.EmployeeViewTasks;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.map.configuration.View;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;
    private H2 label0;
    private H2 label1;
    private H2 label2;
    private H2 label3;

    // manager only
    private double todayAttendance;

    // manager show all tasks due today, employee show only tasks currently in progress
    private int todayTasks;
    private int tasksInProgress;

    // manager and employee show events today
    private int todayEvents;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    @Autowired
    private LoginService loginService;

    @Autowired
    private CheckInOutService checkInOutService;

    @Autowired
    private TasksService taskService;

    @Autowired
    private EventService eventsService;

    @Autowired
    private SamplePersonService samplePersonService;

    private Authentication authentication;
    private String userRole;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker,
            LoginService loginService) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        this.loginService = loginService;
        this.addClassName("main-layout");

        
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        userRole = getUserRole(authentication);

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");
    
        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        viewTitle.getStyle().set("min-width","180px");


        label0 = new H2();
        label0.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.Margin.NONE);


        // Create the labels and divs
        Span icon1 = new Span();
        icon1.addClassNames("la", "la-user-check");
        label1 = new H2();
        label1.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.Margin.NONE);
        HorizontalLayout div1 = new HorizontalLayout(icon1, label1);
    
        Span icon2 = new Span();
        icon2.addClassNames("la", "la-clock-o");
        label2 = new H2();
        label2.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.Margin.NONE);
        HorizontalLayout div2 = new HorizontalLayout(icon2, label2);
    
        Span icon3 = new Span();
        icon3.addClassNames("la", "la-calendar");
        label3 = new H2();
        label3.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.Margin.NONE);
        HorizontalLayout div3 = new HorizontalLayout(icon3, label3);
    
        // Create a FlexLayout containing the labels
        FlexLayout labelsLayout = new FlexLayout();
        labelsLayout.getStyle().set("margin-right", "20px");
        labelsLayout.getStyle().set("gap", "30px");
    
        labelsLayout.setJustifyContentMode(JustifyContentMode.END); 

        try {
            if ("ROLE_ADMIN".equals(userRole)) {
                labelsLayout.add(label0, div1, div2, div3);
            } else if ("ROLE_USER".equals(userRole)) {
                labelsLayout.add(label0, div2, div3);
            } 
        } catch (NullPointerException e) {
            UI.getCurrent().navigate(LoginView.class);
        }

        // Create a Div container for the FlexLayout
        Div container = new Div(labelsLayout);
        container.setWidth("100%"); 
        container.setClassName("hide-on-small-screen");

        // Add the components to the navbar
        addToNavbar(true, toggle, viewTitle, container);

        Element styleElement = new Element("style");
        styleElement.setText("@media (max-width: 650px) { .hide-on-small-screen { display: none; } }");
        getElement().appendChild(styleElement);
    }
    

    private void addDrawerContent() {
        H1 appName = new H1("IAMIN");

        
        // Try to get the user's name from the database and catch the exception if the user is not found
        try {
            if ("ROLE_ADMIN".equals(userRole)) {
                appName = new H1("IAMIN Manager");
    
            } else if ("ROLE_USER".equals(userRole)) {
                appName = new H1("IAMIN Employee");
            } 
        } catch (NullPointerException e) {
            UI.getCurrent().navigate(LoginView.class);
        }

        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();

        if (accessChecker.hasAccess(DashboardView.class)) {
            nav.addItem(new AppNavItem("Dashboard", DashboardView.class, "la la-home"));
        }
        
        if (accessChecker.hasAccess(ManageEmployeesView.class)) {
            nav.addItem(new AppNavItem("Manage Employees", ManageEmployeesView.class, "la la-user"));
        }

        if (accessChecker.hasAccess(ManageRequestsView.class)) {
            nav.addItem(new AppNavItem("Manage Requests", ManageRequestsView.class, "la la-question-circle"));
        }
        if (accessChecker.hasAccess(EmployeeViewTasks.class)) {
            nav.addItem(new AppNavItem("View Tasks", EmployeeViewTasks.class, "la la-tasks"));
        }
       
        if (accessChecker.hasAccess(ManagerTasksView.class)) {
            nav.addItem(new AppNavItem("Manage Tasks", ManagerTasksView.class, "la la-tasks"));
        }

        if (accessChecker.hasAccess(ManageEventsView.class)){
            nav.addItem(new AppNavItem("Manage Events", ManageEventsView.class, "la la-users"));
        }

        if (accessChecker.hasAccess(CreateEmployeeView.class)){
            nav.addItem(new AppNavItem("Create Account", CreateEmployeeView.class, "la la-user-plus"));
        }
        if (accessChecker.hasAccess(ViewRequestsView.class)) {
            nav.addItem(new AppNavItem("View Requests", ViewRequestsView.class, "la la-question-circle"));

        }

        if (accessChecker.hasAccess(TimetableView.class)){
            nav.addItem(new AppNavItem("Timetable", TimetableView.class, "la la-calendar"));
        }
        if (accessChecker.hasAccess(DocumentUploadView.class)){
            nav.addItem(new AppNavItem("Upload Documents", DocumentUploadView.class, "la la-copy"));
        }
        if (accessChecker.hasAccess(DocumentsEmployeeView.class)){
            nav.addItem(new AppNavItem("View Documents", DocumentsEmployeeView.class, "la la-copy"));
        }

        


        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<Login> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            Login login = maybeUser.get();

            Avatar avatar = new Avatar(login.getUsername());

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);

            try {
                Optional<String> personNameOptional = loginService.getPersonNameByUsername(login.getUsername());
                String personName = personNameOptional.get();
                div.add(personName);
            } catch (EntityNotFoundException e) {
                div.add(login.getUsername());

            }
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());

        // Update header stats
        updateAttendance();
        updateTodayTasks();
        updateTodayEvents();
        updateTasksInProgress();


        try {
            if ("ROLE_ADMIN".equals(userRole)) {
                label0.setText("Today");
                label1.setText("Attendance: " + todayAttendance + "%");
                label2.setText("Tasks Due: " + todayTasks);
                label3.setText("Events: " + todayEvents);    
            } else if ("ROLE_USER".equals(userRole)) {
                label0.setText("Today");
                label2.setText("Tasks In Progress: " + tasksInProgress);
                label3.setText("Events: " + todayEvents);            } 
        } catch (NullPointerException e) {
            UI.getCurrent().navigate(LoginView.class);
        }
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    private String getUserRole(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities() != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority.getAuthority();
                if (role != null && role.startsWith("ROLE_")) {
                    return role;
                }
            }
        }
        return null;
    }


    public void updateAttendance() {
        LocalDate today = LocalDate.now();
    
        // Get the list of CheckInOut for today
        List<CheckInOut> checkInOutList = Collections.emptyList();
        try {
            checkInOutList = checkInOutService.findByDateBetween(today, today);
            System.out.println("CheckInOut List: " + checkInOutList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    
        // Count the number of checked-in employees
        int checkedInEmployees = 0;
        for (CheckInOut checkInOut : checkInOutList) {
            if (checkInOut.getcheckInTime() != null) {
                // Count the number of employees who checked in
                checkedInEmployees++;
            }
        }

        System.out.println("Checked-in Employees: " + checkedInEmployees);
    
        // Get the total number of employees
        List<Login> allLogins = loginService.findAll();
        List<Login> filteredLogins = new ArrayList<>();
    
        for (Login login : allLogins) {
            Set<Role> roles = login.getRoles();
            if (roles.contains(Role.USER) && !roles.contains(Role.ADMIN)) {
                filteredLogins.add(login);
            }
            System.out.println("Login: " + login.getUsername() + ", Roles: " + roles);
        }
    
        int totalEmployees = filteredLogins.size();
        System.out.println("Total Employees: " + totalEmployees);
    
        // Calculate the attendance percentage
        double attendancePercentage = (double) checkedInEmployees / totalEmployees * 100;
        attendancePercentage = (double) Math.round(attendancePercentage * 10) / 10;
    
        todayAttendance = attendancePercentage;
        System.out.println("Attendance: " + todayAttendance + "%");
    }

    private void updateTodayTasks() {
        List<Tasks> taskList = Collections.emptyList();
        try {
            taskList = taskService.findTasksDueToday();
            System.out.println("Task List: " + taskList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        todayTasks = taskList.size();
    }

    private void updateTodayEvents() {
        List<Events> eventList = Collections.emptyList();
        try {
            SamplePerson currentPerson = authenticatedUser.get().get().getPerson();
            eventList = eventsService.findEventsTodayForPerson(currentPerson);
            System.out.println("Event List: " + eventList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        todayEvents = eventList.size();
    }
    

    private void updateTasksInProgress() {
        List<Tasks> taskList = Collections.emptyList();
        try {
            SamplePerson currentPerson = authenticatedUser.get().get().getPerson();
            taskList = taskService.findTasksInProgress(currentPerson);
            System.out.println("Task List: " + taskList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        tasksInProgress = taskList.size();
    }

}

