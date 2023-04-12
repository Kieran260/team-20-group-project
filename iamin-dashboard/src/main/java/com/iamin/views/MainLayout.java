package com.iamin.views;

import com.iamin.components.appnav.AppNav;
import com.iamin.components.appnav.AppNavItem;
import com.iamin.data.entity.Login;
import com.iamin.data.service.LoginService;
import com.iamin.security.AuthenticatedUser;
import com.iamin.views.dashboard.DashboardView;
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
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.map.configuration.View;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.io.ByteArrayInputStream;
import java.util.Optional;

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

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    @Autowired
    private LoginService loginService;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker,
            LoginService loginService) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        this.loginService = loginService;
        this.addClassName("main-layout");

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("IAMIN");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get user's role and set the app name accordingly
        String userRole = getUserRole(authentication);
        
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
            nav.addItem(new AppNavItem("Add Employee", CreateEmployeeView.class, "la la-user-plus"));
        }
        if (accessChecker.hasAccess(ViewRequestsView.class)) {
            nav.addItem(new AppNavItem("View Requests", ViewRequestsView.class, "la la-question-circle"));

        }

        if (accessChecker.hasAccess(TimetableView.class)){
            nav.addItem(new AppNavItem("Timetable", TimetableView.class, "la la-calendar"));
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
}
