package com.example.application.it.elements;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginViewElement {

    public boolean login(String username, String password) {
        root.locator("input[name='username']").fill(username);
        root.locator("input[name='password']").fill(password);
        root.locator("vaadin-button").getByText("Log in").click();

        try {
            // Assert that main layout appears after login
            assertThat(root.page().locator("vaadin-app-layout")).isVisible();
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    private final Locator root;

    public LoginViewElement(Page page) {
        root = page.locator("vaadin-vertical-layout.login-view");
    }

}