package ru.stqa.pft.addressbook.appmanager;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.Browser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

    private final Properties properties;
    private final String browser;
    WebDriver wd;
    private GroupHelper groupHelper;
    private NavigationHelper navigationHelper;
    private SessionHelper sessionHelper;
    private ContactHelper addressHelper;
    private DbHelper dbHelper;

    public ApplicationManager(String browser) {
        this.browser = browser;
        properties = new Properties();
    }

    public void init() throws IOException {
        String target = System.getProperty("target", "local");
        properties.load(new FileReader(new File(String.format("src/test/resources/%s.properties", target))));
        dbHelper = new DbHelper();

        if (Objects.equals(browser, Browser.CHROME.browserName())) {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\IdeaProjects\\chromedriver-win64\\chromedriver.exe");
            wd = new ChromeDriver();
        } else if (Objects.equals(browser, Browser.EDGE.browserName())) {
//            System.setProperty("webdriver.edge.driver", "C:\\Users\\user\\IdeaProjects\\edgedriver_win64\\msedgedriver.exe");
            WebDriverManager.edgedriver().setup();
            wd = new EdgeDriver();
        } else if (Objects.equals(browser, Browser.IE.browserName())) {
            WebDriverManager.iedriver().setup();
            wd = new InternetExplorerDriver();
        }
        wd.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        wd.get(properties.getProperty("web.baseUrl"));

        groupHelper = new GroupHelper(wd);
        navigationHelper = new NavigationHelper(wd);
        addressHelper = new ContactHelper(wd);
        sessionHelper = new SessionHelper(wd);
        sessionHelper.login(properties.getProperty("web.login"), properties.getProperty("web.password"));

    }

    public void stop() {
        wd.findElement(By.linkText("Logout")).click();
        wd.quit();
    }

    public GroupHelper group() {
        return groupHelper;
    }

    public NavigationHelper goTo() {
        return navigationHelper;
    }

    public ContactHelper contact() {
        return addressHelper;
    }

    public DbHelper db() {
        return dbHelper;
    }

}
