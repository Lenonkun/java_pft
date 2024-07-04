package bdd;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.remote.Browser;
import org.testng.annotations.BeforeMethod;
import ru.stqa.pft.addressbook.appmanager.ApplicationManager;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GroupStepDefinitions {
    private ApplicationManager app;
    private Groups groups;
    private GroupData newGroup;

    @Before
    public void init() throws IOException {
        app= new ApplicationManager(System.getProperty("brouser", Browser.CHROME.browserName()));
        app.init();

    }
    @After
    public void stop(){
        app.stop();
        app=null;

    }



    @Given("a set of groups")
    public void loadGroups() {
        groups = app.db().groups();


    }

    @When("I create a new group with name {string}, header {string} and footer {string}")
    public void createGroup(String name, String header, String footer) {
         newGroup = new GroupData().withName(name).withHeader(header).withFooter(footer);
         app.goTo().groupPage();
        app.group().create(newGroup);
    }

    @Then("the new set of groups is equal to the old set with the added group")
    public void verifyGroupCreated() {
        Groups newGroups = app.db().groups();
        assertThat(newGroups, CoreMatchers.equalTo(
                groups.withAdded(newGroup.withId(newGroups.stream().mapToInt((g) -> g.getId()).max().getAsInt()))));

    }
}
