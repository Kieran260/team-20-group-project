package com..views;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com..components.appnav.AppNav;
import com..components.appnav.AppNavItem;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com..views.MainLayout;
import com..views.dashboard.DashboardView;
import com..views.manageemployees.ManageEmployeesView;
import com..views.manageevents.ManageEventsView;
import com..views.calendar.CalendarView;
import com..views.login.LoginView;
import com.vaadin.flow.component.avatar.Avatar;
import com..data.entity.User;
import com..security.AuthenticatedUser;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import com.vaadin.flow.component.icon.Icon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private AuthenticatedUser authenticatedUser;
private AccessAnnotationChecker accessChecker;

public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
           this.authenticatedUser = authenticatedUser;
           this.accessChecker = accessChecker;

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
    nav.addItem(new AppNavItem("Manage Employees", ManageEmployeesView.class, "la la-columns"));

}
if (accessChecker.hasAccess(ManageEventsView.class)) {
    nav.addItem(new AppNavItem("Manage Events", ManageEventsView.class, "la la-columns"));

}
if (accessChecker.hasAccess(CalendarView.class)) {
    nav.addItem(new AppNavItem("Calendar", CalendarView.class, "la la-calendar"));

}


        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
if (maybeUser.isPresent()) {
    User user = maybeUser.get();

    Avatar avatar = new Avatar(user.getName());
    StreamResource resource = new StreamResource("profile-pic", () -> new ByteArrayInputStream(user.getProfilePicture()));
    avatar.setImageResource(resource);
    avatar.setThemeName("xsmall");
    avatar.getElement().setAttribute("tabindex", "-1");

    MenuBar userMenu = new MenuBar();
    userMenu.setThemeName("tertiary-inline contrast");

    MenuItem userName = userMenu.addItem("");
    Div div = new Div();
    div.add(avatar);
    div.add(user.getName());
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
}
