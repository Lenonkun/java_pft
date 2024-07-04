package ru.stqa.pft.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class RegistrationTests extends TestBase{
    @BeforeMethod
    public void startMailServer(){
        app.mail().start();
    }
    @Test
    public void testRegistration() throws MessagingException, IOException {
        long now = System.currentTimeMillis();
        String mail = String.format("user%s@localhost.localdomain", now);
        String user = String.format("user%s",now);
        String passwordNewUser = "password";
        app.registration().start(user, mail);
        List<MailMessage> mailMessages = app.mail().waitForMail(2, 20000);
        String confirmationLink = findConfirmationLink(mailMessages, mail);
        app.registration().finish(confirmationLink, passwordNewUser);

        assertTrue(app.newSession().login(user, passwordNewUser));

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
