package com.iamin.views.AccountSettings;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.iamin.views.MainLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.PermitAll;

@PageTitle("AccountSettings")
@Route(value = "accSettings", layout = MainLayout.class)
@RouteAlias(value = "accSettings", layout = MainLayout.class)
@PermitAll
public class AccountSettings extends HorizontalLayout{
    
}
