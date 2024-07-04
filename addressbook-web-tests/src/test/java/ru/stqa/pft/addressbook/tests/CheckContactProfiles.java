package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Test
public class CheckContactProfiles extends TestBase {
    @BeforeMethod
    public void ensurePreconditions() {
        app.contact().goToHomePage();
        app.contact().create(new ContactData()
                .withFname("ivan").withLname("1111vanov").withAddress("123")
                .withHomePhone("7(77)").withMobile("99-9").withWorkPhone("88 8")
                .withEmail("1@ya.ru)").withEmail2("2@ya-ru").withEmail3("3@ya ru"), true);
    }

    public void testContactProfileCheck() {
        app.contact().goToHomePage();
        ContactData contact = app.contact().all().iterator().next();
        ContactData contactInfoFromViewForm = app.contact().infoFromViewForm(contact);

        assertThat(mergeContactInfo(contact), equalTo(mergeContactInfoForm(contactInfoFromViewForm)));

        app.contact().goToHomePage();
        app.contact().delete(contact);


        //Если проверять по отдельности на случай если в будущем понадобится
//        assertThat(contact.getFname(), equalTo(contactInfoFromViewForm.getFname()));
//        assertThat(contact.getMname(), equalTo(contactInfoFromViewForm.getMname()));
//        assertThat(contact.getAddress(), equalTo(contactInfoFromViewForm.getAddress()));
//        assertThat(contact.getAllEmails(), equalTo(mergeEmails(contactInfoFromViewForm)));
//        assertThat(contact.getAllPhones(), equalTo(mergePhones(contactInfoFromViewForm)));

    }

    private String mergeContactInfo(ContactData contact) {
        return Arrays.asList(contact.getFirstname(), contact.getLastname(), contact.getAddress()
                        , contact.getAllPhones(), contact.getAllEmails()).stream()
                .collect(Collectors.joining("\n"));
    }

    private String mergeContactInfoForm(ContactData contact) {
        return Arrays.asList(contact.getFirstname(), contact.getLastname(), contact.getAddress()
                        , contact.getHomePhone(), contact.getMobile(), contact.getWorkPhone()
                        , contact.getEmail(), contact.getEmail2(), contact.getEmail3()).stream()
                .collect(Collectors.joining("\n"));
    }

/*    private String mergePhones(ContactData contact) {
        return Arrays.asList(contact.getHomePhone(), contact.getMobile(), contact.getWorkPhone())
                .stream().filter((s -> !s.equals(""))).collect(Collectors.joining("\n"));
    }

    private String mergeEmails(ContactData contact) {
        return Arrays.asList(contact.getEmail(), contact.getEmail2(), contact.getEmail3())
                .stream().filter((s -> !s.equals(""))).collect(Collectors.joining("\n"));
    }*/



}
