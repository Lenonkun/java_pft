package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import java.util.Set;

@Test
public class AddContactToGroup extends TestBase{

    @Test
    public void testAddContactToGroup() {
        Contacts beforeContacts = app.db().contacts();
        Groups beforeGroups= app.db().groups();
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

        app.contact().addToGroup(selectContact,group.getId());
        app.contact().goToHomePage();

        Groups afterGroups = app.db().groups();
        //final - вынужденная мера для лямбда
        final GroupData fGroup = group;
        GroupData addedGroup = afterGroups.stream().filter(g -> g.getId()== fGroup.getId())
                .findFirst().orElse(null);//найти группу для проверок ниже
        
        Set<ContactData> contactsInGroup = addedGroup.getContacts();
//        System.out.println("Group "+addedGroup);
//        System.out.println("should be a"+selectContact);
//        System.out.println("this? "+contactsInGroup);
        Assert.assertTrue(contactsInGroup.contains(selectContact));


    }
}
