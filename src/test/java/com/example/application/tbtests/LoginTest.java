package com.example.application.tbtests;

import com.example.application.tbtests.elements.LoginViewElement;
import org.junit.Assert;
import org.junit.Test;

public class LoginTest extends AbstractTest {
    public LoginTest() {
        super("");
    }

    @Test
    public void loginAsValidUserSucceeds() {
        LoginViewElement loginView = $(LoginViewElement.class).onPage().first();
        Assert.assertTrue(loginView.login("user", "userpass"));
    }

    @Test
    public void loginAsInvalidUserFails() {
        LoginViewElement loginView = $(LoginViewElement.class).onPage().first();
        Assert.assertFalse(loginView.login("user", "invalid"));
    }
}
