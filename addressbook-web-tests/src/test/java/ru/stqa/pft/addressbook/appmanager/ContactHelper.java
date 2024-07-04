package ru.stqa.pft.addressbook.appmanager;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;

import java.util.List;

public class ContactHelper extends HelperBase {


    public ContactHelper(WebDriver wd) {
        super(wd);
    }

    public void create(ContactData address, boolean b) {
        goToNewContactPage();//переходим на ЭФ создание адреса
        fillContactForm(address, b);//заполнить форму
        submitContactCreation();
        contactsCache=null;//признак создания\редактирования контакта
        goToHomePage();
    }
    public void modify(ContactData contact) {
        initContactModification(contact.getId());
        fillContactForm(contact, false);
        submitContactModification();
        contactsCache=null;
        goToHomePage();
    }
    public void delete(ContactData delete) {
        selectContact(delete.getId());
        deleteSelectedContact();
        contactsCache=null;
        goToHomePage();
    }
    public void addToGroup(ContactData contact, Integer groupId) {
        selectContact(contact.getId());
        addToSelectedGroup(groupId);
        goToHomePage();



    }

    private void addToSelectedGroup(Integer groupId) {
        click(By.xpath("//select[@name='to_group']"));
        wd.findElement(By.cssSelector("select[name='to_group']>option[value='"+groupId+"']")).click();
        click(By.xpath("//input[@name='add']"));
    }

    public void removeAContactWithGroup(ContactData contact, GroupData selectGroup) {
        goToHomePage();
        click(By.xpath("//select[@name='group']"));
        wd.findElement(By.cssSelector("select[name='group']>option[value='"+selectGroup.getId()+"']")).click();
        selectContact(contact.getId());
        click(By.name("remove"));



    }


    public void goToHomePage() {
        click(By.linkText("home"));
    }

    public void goToNewContactPage() {
        click(By.linkText("add new"));
    }

    public void fillContactForm(ContactData contactData, boolean creation) {
        type(By.name("firstname"), contactData.getFirstname());
        type(By.name("middlename"), contactData.getMiddlename());
        type(By.name("lastname"), contactData.getLastname());
//        if (contactData.getPhoto()!=null){
//            attach(By.name("photo"), contactData.getPhoto());
//        }
        type(By.name("address"), contactData.getAddress());
        type(By.name("home"), contactData.getHomePhone());
        type(By.name("mobile"), contactData.getMobile());
        type(By.name("work"), contactData.getWorkPhone());
        type(By.name("email"), contactData.getEmail());
        type(By.name("email2"), contactData.getEmail2());
        type(By.name("email3"), contactData.getEmail3());
        if (contactData.getBday()!=null){
            select(By.name("bday"), contactData.getBday());
        }
        if (contactData.getBday()!=null){
            select(By.name("bmonth"), contactData.getBmonth());
        }
        type(By.name("byear"), contactData.getByear());

        if (creation == true&&contactData.getGroups().size()>0) {
//            select(By.name("new_group"), contactData.getGroup());
            new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroups().iterator().next().getName());
        }
        else {
//            Assert.assertFalse(isElementPresent(By.name("new_group")));
        }
        type(By.name("address2"), contactData.getAddress2());

    }


    public void submitContactCreation() {
        click(By.xpath("//*[@id=\"content\"]/form/input[21]"));
    }

    public void submitContactModification() {
        click(By.xpath("//*[@id=\"content\"]/form[1]/input[22]"));
    }


    public void selectContact(int id) {
        wd.findElement(By.cssSelector("input[value='"+id+"']")).click();
    }

    public void deleteSelectedContact() {
        click(By.xpath("//input[@value='Delete']"));
        if (isAlertPresent()) {
            Alert alert = wd.switchTo().alert();
            alert.accept();
        }
    }

    public void initContactModification(int id) {
        wd.findElement(By.cssSelector("tr[name='entry'] > td[class='center'] > a[href='edit.php?id="+id+"']")).click();
    }
    public void initContactView(int id) {
        wd.findElement(By.cssSelector("#maintable > tbody > tr:nth-child(2) > td:nth-child(7) > a")).click();
    }

    private Contacts contactsCache;

    //поиск всех контактов на ЭФ home
    public Contacts all() {
        if (contactsCache!=null){
            return contactsCache;
        }
        else
            contactsCache = new Contacts();
        List<WebElement> elements = wd.findElements(By.cssSelector("tr[name='entry']"));
        for (WebElement element : elements) {
            int id = Integer.parseInt( element.findElement(By.tagName("input")).getAttribute("value"));
            String fisrstName1 = element.findElement(By.cssSelector("td:nth-of-type(3)")).getText();
            String lastName1 = element.findElement(By.cssSelector("td:nth-of-type(2)")).getText();
            String address = element.findElement(By.cssSelector("td:nth-of-type(4)")).getText();
            String allEmails =element.findElement(By.cssSelector("td:nth-of-type(5)")).getText();
            String allPhones = element.findElement(By.cssSelector("td:nth-of-type(6)")).getText();

            contactsCache.add(new ContactData().withId(id).withFname(fisrstName1).withLname(lastName1)
                    .withAddress(address).withAllEmails(allEmails).withAllPhones(allPhones));
        }
        return contactsCache;
    }

    public boolean isThereContact() {
        return isElementPresent(By.xpath("/html/body/div[1]/div[4]/form[2]/table/tbody/tr[2]/td[1]/input"));
    }

    public int count() {
        return wd.findElements(By.name("selected[]")).size();
    }

    public ContactData infoFromEditForm(ContactData contact) {
        initContactModification(contact.getId());
        String firstName = wd.findElement(By.name("firstname")).getAttribute("value");
        String lastName = wd.findElement(By.name("lastname")).getAttribute("value");

        String address = wd.findElement(By.name("address")).getText();

        String home = cleaned(wd.findElement(By.name("home")).getAttribute("value"));//благодаря cleaned получаем сразу "чистое" значение телефонов
        String mobile = cleaned(wd.findElement(By.name("mobile")).getAttribute("value"));
        String work = cleaned(wd.findElement(By.name("work")).getAttribute("value"));

        String email = wd.findElement(By.name("email")).getAttribute("value");
        String email2 = wd.findElement(By.name("email2")).getAttribute("value");
        String email3 = wd.findElement(By.name("email3")).getAttribute("value");

        wd.navigate().back();
        return contact.withId(contact.getId()).withFname(firstName).withLname(lastName)
                .withAddress(address).withHomePhone(home).withMobile(mobile).withWorkPhone(work)
                .withEmail(email).withEmail2(email2).withEmail3(email3);
    }

    public ContactData infoFromViewForm(ContactData contact) {
        initContactView(contact.getId());//app.contact().all().iterator().next()
        WebElement element = wd.findElement(By.cssSelector("div[id = 'content']"));
        String firstName = element.getText().split("\n")[0].split(" ")[0];
        String lastName= element.getText().split("\n")[0].split(" ")[1];

        String address = element.getText().split("\n")[1];

        String home = cleaned(element.getText().split("\n")[3].split(":")[1]);
        String mobile = cleaned(element.getText().split("\n")[4].split(":")[1]);
        String work = cleaned(element.getText().split("\n")[5].split(":")[1]);

        String email = wd.findElement(By.xpath("//*[@id=\"content\"]/a[1]")).getText();
        String email2 = wd.findElement(By.xpath("//*[@id=\"content\"]/a[2]")).getText();
        String email3 = wd.findElement(By.xpath("//*[@id=\"content\"]/a[3]")).getText();

        wd.navigate().back();
        return contact.withId(contact.getId()).withFname(firstName).withLname(lastName)
                .withAddress(address).withHomePhone(home).withMobile(mobile).withWorkPhone(work)
                .withEmail(email).withEmail2(email2).withEmail3(email3);
    }

    public String cleaned(String phone){
        return phone.replaceAll("\\s","").replaceAll("[-()]","");
    }


}
