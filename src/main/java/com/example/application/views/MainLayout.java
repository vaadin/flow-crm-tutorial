package com.example.application.views;

import com.example.application.data.service.CrmService;
import com.example.application.security.SecurityService;
import com.example.application.views.list.ListView;
import nl.martijndwars.webpush.Subscription;

import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;
    private final CrmService crmService;

    private ToggleButton notifications;
    private Subscription clientSubscription;

    public MainLayout(SecurityService securityService, CrmService crmService) {
        this.securityService = securityService;
        this.crmService = crmService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        logo.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.MEDIUM);

        String u = securityService.getAuthenticatedUser().getUsername();
        notifications = new ToggleButton("Notifications");
        notifications.addValueChangeListener(event -> {
            if (event.getValue()) {
                // subscribe
                if(clientSubscription != null) {
                    System.out.println("Storing existing subscription for user");
                    crmService.addSubscription(u, clientSubscription);
                    clientSubscription = null;
                } else {
                    crmService.getWebPush().subscribe(event.getSource().getUI().get(), subscribe -> crmService.addSubscription(u, subscribe));
                }
            } else {
                if(crmService.hasMultipleSubscriptions(u)) {
                    // unsubscribe
                    System.out.println("Removing only db subscription");
                    // TODO:: Not removing subscription correctly from DB!!!
                    crmService.removeSubscription(u);
                } else {
                    System.out.println("Removing subscritpion");
                    crmService.getWebPush().unsubscribe(event.getSource().getUI().get(), subscribe -> crmService.removeSubscription(u));
                }
                // Manually remove sub
                // navigator.serviceWorker.getRegistration().then((reg) => reg.pushManager.getSubscription().then(sub => sub.unsubscribe()));
            }
        });
        // Manually check subscription on client console use:
        // navigator.serviceWorker.getRegistration().then((reg) => reg.pushManager.getSubscription().then(sub => console.log(sub)));
        Button logout = new Button("Log out " + u, e -> securityService.logout()); // <2>

        var header = new HorizontalLayout(new DrawerToggle(), logo, notifications, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo); // <4>
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);

    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("List", ListView.class),
                new RouterLink("Dashboard", DashboardView.class)
        ));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        String username = securityService.getAuthenticatedUser().getUsername();
        boolean hasSubscription = crmService.getSubscription(username) != null;

        crmService.getWebPush().subscriptionExists(attachEvent.getUI(), existsOnClient -> {
            if (!hasSubscription && existsOnClient) {
                // get client subscription, but do not activate as for wrong user.
                crmService.getWebPush().fetchExistingSubscription(attachEvent.getUI(), subscription -> clientSubscription = subscription);
            } else if (hasSubscription && existsOnClient) {
                // set active as user subscription and existing client subscription
                notifications.setValue(true);
            }
        });
    }
}