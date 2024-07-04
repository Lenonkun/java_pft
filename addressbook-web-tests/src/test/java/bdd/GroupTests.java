package bdd;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "classpath:bdd", plugin = {"pretty","html:build/cucumber-report"})
public class GroupTests extends AbstractTestNGCucumberTests {


}
