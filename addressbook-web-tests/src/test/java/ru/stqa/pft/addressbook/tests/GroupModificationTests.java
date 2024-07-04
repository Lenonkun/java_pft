package ru.stqa.pft.addressbook.tests;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.testng.Assert.*;


public class GroupModificationTests extends TestBase{

    @BeforeMethod
    public void ensurePreconditions(){
        if (app.db().groups().size()==0){
        app.goTo().groupPage();
        app.group().create(new GroupData().withName("group1"));
        }

    }
    @Test
    public void testGroupModification(){
        Groups before = app.db().groups();
        GroupData modifiedGroup=before.iterator().next();
        GroupData group = new GroupData()
                .withId(modifiedGroup.getId()).withName("group1").withFooter("group1").withHeader("group1");
        app.goTo().groupPage();
        app.group().modify(group);

        assertEquals(app.group().count(),before.size());
        Groups after = app.db().groups();
        assertThat(after, equalTo(before.without(modifiedGroup).withAdded(group)));

        verifyGroupListInUI();
    }


}