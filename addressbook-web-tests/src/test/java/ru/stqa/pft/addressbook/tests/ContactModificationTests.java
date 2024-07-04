package ru.stqa.pft.addressbook.tests;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ContactModificationTests extends TestBase {
    @BeforeMethod
    public void ensurePreconditions(){
        app.contact().goToHomePage();
        if (app.db().contacts().size()==0) {
            app.contact().create(new ContactData()
                    .withFname("ivan4").withMname("ivanovich").withLname("ivanov").withAddress("123")
                    .withMobile("999").withEmail("123@ya.ru").withBday("11").withBmonth("January")
                    .withByear("1990").withAddress2("123123"), true);
        }
    }
    @Test
    public void testContactModification() {
        Contacts before = app.db().contacts();
        ContactData modifiedContact = before.iterator().next();
        ContactData contact = new ContactData().withId(modifiedContact.getId()).withFname("ivan1")
                .withMname("ivanov").withLname("ivanovich").withAddress("123").withMobile("999")
                .withEmail("123@ya.ru").withBday("11").withBmonth("January").withByear("1990")
                .withAddress2("123123");

        app.contact().modify(contact);

        Assert.assertEquals(app.contact().count(),before.size());
        app.contact().goToHomePage();
        Contacts after = app.db().contacts();

        MatcherAssert.assertThat(after, CoreMatchers.equalTo(
                before.without(modifiedContact).withAdded(contact)));
        verifyContactsListInUI();
    }

}