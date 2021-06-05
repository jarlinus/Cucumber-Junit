package net.cucumber.demo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class GoogleTranslatePage {

    WebDriver driver;
    String URL = "https://translate.google.ie/?hl=en";

    public GoogleTranslatePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isLoaded() {
        return driver.getCurrentUrl().contains(URL);
    }

    public void enterText(String wordToBeTranslated) {
        driver.findElement(By.xpath("//textarea[@jsname=\"BJE2fc\"]")).sendKeys(wordToBeTranslated);
    }

    public String getTranslatedWord() {
        return driver.findElement(By.xpath("//div[@class='J0lOec']")).getText();
    }
}
