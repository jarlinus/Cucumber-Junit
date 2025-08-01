package net.cucumber.demo.pages;

import net.cucumber.demo.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class GoogleHomePage {
    private static final Logger logger = LoggerFactory.getLogger(GoogleHomePage.class);
    
    private final WebDriver driver;
    private final String URL = Configuration.getInstance().getProperty("application.url");
    private final JavascriptExecutor js;
    private final WebDriverWait wait;

    public GoogleHomePage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "L2AGLb")
    private WebElement cookiesAgree;

    @FindBy(css = "[aria-label='Google apps']")
    private WebElement googleApps;

    @FindBy(xpath = "//span[text()='Translate']")
    private WebElement translate;

    public void navigateTo() {
        logger.info("Navigating to Google home page: {}", URL);
        driver.get(URL);
    }

    public boolean isLoaded() {
        try {
            return wait.until(ExpectedConditions.urlContains("google.com"));
        } catch (Exception e) {
            logger.error("Failed to verify if Google home page is loaded", e);
            return false;
        }
    }

    public void selectGoogleProducts() {
        logger.info("Clicking on Google apps menu");
        wait.until(ExpectedConditions.elementToBeClickable(googleApps)).click();
    }

    public void selectTranslate() {
        logger.info("Selecting Google Translate from apps menu");
        try {
            // Wait for the iframe to be available and switch to it
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
            
            // Wait for the translate element to be clickable and scroll it into view
            WebElement translateElement = wait.until(ExpectedConditions.elementToBeClickable(translate));
            js.executeScript("arguments[0].scrollIntoView(true);", translateElement);
            
            // Add a small delay for the scroll to complete
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            translateElement.click();
        } catch (Exception e) {
            logger.error("Failed to select Google Translate", e);
            throw new RuntimeException("Unable to navigate to Google Translate", e);
        }
    }

    public void acceptCookies() {
        logger.info("Accepting cookies");
        try {
            WebElement cookiesButton = wait.until(ExpectedConditions.elementToBeClickable(cookiesAgree));
            cookiesButton.click();
        } catch (Exception e) {
            logger.warn("Cookies dialog not found or already accepted");
        }
    }
}
