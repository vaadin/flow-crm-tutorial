package com.example.application.it;

import com.example.application.it.elements.LoginViewElement;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@RunLocally(Browser.FIREFOX)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginE2ETest {

    @Autowired
    Environment environment;

    static {
        // Prevent Vaadin Development mode to launch browser window
        System.setProperty("vaadin.launch-browser", "false");
    }

    static Playwright playwright = Playwright.create();
    private Page page;

    @BeforeEach
    void openBrowser() {
        page = playwright.chromium().launch().newPage();
        page.navigate("http://localhost:"
                + environment.getProperty("local.server.port") + "/");
    }

    @Test
    public void loginAsValidUserSucceeds() {
        LoginViewElement loginView = new LoginViewElement(page);
        assertTrue(loginView.login("user", "password"));
    }

    @Test
    public void loginAsInvalidUserFails() {
        LoginViewElement loginView = new LoginViewElement(page);
        assertFalse(loginView.login("user", "invalid"));
    }

}
