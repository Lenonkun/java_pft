package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.appmanager.HttpSession;
import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.User;

import java.util.List;

import static org.testng.Assert.assertTrue;

public class ModificationUsers extends TestBase {
    @BeforeMethod
    public void startMailServer(){
        app.mail().start();
    }
    @Test
    public void testModificationUsers() throws Exception {
        //выбор юзера из БД
        User user = app.db().users().get(1);
        String userName = user.getUsername();
        String emailUser = user.getEmail();
        String passwordNewUser = "Ww123456";
        //авторизация под администратором
        app.modification().start("administrator","Ww123456");
        //сброс пароля выбранному пользователю
        app.modification().resetUserPassword(userName);
        //Ожидание писем
        List<MailMessage> mailMessages = app.mail().waitForMail(1, 20000);
        String confirmationLink = findConfirmationLink(mailMessages, emailUser);
        //переход по ссылке и смена пароля
        app.registration().finish(confirmationLink, passwordNewUser);
        //проверка авторизация под пользователем
        assertTrue(app.newSession().login(userName, passwordNewUser));
    }
    private String findConfirmationLink(List<MailMessage> mailMessages, String mail) {
        MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(mail)).findFirst().get();
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);
    }
    @AfterMethod(alwaysRun = true)
    public void stopMailSever(){
        app.mail().stop();
    }
}
