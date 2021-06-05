package net.cucumber.demo.engine;

import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * The test context that will be used for the different tests.
 * It holds the test state: the current driver and a test data {@link Map}.
 *
 * A new instance should be initialised per test flow, and should be destroyed at the end of its lifecycle via the teardown method.
 */
public class TestContext {
    public static final String DEFAULT_DRIVER = "DEFAULT_DRIVER";

    private final Map<String, WebDriver> drivers = new HashMap<>();
    private final Map<String, Object> testData = new HashMap<>();
    private Scenario scenario;

    public WebDriver getDriver(String name) {
        WebDriver driver = drivers.get(name);
        if (driver == null) {
            driver = getDriverFromFactory();
            drivers.put(name, driver);
        }
        return driver;
    }

    protected WebDriver getDriverFromFactory() {
        return WebdriverFactory.getInstance().getDriver(scenario.getName());
    }

    public WebDriver getDriver() {
        return getDriver(DEFAULT_DRIVER);
    }

    public void quitDriver() {
        quitDriver(DEFAULT_DRIVER);
    }

    public void quitDriver(String driverName) {
        WebDriver driver = drivers.get(driverName);
        if (driver != null) {
            driver.quit();
            drivers.remove(driverName);
        }
    }

    public Map<String, Object> getTestData() {
        return testData;
    }

    public void tearDown() {
        if (drivers != null) {
            drivers.entrySet().stream().forEach(entry -> entry.getValue().quit());
            drivers.clear();
        }
        testData.clear();
        scenario = null;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public Map<String, WebDriver> getDrivers() {
        return drivers;
    }
}
