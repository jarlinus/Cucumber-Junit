package net.cucumber.demo.steps;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import net.cucumber.demo.engine.TestContext;
import net.cucumber.demo.operations.ScreenshotOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalHooks {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    TestContext testContext;

    public GlobalHooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before
    public void before(Scenario scenario) {
        logger.debug("Starting scenario: " + scenario.getName());
        testContext.setScenario(scenario);
    }

    @After
    public void teardown(Scenario scenario) {
        logger.debug("Ending scenario: " + scenario.getName());
        if (scenario.isFailed()) {
            logger.debug("Scenario failed. Saving screenshot");
            // Generate screenshots with driver name
            testContext.getDrivers().entrySet().stream().forEach(stringWebDriverEntry -> {
                ScreenshotOperations.saveScreenshot(stringWebDriverEntry.getValue(), scenario, String.format("screenshot_failure_%s", stringWebDriverEntry.getKey()));
            });
        }
        testContext.tearDown();
    }
}
