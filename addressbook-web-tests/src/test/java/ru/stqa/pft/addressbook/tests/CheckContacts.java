package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
@Test

public class CheckContacts extends TestBase{
    @BeforeMethod
    public void ensurePreconditions(){
        app.contact().goToHomePage();
        if (app.contact().all().size()==0) {
            app.contact().create(new ContactData()
                    .withFname("ivan4").withMname("ivanovich").withLname("ivanov").withAddress("123")
                    .withMobile("999").withEmail("123@ya.ru").withBday("11").withBmonth("January")
                    .withByear("1990").withAddress2("123123"), true);
        }
    }
    public void testContactCheck(){
        app.contact().goToHomePage();
        ContactData contact = app.contact().all().iterator().next();
        ContactData contactInfoFromEditForm = app.contact().infoFromEditForm(contact);

        assertThat(contact.getAllPhones(), equalTo(mergePhones(contactInfoFromEditForm)));

        assertThat(contact.getAddress(), equalTo(contactInfoFromEditForm.getAddress()));

        assertThat(contact.getAllEmails(), equalTo(mergeEmails(contactInfoFromEditForm)));

    }

    private String mergePhones(ContactData contact) {
        return Arrays.asList(contact.getHomePhone(), contact.getMobile(), contact.getWorkPhone())
                .stream().filter((s -> !s.equals(""))).collect(Collectors.joining("\n"));
    }
    private String mergeEmails(ContactData contact) {
        return Arrays.asList(contact.getEmail(), contact.getEmail2(), contact.getEmail3())
                .stream().filter((s -> !s.equals(""))).collect(Collectors.joining("\n"));
    }

}
