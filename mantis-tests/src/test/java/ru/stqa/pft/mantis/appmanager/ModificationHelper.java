package ru.stqa.pft.mantis.appmanager;

import org.openqa.selenium.By;

public class ModificationHelper extends HelperBase {
    public ModificationHelper (ApplicationManager app) {
        super(app);
    }
    public void start(String userName, String password) {
        wd.get(app.getProperty("web.baseUrl") + "login_page.php");
        type(By.name("username"),userName);
        type(By.name("password"),password);
        click(By.cssSelector("input[type='submit']"));
    }
    public void selectUser(String user) {
        click(By.cssSelector("#manage-menu > ul > li:nth-child(1) > a"));
        type(By.name("username"),user);
        click(By.cssSelector("form[id='manage-user-edit-form']>fieldset>input[type='submit']"));
    }
    public void resetUserPassword(String user) {
        selectUser(user);
        resetPassword();
    }
    private void resetPassword() {
        click(By.cssSelector("form[id='manage-user-reset-form']>fieldset>span>input[type='submit']"));
    }
}
