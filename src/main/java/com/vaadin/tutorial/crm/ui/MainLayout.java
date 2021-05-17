package com.vaadin.tutorial.crm.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.tutorial.crm.security.SecurityUtils;
import com.vaadin.tutorial.crm.ui.views.dashboard.DashboardView;
import com.vaadin.tutorial.crm.ui.views.home.HomeView;
import com.vaadin.tutorial.crm.ui.views.list.ListView;
import org.springframework.security.core.userdetails.UserDetails;

@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    private final SecurityUtils securityUtils;

    public MainLayout(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        logo.addClassName("logo");

        HorizontalLayout header;

        UserDetails user = securityUtils.getAuthenticatedUser();
        if (user != null) {
            Button logout = new Button("Log out", e -> securityUtils.logout());
            header = new HorizontalLayout(new DrawerToggle(), logo, logout);
        } else {
            header = new HorizontalLayout(new DrawerToggle(), logo);
        }
        header.addClassName("header");
        header.setWidth("100%");
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink homeLink = new RouterLink("Home", HomeView.class);
        homeLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                homeLink,
            new RouterLink("Contact List", ListView.class),
            new RouterLink("Dashboard", DashboardView.class)
        ));
    }
}
