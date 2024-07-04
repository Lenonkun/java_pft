package ru.stqa.pft.addressbook.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


public class GroupDeletionTests extends TestBase {
    @BeforeMethod
    public void ensurePreconditions(){
        app.goTo().groupPage();
        if (app.db().groups().size()==0){
            app.group().create(new GroupData().withName("group1"));
        }

    }
    @Test
    public void testGroupDeletion() {
        Groups before = app.db().groups();
        GroupData deletedGroup = before.iterator().next();

        app.group().delete(deletedGroup);

        Assert.assertEquals(app.group().count(),before.size() - 1);
        Groups after = app.db().groups();

        assertThat(after,equalTo(before.without(deletedGroup)));
        verifyGroupListInUI();
    }
}