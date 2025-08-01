package net.cucumber.demo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class GoogleTranslatePage {
    private static final Logger logger = LoggerFactory.getLogger(GoogleTranslatePage.class);
    
    private final WebDriver driver;
    private final String URL = "https://translate.google.ie/?hl=en";
    private final WebDriverWait wait;

    // Modern locators - more stable selectors
    @FindBy(css = "textarea[jsname='BJE2fc']")
    private WebElement sourceTextArea;
    
    @FindBy(css = "[data-result-index='0'] .J0lOec")
    private WebElement translatedText;

    public GoogleTranslatePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    public boolean isLoaded() {
        try {
            logger.info("Checking if Google Translate page is loaded");
            return wait.until(ExpectedConditions.urlContains("translate.google"));
        } catch (Exception e) {
            logger.error("Failed to verify if Google Translate page is loaded", e);
            return false;
        }
    }

    public void enterText(String wordToBeTranslated) {
        logger.info("Entering text for translation: {}", wordToBeTranslated);
        try {
            WebElement textArea = wait.until(ExpectedConditions.elementToBeClickable(sourceTextArea));
            textArea.clear();
            textArea.sendKeys(wordToBeTranslated);
        } catch (Exception e) {
            logger.error("Failed to enter text for translation", e);
            throw new RuntimeException("Unable to enter text in translation field", e);
        }
    }

    public String getTranslatedWord() {
        logger.info("Getting translated text");
        try {
            WebElement translatedElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("[data-result-index='0'] .J0lOec")));
            
            // Wait for translation to appear (text should not be empty)
            wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(
                By.cssSelector("[data-result-index='0'] .J0lOec"), "")));
            
            String translatedText = translatedElement.getText();
            logger.info("Retrieved translated text: {}", translatedText);
            return translatedText;
        } catch (Exception e) {
            logger.error("Failed to get translated text", e);
            throw new RuntimeException("Unable to retrieve translated text", e);
        }
    }
}
