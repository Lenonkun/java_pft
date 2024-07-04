package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.HashSet;
import java.util.Set;

public class RemoveContactFromGroup extends TestBase{
    @BeforeMethod
    public void ensurePreconditions(){
        app.contact().goToHomePage();
        if (app.db().contacts().size()==0) {
            app.contact().create(new ContactData()
                    .withFname("ivan4").withMname("ivanovich").withLname("ivanov").withAddress("123")
                    .withMobile("999").withEmail("123@ya.ru").withBday("11").withBmonth("January")
                    .withByear("1990").withAddress2("123123"), true);
        }
        if (app.db().groups().size()==0){
            app.goTo().groupPage();
            app.group().create(new GroupData().withName("group1"));
        }

    }
    @Test
    public void testRemoveContactFromGroup() {
        Contacts beforeContacts = app.db().contacts();
        Groups beforeGroups= app.db().groups();
        GroupData selectGroup = beforeGroups.iterator().next();
        Set<ContactData> groupContacts = selectGroup.getContacts();

        ContactData contact = beforeContacts.stream().filter(c -> groupContacts.contains(c))
                .findFirst().orElse(null);//есть ли контакты в группе

        if (contact==null){//если нет, то добавляем
            contact = beforeContacts.iterator().next();
            app.contact().addToGroup(contact,selectGroup.getId());
        }

        app.contact().removeAContactWithGroup(contact,selectGroup); //и сразу удаляем

        //вытаскиваем этот контакт для проверок
        Contacts afterContacts = app.db().contacts();
        final ContactData fContact = contact;
        ContactData contactAfter = afterContacts.stream().filter(c -> c.getId()==fContact.getId())
                .findFirst().orElse(null);

        Set<GroupData> groupsInContact = contactAfter.getGroups();
//        System.out.println(groupsInContact);
//        System.out.println(selectGroup);
        Assert.assertTrue(!groupsInContact.contains(selectGroup));

    }
}
