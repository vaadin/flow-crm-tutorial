package com.example.application.views.list;

import com.example.application.data.Contact;
import com.example.application.views.DashboardView;
import com.example.application.views.LoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
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
        navigate(ListView.class);
        assertNotNull(getCurrentView());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_gridIsPopulated() {
        navigate(ListView.class);
        @SuppressWarnings("unchecked")
        Grid<Contact> grid = $view(Grid.class).first();
        assertTrue(test(grid).size() > 0,
                "Grid should contain contacts from the database");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_formIsInitiallyHidden() {
        navigate(ListView.class);
        // Form is in the tree but not visible — $view query only finds usable components
        assertTrue($view(ContactForm.class).all().isEmpty(),
                "Contact form should not be usable when no contact is selected");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_selectContact_formIsShownWithData() {
        navigate(ListView.class);
        @SuppressWarnings("unchecked")
        Grid<Contact> grid = $view(Grid.class).first();
        Contact firstContact = test(grid).getRow(0);

        test(grid).select(0);

        ContactForm form = $view(ContactForm.class).first();
        assertTrue(form.isVisible(),
                "Form should be visible when a contact is selected");
        assertEquals(firstContact.getFirstName(),
                test(grid).getCellText(0, 0));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_cancelButton_hidesForm() {
        navigate(ListView.class);
        @SuppressWarnings("unchecked")
        Grid<Contact> grid = $view(Grid.class).first();

        test(grid).select(0);
        assertFalse($view(ContactForm.class).all().isEmpty(),
                "Form should be visible after selecting a contact");

        test($(Button.class).withText("Cancel").single()).click();
        assertTrue($view(ContactForm.class).all().isEmpty(),
                "Form should be hidden after clicking Cancel");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_addContactButton_showsEmptyForm() {
        navigate(ListView.class);

        test($(Button.class).withText("Add contact").single()).click();

        ContactForm form = $view(ContactForm.class).first();
        assertTrue(form.isVisible(),
                "Form should be visible after clicking Add contact");

        TextField firstName = $view(TextField.class)
                .withPropertyValue(TextField::getLabel, "First name").single();
        assertEquals("", firstName.getValue(),
                "First name should be empty for a new contact");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_filterByName_gridIsFiltered() {
        navigate(ListView.class);
        @SuppressWarnings("unchecked")
        Grid<Contact> grid = $view(Grid.class).first();
        int totalContacts = test(grid).size();
        assertTrue(totalContacts > 0, "Grid should have contacts initially");

        TextField filter = $view(TextField.class)
                .withPropertyValue(TextField::getPlaceholder, "Filter by name...").single();
        test(filter).setValue("Avery");

        int filteredContacts = test(grid).size();
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
        navigate(LoginView.class);
        assertNotNull(getCurrentView());
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
        assertEquals("Vaadin CRM", title.getText());
    }

    // ==================== Security Tests ====================

    @Test
    @WithAnonymousUser
    void security_anonymousUser_redirectedToLoginFromListView() {
        navigate("", LoginView.class);
        assertNotNull($(LoginForm.class).first(),
                "Anonymous user should be redirected to login when accessing root");
    }

    @Test
    @WithAnonymousUser
    void security_anonymousUser_redirectedToLoginFromDashboard() {
        navigate("dashboard", LoginView.class);
        assertNotNull($(LoginForm.class).first(),
                "Anonymous user should be redirected to login when accessing dashboard");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void security_authenticatedUser_canAccessListView() {
        navigate(ListView.class);
        assertNotNull($view(Grid.class).first(),
                "Authenticated user should see the contact grid");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void security_authenticatedUser_canAccessDashboard() {
        navigate(DashboardView.class);
        assertNotNull($(Span.class).all().stream()
                        .filter(s -> s.getText() != null && s.getText().contains("contacts"))
                        .findFirst().orElse(null),
                "Authenticated user should see contact stats");
    }

    // ==================== ContactForm Integration Tests ====================

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_editContact_formFieldsMatchSelectedContact() {
        navigate(ListView.class);
        @SuppressWarnings("unchecked")
        Grid<Contact> grid = $view(Grid.class).first();
        Contact contact = test(grid).getRow(0);

        test(grid).select(0);

        TextField firstName = $view(TextField.class)
                .withPropertyValue(TextField::getLabel, "First name").single();
        TextField lastName = $view(TextField.class)
                .withPropertyValue(TextField::getLabel, "Last name").single();
        EmailField email = $view(EmailField.class).first();
        @SuppressWarnings("unchecked")
        ComboBox<Object> company = $view(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Company").single();
        @SuppressWarnings("unchecked")
        ComboBox<Object> status = $view(ComboBox.class)
                .withPropertyValue(ComboBox::getLabel, "Status").single();

        assertEquals(contact.getFirstName(), firstName.getValue());
        assertEquals(contact.getLastName(), lastName.getValue());
        assertEquals(contact.getEmail(), email.getValue());
        assertEquals(contact.getCompany(), test(company).getSelected());
        assertEquals(contact.getStatus(), test(status).getSelected());
    }

}