package net.cucumber.demo.operations;

import io.cucumber.java.Scenario;
import net.cucumber.demo.Configuration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotOperations {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotOperations.class);
    private static final String SCREENSHOTS_PATH = Configuration.getInstance().getProperty("screenshots.path", "target/screenshots");

    public static void saveScreenshot(WebDriver driver, Scenario scenario) {
        saveScreenshot(driver, scenario, "screenshot");
    }

    public static void saveScreenshot(WebDriver driver, Scenario scenario, String filename) {
        if (!(driver instanceof TakesScreenshot)) {
            logger.warn("Driver does not support taking screenshots");
            return;
        }

        try {
            // Take screenshot using Selenium 4 built-in capability
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            
            // Create timestamp for unique filename
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"));
            String screenshotFileName = String.format("%s_%s.png", filename, timestamp);
            
            // Ensure screenshots directory exists
            Path screenshotsDir = Paths.get(SCREENSHOTS_PATH);
            Files.createDirectories(screenshotsDir);
            
            // Save screenshot to file
            Path screenshotPath = screenshotsDir.resolve(screenshotFileName);
            Files.write(screenshotPath, screenshotBytes);
            
            logger.info("Screenshot saved: {}", screenshotPath.toAbsolutePath());
            
            // Attach screenshot to Cucumber report if scenario is provided
            if (scenario != null) {
                scenario.attach(screenshotBytes, "image/png", String.format("%s - %s", filename, scenario.getName()));
                logger.debug("Screenshot attached to Cucumber report for scenario: {}", scenario.getName());
            }
            
        } catch (IOException e) {
            logger.error("Failed to save screenshot", e);
        } catch (Exception e) {
            logger.error("Unexpected error while taking screenshot", e);
        }
    }

    /**
     * Take a full page screenshot (if supported by the browser)
     */
    public static void saveFullPageScreenshot(WebDriver driver, Scenario scenario, String filename) {
        logger.info("Taking full page screenshot");
        // For now, use the standard screenshot method
        // Future enhancement: implement full page screenshot for supported browsers
        saveScreenshot(driver, scenario, filename + "_fullpage");
    }
}
