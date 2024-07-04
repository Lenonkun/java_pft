package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.Set;

@Test
public class AddContactToGroup extends TestBase {
    @BeforeMethod
    public void ensurePreconditions() {
        app.contact().goToHomePage();
        if (app.db().contacts().size() == 0) {
            app.contact().create(new ContactData()
                    .withFname("ivan4").withMname("ivanovich").withLname("ivanov").withAddress("123")
                    .withMobile("999").withEmail("123@ya.ru").withBday("11").withBmonth("January")
                    .withByear("1990").withAddress2("123123"), true);
        }
        if (app.db().groups().size() == 0) {
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("group1"));
        }

    }

    @Test
    public void testAddContactToGroup() {
        Contacts beforeContacts = app.db().contacts();
        Groups beforeGroups = app.db().groups();
        ContactData selectContact = beforeContacts.iterator().next();
        Set<GroupData> contactGroups = selectContact.getGroups();
        GroupData group = beforeGroups.stream().filter(g -> !contactGroups.contains(g))
                .findFirst().orElse(null);//перебор групп в которые входит контакт

        if (group == null) {//если таких нет - создать группу
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("New Group"));
            beforeGroups = app.db().groups();// обновить список

            group = beforeGroups.stream().filter(g -> !contactGroups.contains(g))
                    .findFirst().orElse(null);//выбрать новую группу
        }

        app.contact().addToGroup(selectContact, group.getId());
        app.contact().goToHomePage();

        Groups afterGroups = app.db().groups();
        //final - вынужденная мера для лямбда
        final GroupData fGroup = group;
        GroupData addedGroup = afterGroups.stream().filter(g -> g.getId() == fGroup.getId())
                .findFirst().orElse(null);//найти группу для проверок ниже

        Set<ContactData> contactsInGroup = addedGroup.getContacts();
//        System.out.println("Group "+addedGroup);
//        System.out.println("should be a"+selectContact);
//        System.out.println("this? "+contactsInGroup);
        Assert.assertTrue(contactsInGroup.contains(selectContact));


    }
}
