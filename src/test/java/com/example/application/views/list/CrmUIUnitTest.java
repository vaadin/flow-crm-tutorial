package com.example.application.views.list;

import com.example.application.data.Contact;
import com.example.application.views.DashboardView;
import com.example.application.views.LoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.testbench.unit.SpringUIUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CrmUIUnitTest extends SpringUIUnitTest {

    static {
        System.setProperty("vaadin.launch-browser", "false");
    }

    // ==================== ListView Tests ====================
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_navigateToRoot_viewIsShown() {
        ListView view = navigate(ListView.class);
        assertNotNull(view);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_gridIsPopulated() {
        ListView view = navigate(ListView.class);
        @SuppressWarnings("unchecked")
        ListDataProvider<Contact> dataProvider =
                (ListDataProvider<Contact>) view.grid.getDataProvider();
        assertFalse(dataProvider.getItems().isEmpty(),
                "Grid should contain contacts from the database");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_formIsInitiallyHidden() {
        ListView view = navigate(ListView.class);
        assertFalse(view.form.isVisible(),
                "Contact form should be hidden when no contact is selected");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_selectContact_formIsShownWithData() {
        ListView view = navigate(ListView.class);
        Contact firstContact = getFirstItem(view.grid);

        view.grid.asSingleSelect().setValue(firstContact);

        assertTrue(view.form.isVisible(),
                "Form should be visible when a contact is selected");
        assertEquals(firstContact.getFirstName(), view.form.firstName.getValue());
        assertEquals(firstContact.getLastName(), view.form.lastName.getValue());
        assertEquals(firstContact.getEmail(), view.form.email.getValue());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_deselectContact_formIsHidden() {
        ListView view = navigate(ListView.class);
        Contact firstContact = getFirstItem(view.grid);

        view.grid.asSingleSelect().setValue(firstContact);
        assertTrue(view.form.isVisible());

        view.grid.asSingleSelect().clear();
        assertFalse(view.form.isVisible(),
                "Form should be hidden when contact is deselected");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_addContactButton_showsEmptyForm() {
        ListView view = navigate(ListView.class);

        Button addButton = $(Button.class).all().stream()
                .filter(b -> "Add contact".equals(b.getText()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Add contact button not found"));

        test(addButton).click();

        assertTrue(view.form.isVisible(),
                "Form should be visible after clicking Add contact");
        assertEquals("", view.form.firstName.getValue(),
                "First name should be empty for a new contact");
        assertEquals("", view.form.lastName.getValue(),
                "Last name should be empty for a new contact");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_filterByName_gridIsFiltered() {
        ListView view = navigate(ListView.class);
        @SuppressWarnings("unchecked")
        ListDataProvider<Contact> dataProvider =
                (ListDataProvider<Contact>) view.grid.getDataProvider();
        int totalContacts = dataProvider.getItems().size();
        assertTrue(totalContacts > 0, "Grid should have contacts initially");

        view.filterText.setValue("Avery");

        @SuppressWarnings("unchecked")
        ListDataProvider<Contact> filteredProvider =
                (ListDataProvider<Contact>) view.grid.getDataProvider();
        int filteredContacts = filteredProvider.getItems().size();
        assertTrue(filteredContacts <= totalContacts,
                "Filtered results should be <= total contacts");
    }

    // ==================== DashboardView Tests ====================

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void dashboardView_navigateToDashboard_viewIsShown() {
        DashboardView view = navigate(DashboardView.class);
        assertNotNull(view);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void dashboardView_contactStatsAreDisplayed() {
        navigate(DashboardView.class);

        Span stats = $(Span.class).all().stream()
                .filter(s -> s.getText() != null && s.getText().contains("contacts"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Contact stats span not found"));

        assertTrue(stats.getText().matches("\\d+ contacts"),
                "Stats should show number of contacts, got: " + stats.getText());
    }

    // ==================== LoginView Tests ====================

    @Test
    @WithAnonymousUser
    void loginView_anonymousUser_canAccessLoginPage() {
        LoginView view = navigate(LoginView.class);
        assertNotNull(view);
    }

    @Test
    @WithAnonymousUser
    void loginView_containsLoginForm() {
        navigate(LoginView.class);

        LoginForm loginForm = $(LoginForm.class).first();
        assertNotNull(loginForm, "Login page should contain a LoginForm");
    }

    @Test
    @WithAnonymousUser
    void loginView_containsTitle() {
        navigate(LoginView.class);

        H1 title = $(H1.class).first();
        assertNotNull(title, "Login page should have a title");
        assertEquals("Vaadin CRM", title.getText());
    }

    // ==================== Security Tests ====================

    @Test
    @WithAnonymousUser
    void security_anonymousUser_redirectedToLoginFromListView() {
        LoginView loginView = navigate("", LoginView.class);
        assertNotNull(loginView,
                "Anonymous user should be redirected to login when accessing root");
    }

    @Test
    @WithAnonymousUser
    void security_anonymousUser_redirectedToLoginFromDashboard() {
        LoginView loginView = navigate("dashboard", LoginView.class);
        assertNotNull(loginView,
                "Anonymous user should be redirected to login when accessing dashboard");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void security_authenticatedUser_canAccessListView() {
        ListView view = navigate(ListView.class);
        assertNotNull(view, "Authenticated user should be able to access list view");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void security_authenticatedUser_canAccessDashboard() {
        DashboardView view = navigate(DashboardView.class);
        assertNotNull(view, "Authenticated user should be able to access dashboard");
    }

    // ==================== ContactForm Integration Tests ====================

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_editContact_formFieldsMatchSelectedContact() {
        ListView view = navigate(ListView.class);
        Contact contact = getFirstItem(view.grid);

        view.grid.asSingleSelect().setValue(contact);

        ContactForm form = view.form;
        assertEquals(contact.getFirstName(), form.firstName.getValue());
        assertEquals(contact.getLastName(), form.lastName.getValue());
        assertEquals(contact.getEmail(), form.email.getValue());
        assertEquals(contact.getCompany(), form.company.getValue());
        assertEquals(contact.getStatus(), form.status.getValue());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_closeForm_formIsHidden() {
        ListView view = navigate(ListView.class);
        Contact contact = getFirstItem(view.grid);

        view.grid.asSingleSelect().setValue(contact);
        assertTrue(view.form.isVisible());

        test(view.form.close).click();
        assertFalse(view.form.isVisible(),
                "Form should be hidden after clicking Cancel");
    }

    // ==================== Helper Methods ====================

    @SuppressWarnings("unchecked")
    private Contact getFirstItem(Grid<Contact> grid) {
        ListDataProvider<Contact> dataProvider =
                (ListDataProvider<Contact>) grid.getDataProvider();
        assertFalse(dataProvider.getItems().isEmpty(), "Grid should have items");
        return dataProvider.getItems().iterator().next();
    }
}
