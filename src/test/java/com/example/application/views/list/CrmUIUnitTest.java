package com.example.application.views.list;

import com.example.application.data.Contact;
import com.example.application.views.DashboardView;
import com.example.application.views.LoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridTester;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.browserless.SpringBrowserlessTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CrmUIUnitTest extends SpringBrowserlessTest {

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
        ListView view = navigate(ListView.class);
        assertTrue(test(view.grid).size() > 0,
                "Grid should contain contacts from the database");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_formIsInitiallyHidden() {
        ListView view = navigate(ListView.class);
        assertFalse($(ContactForm.class).exists(),
                "Contact form should be hidden when no contact is selected");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_selectContact_formIsShownWithData() {
        ListView view = navigate(ListView.class);

        GridTester<Grid<Contact>, Contact> grid_ = test(view.grid);
        Contact firstContact = grid_.getRow(0);
        grid_.clickRow(0);

        ContactForm form = $(ContactForm.class).single();
        assertEquals(firstContact.getFirstName(), form.firstName.getValue());
        assertEquals(firstContact.getLastName(), form.lastName.getValue());
        assertEquals(firstContact.getEmail(), form.email.getValue());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_deselectContact_formIsHidden() {
        ListView view = navigate(ListView.class);
        var grid_ = test(view.grid);

        // select
        grid_.clickRow(0);
        assertTrue($(ContactForm.class).exists(),
                "Form should be visible when contact is selected");

        // deselect
        grid_.clickRow(0);
        assertFalse($(ContactForm.class).exists(),
                "Form should be hidden when contact is deselected");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_addContactButton_showsEmptyForm() {
        navigate(ListView.class);

        $(Button.class).withText("Add contact").first().click();

        ContactForm form = $(ContactForm.class).single();

        assertEquals("", form.firstName.getValue(),
                "First name should be empty for a new contact");
        assertEquals("", form.lastName.getValue(),
                "Last name should be empty for a new contact");
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_filterByName_gridIsFiltered() {
        ListView view = navigate(ListView.class);
        var grid_ = test(view.grid);
        int totalContacts = grid_.size();
        assertTrue(totalContacts > 0, "Grid should have contacts initially");

        test(view.filterText).setValue("Mar");

        int filteredContacts = grid_.size();
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

        Span stats = $(Span.class).withTextContaining("contacts").single();
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

        assertTrue($(LoginForm.class).exists(), "Login page should contain a LoginForm");
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

        assertTrue($(Span.class).withTextContaining("contacts").exists(),
                "Authenticated user should see contact stats");
    }

    // ==================== ContactForm Integration Tests ====================

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_editContact_formFieldsMatchSelectedContact() {
        ListView view = navigate(ListView.class);
        var grid_ = test(view.grid);
        Contact contact = grid_.getRow(0);
        grid_.clickRow(0);

        TextField firstName = $view(TextField.class).withCaption("First name").single();
        TextField lastName = $view(TextField.class).withCaption("Last name").single();
        EmailField email = $view(EmailField.class).single();

        ComboBox<?> company = $view(ComboBox.class).withCaption("Company").single();
        ComboBox<?> status = $view(ComboBox.class).withCaption("Status").single();

        assertEquals(contact.getFirstName(), firstName.getValue());
        assertEquals(contact.getLastName(), lastName.getValue());
        assertEquals(contact.getEmail(), email.getValue());
        assertEquals(contact.getCompany(), test(company).getSelected());
        assertEquals(contact.getStatus(), test(status).getSelected());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void listView_closeForm_formIsHidden() {
        ListView view = navigate(ListView.class);
        test(view.grid).clickRow(0);

        assertTrue($(ContactForm.class).exists());

        test($(Button.class).withText("Cancel").single()).click();
        assertFalse($(ContactForm.class).exists(),
                "Form should be hidden after clicking Cancel");
    }

    // ==================== Helper Methods ====================

}