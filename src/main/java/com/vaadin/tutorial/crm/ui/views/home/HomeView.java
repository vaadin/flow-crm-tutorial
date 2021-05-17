package com.vaadin.tutorial.crm.ui.views.home;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.tutorial.crm.ui.MainLayout;

@PageTitle("Vaadin CRM - Home")
@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends VerticalLayout {

    public HomeView() {
        H1 title = new H1("This is the home page which is available to everyone!");

        H3 description = new H3("By navigating to other pages you would be prompted to login.");

        Html credentials = new Html("<div> The following users are available: <ul>" +
                "<li>Username: <strong>user</strong>, Password: <strong>user</strong>, this is the normal user.</li>" +
                "<li>Username: <strong>admin</strong>, Password: <strong>admin</strong>, this is the administrator account.</li><div>");

        add(title, description, credentials);
    }
}
