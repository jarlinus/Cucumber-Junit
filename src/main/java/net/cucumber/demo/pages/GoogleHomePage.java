package net.cucumber.demo.pages;

import net.cucumber.demo.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class GoogleHomePage {

    WebDriver driver;
    String URL = Configuration.getInstance().getProperty("application.url");
    JavascriptExecutor js;

    public GoogleHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        js = (JavascriptExecutor) driver;
    }

    @FindBy(id = "L2AGLb")
    private WebElement cookiesAgree;

    @FindBy(xpath = "//a[@aria-label='Google apps']")
    private WebElement googleApps;

    @FindBy(xpath = "//span[text()='Translate']")
    private WebElement translate;

    public void navigateTo() {
        driver.get(URL);
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains(URL);
    }

    public void selectGoogleProducts() {
        googleApps.click();
    }

    public void selectTranslate() {
        driver.switchTo().frame(0);
        List<WebElement> productList = driver.findElements(By.xpath("//ul[@jsname=\"k77Iif\"]/li/a/span"));
        js.executeScript("arguments[0].scrollIntoView();", translate);
        translate.click();
    }

    public void acceptCookies() {
        cookiesAgree.click();
    }
}
