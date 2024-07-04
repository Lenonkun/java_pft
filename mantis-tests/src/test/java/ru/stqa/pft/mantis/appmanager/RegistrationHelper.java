package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;

public class RegistrationHelper extends HelperBase {

    public RegistrationHelper(ApplicationManager app) {
        super(app);
    }

    public void start(String userName, String mail) {
        wd.get(app.getProperty("web.baseUrl") + "signup_page.php");
        type(By.name("username"),userName);
        type(By.name("email"),mail);
        click(By.cssSelector("input[type='submit']"));
    }

    public void finish(String confirmationLink, String password) {
        wd.get(confirmationLink);
        type(By.name("password"),password);
        type(By.name("password_confirm"),password);
        click(By.cssSelector("input[type='submit']"));
    }
}
