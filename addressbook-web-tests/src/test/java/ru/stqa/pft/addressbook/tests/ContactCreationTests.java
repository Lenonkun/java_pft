package ru.stqa.pft.addressbook.tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.WildcardTypePermission;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.Adapder.FileTypeAdapter;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactCreationTests extends TestBase {
    @DataProvider
    public Iterator<Object[]> validContactsFromXml() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(
                "src/test/resources/contacts.xml")))) {
            String xml = "";
            String line = reader.readLine();
            while (line != null) {
                xml += line;
                line = reader.readLine();
            }
            XStream xstream = new XStream();
            // Разрешить классы из пакета
            xstream.addPermission(new WildcardTypePermission(new String[]{"ru.stqa.pft.addressbook.model.**"}));
            xstream.processAnnotations(ContactData.class);
            List<ContactData> contacts = (List<ContactData>) xstream.fromXML(xml);
            return contacts.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
        }
    }

    @DataProvider
    public Iterator<Object[]> validContactsFromJson() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(
                "src/test/resources/contacts.json")))) {
            StringBuilder json = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                json.append(line);
                line = reader.readLine();
            }
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(File.class, new FileTypeAdapter())
                    .create();
            List<ContactData> contacts = gson.fromJson(json.toString(), new TypeToken<List<ContactData>>() {}
                    .getType());
            return contacts.stream().map((g) -> new Object[]{g}).collect(Collectors.toList()).iterator();
        }
    }

    @Test(dataProvider = "validContactsFromJson")
    public void testCreationContact(ContactData contact) {
        app.contact().goToHomePage();
        Contacts before = app.db().contacts();
//        File photo = new File("src/test/resources/Photos_vjXZUhS7tQ.png");
        app.contact().create(contact, true);
        Assert.assertEquals(app.contact().count(), before.size() + 1);
        Contacts after = app.db().contacts();

        assertThat(after, equalTo(before.withAdded(contact.withId(after.stream().mapToInt(ContactData::getId).max().getAsInt()))));
        verifyContactsListInUI();
    }
}