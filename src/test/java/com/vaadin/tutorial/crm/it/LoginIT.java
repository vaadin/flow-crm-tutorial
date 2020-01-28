package com.vaadin.tutorial.crm.it;

import com.vaadin.tutorial.crm.it.elements.login.LoginViewElement;
import org.junit.Assert;
import org.junit.Test;

public class LoginIT extends AbstractTest {
    public LoginIT() {
        super("");
    }

    @Test
    public void loginAsValidUserSucceeds() {
        LoginViewElement loginView = $(LoginViewElement.class).onPage().first();
        Assert.assertTrue(loginView.login("user", "password"));
    }

    @Test
    public void loginAsInvalidUserFails() {
        LoginViewElement loginView = $(LoginViewElement.class).onPage().first();
        Assert.assertFalse(loginView.login("user", "invalid"));
    }


}
