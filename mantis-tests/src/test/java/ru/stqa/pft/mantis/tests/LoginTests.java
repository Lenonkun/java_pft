package ru.stqa.pft.mantis.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.mantis.appmanager.HttpSession;

import java.io.IOException;

public class LoginTests extends TestBase{
    @Test
    public void testLogin() throws IOException {
        HttpSession session = app.newSession();

        Assert.assertTrue( session.login("administrator","Ww123456"));
        Assert.assertTrue(session.isLoggedInAs("administrator"));
    }
}
