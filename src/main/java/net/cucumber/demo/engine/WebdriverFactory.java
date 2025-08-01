package net.cucumber.demo.engine;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.cucumber.demo.Configuration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class WebdriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(WebdriverFactory.class);
    private static WebdriverFactory instance = new WebdriverFactory();
    private final Configuration configuration = Configuration.getInstance();
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
        return switch (browserName.toLowerCase()) {
            case "chrome", "local-chrome" -> DriverType.CHROME;
            case "firefox", "local-firefox" -> DriverType.FIREFOX;
            case "edge", "local-edge" -> DriverType.EDGE;
            default -> throw new IllegalArgumentException(
                "Browser Name Key value in Configuration.properties is not matched : " + browserName);
        };
    }

    private WebDriver createDriver(DriverType browserType, String testName) {
        WebDriver driver = switch (browserType) {
            case FIREFOX -> createFirefoxDriver();
            case CHROME -> createChromeDriver();
            case EDGE -> createEdgeDriver();
        };

        configureDriver(driver);
        logger.info("Created {} driver for test: {}", browserType, testName);
        return driver;
    }

    private WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        // Add common options
        if (configuration.getBooleanProperty("headless", false)) {
            options.addArguments("--headless");
        }
        
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    private WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        // Modern Chrome options
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        
        if (configuration.getBooleanProperty("headless", false)) {
            options.addArguments("--headless=new");
        }
        
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    private WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (configuration.getBooleanProperty("headless", false)) {
            options.addArguments("--headless");
        }
        
        WebDriver driver = new EdgeDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    private void configureDriver(WebDriver driver) {
        // Set timeouts using Duration (Selenium 4 way)
        int implicitWait = configuration.getIntProperty("webdriver.implicit_wait", 20);
        int pageLoadTimeout = configuration.getIntProperty("webdriver.timeout", 30);
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().deleteAllCookies();
    }

    public enum DriverType {
        CHROME,
        FIREFOX,
        EDGE
    }
}
