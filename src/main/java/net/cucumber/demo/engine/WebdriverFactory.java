package net.cucumber.demo.engine;

import net.cucumber.demo.Configuration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.concurrent.TimeUnit;

public class WebdriverFactory {
    private static final String WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver";
    private static final String WEBDRIVER_FIREFOX_DRIVER = "webdriver.gecko.driver";
    private static final String WEBDRIVER_IE_DRIVER = "webdriver.ie.driver";
    private static WebdriverFactory instance = new WebdriverFactory();
    private Configuration configuration = Configuration.getInstance();
    private final String browserName = configuration.getProperty("browser");

    public static WebdriverFactory getInstance() {
        return instance;
    }

    public WebDriver getDriver() {
        return getDriver(getBrowserType(), null);
    }

    public WebDriver getDriver(String testName) {
        return getDriver(getBrowserType(), testName);
    }

    public WebDriver getDriver(DriverType browserType, String testName) {
        return createDriver(browserType, testName);
    }

    public DriverType getBrowserType() {
        switch (browserName) {
            case "local-chrome":
            case "chrome":
                return DriverType.CHROME;
            case "local-firefox":
            case "firefox":
                return DriverType.FIREFOX;
            case "local-ie":
            case "iexplorer":
            case "ie":
                return DriverType.INTERNETEXPLORER;
            default:
                throw new IllegalArgumentException("Browser Name Key value in Configuration.properties is not matched : " + browserName);
        }
    }


    private WebDriver createDriver(DriverType browserType, String testName) {
        RemoteWebDriver driver = null;
        switch (browserType) {
            case FIREFOX:
                System.setProperty(WEBDRIVER_FIREFOX_DRIVER, configuration.getProperty("webdriver.firefox.driver"));
                driver = new FirefoxDriver();
                driver.manage().window().maximize();
                break;
            case CHROME:
                System.setProperty(WEBDRIVER_CHROME_DRIVER, configuration.getProperty("webdriver.chrome.driver"));
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("useAutomationExtension", false);
                options.setCapability("acceptInsecureCerts", true);
                options.setCapability("acceptSslCerts", true);
                driver = new ChromeDriver(options);
                driver.manage().window().maximize();
                break;
            case INTERNETEXPLORER:
                System.setProperty(WEBDRIVER_IE_DRIVER, configuration.getProperty("webdriver.ie.driver"));
                driver = new InternetExplorerDriver();
                driver.manage().window().maximize();
                break;
        }
        driver.manage().timeouts().implicitlyWait(Long.parseLong(configuration.getProperty("webdriver.implicit_wait", "20")), TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(Long.parseLong(configuration.getProperty("webdriver.timeout", "30")), TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();

        EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
        return eventDriver;
    }

    public enum DriverType {
        CHROME,
        FIREFOX,
        INTERNETEXPLORER,
    }
}
