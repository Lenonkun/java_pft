package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.util.HashSet;
import java.util.Set;

public class RemoveContactFromGroup extends TestBase{
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
