package com.example.application.it;

import com.example.application.it.elements.LoginViewElement;
import com.vaadin.testbench.BrowserTest;
import com.vaadin.testbench.BrowserTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@RunLocally(Browser.FIREFOX)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginE2ETest extends BrowserTestBase {

    @Autowired
    Environment environment;

    static {
        // Prevent Vaadin Development mode to launch browser window
        System.setProperty("vaadin.launch-browser", "false");
    }

    @BeforeEach
    void openBrowser() {
        getDriver().get("http://localhost:" + environment.getProperty("local.server.port") + "/");
    }

    @BrowserTest
    public void loginAsValidUserSucceeds() {
        LoginViewElement loginView = $(LoginViewElement.class).onPage().first();
        assertTrue(loginView.login("user", "password"));
    }

    @BrowserTest
    public void loginAsInvalidUserFails() {
        LoginViewElement loginView = $(LoginViewElement.class).onPage().first();
        assertFalse(loginView.login("user", "invalid"));
    }

}
