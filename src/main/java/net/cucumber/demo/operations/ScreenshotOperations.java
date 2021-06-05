package net.cucumber.demo.operations;

import io.cucumber.java.Scenario;
import net.cucumber.demo.Configuration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ScreenshotOperations {
    private static final Logger log = LoggerFactory.getLogger(ScreenshotOperations.class);
    private static final String PATH = Configuration.getInstance().getProperty("screenshots.path", "target/screenshots");

    public static void saveScreenshot(WebDriver driver, Scenario scenario) {
        saveScreenshot(driver, scenario, "screenshot");
    }

    public static void saveScreenshot(WebDriver driver, Scenario scenario, String filename) {
        try {
            Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
            File file = new File(String.format(PATH + "/" + filename + "-%d-%d.png", System.currentTimeMillis(), new Random().nextInt(99999999)));
            file.mkdirs();
            ImageIO.write(screenshot.getImage(), "png", file);
            if (scenario != null) {
                scenario.embed(FileUtils.readFileToByteArray(file), "image/png", String.format("%s - %s", filename, scenario.getName()));
            }
        } catch (IOException e) {
            log.error("Could not save screen shot", e);
        }
    }
}
