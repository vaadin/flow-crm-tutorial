package com.example.application.it;

import com.example.application.it.elements.LoginViewElement;
import com.vaadin.flow.component.applayout.testbench.AppLayoutElement;
import com.vaadin.flow.component.login.testbench.LoginFormElement;
import com.vaadin.testbench.BrowserTest;
import com.vaadin.testbench.BrowserTestBase;
import com.vaadin.testbench.annotations.RunLocally;
import com.vaadin.testbench.parallel.Browser;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunLocally(Browser.FIREFOX)
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
