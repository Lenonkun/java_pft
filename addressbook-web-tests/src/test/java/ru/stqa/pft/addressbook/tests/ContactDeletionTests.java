package ru.stqa.pft.addressbook.tests;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

public class ContactDeletionTests extends TestBase {
    @BeforeMethod
    public void ensurePreconditions() {
        app.contact().goToHomePage();
        if (app.db().contacts().size() == 0) {
            app.contact().create(new ContactData()
                    .withFname("ivan4").withMname("ivanovich").withLname("ivanov").withAddress("123")
                    .withMobile("999").withEmail("123@ya.ru").withBday("11").withBmonth("January")
                    .withByear("1990").withAddress2("123123"), true);
        }
    }

    @Test
    public void testDeletionContact() {
        Contacts before = app.db().contacts();
        ContactData deletedContact = before.iterator().next();

        app.contact().delete(deletedContact);

        Assert.assertEquals(app.contact().count(), before.size() - 1);
        Contacts after = app.db().contacts();

        MatcherAssert.assertThat(after, CoreMatchers.equalTo(before.without(deletedContact)));
        verifyContactsListInUI();
    }
}
