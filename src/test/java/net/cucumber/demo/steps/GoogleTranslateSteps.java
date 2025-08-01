package net.cucumber.demo.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.cucumber.demo.engine.TestContext;
import net.cucumber.demo.pages.GoogleHomePage;
import net.cucumber.demo.pages.GoogleTranslatePage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoogleTranslateSteps {

    TestContext testContext;

    public GoogleTranslateSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("user translates {string} a French word to English")
    public void userTranslatesAFrenchWordToEnglish(String wordToBeTranslated) {
        GoogleHomePage googleHomePage = new GoogleHomePage(testContext.getDriver());
        googleHomePage.acceptCookies();
        googleHomePage.selectGoogleProducts();
        googleHomePage.selectTranslate();
        GoogleTranslatePage googleTranslatePage = new GoogleTranslatePage(testContext.getDriver());
        assertTrue(googleTranslatePage.isLoaded(), "Google Translate page should be loaded");
        googleTranslatePage.enterText(wordToBeTranslated);
    }

    @Then("the translated word {string} should be displayed")
    public void theTranslatedWordShouldBeDisplayed(String expectedTranslatedWord) {
        GoogleTranslatePage googleTranslatePage = new GoogleTranslatePage(testContext.getDriver());
        String actualTranslatedWord = googleTranslatePage.getTranslatedWord().trim();
        assertEquals(expectedTranslatedWord, actualTranslatedWord, 
            "Expected translated word does not match actual translated word");
    }
}
