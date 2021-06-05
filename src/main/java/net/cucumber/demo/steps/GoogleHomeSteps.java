package net.cucumber.demo.steps;

import io.cucumber.java.en.Given;
import net.cucumber.demo.engine.TestContext;
import net.cucumber.demo.pages.GoogleHomePage;

import static org.junit.Assert.assertTrue;

public class GoogleHomeSteps {

    TestContext testContext;

    public GoogleHomeSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Given("user is in google home page")
    public void userIsInGoogleHomePage() {
        GoogleHomePage googleHomePage = new GoogleHomePage(testContext.getDriver());
        googleHomePage.navigateTo();
        assertTrue(googleHomePage.isLoaded());
    }
}
