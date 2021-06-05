package net.cucumber.demo.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"net/cucumber/demo/steps"},
        plugin = {"json:target/cucumber-reports/cucumber-json-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber-junit-reports/cucumberJunit.xml"},
        dryRun = false,
        strict = true,
        monochrome = true,
        tags = {"@demoTest"}
)

public class TestRunner {

    private static long duration;

    @BeforeClass
    public static void before() {
        duration = System.currentTimeMillis();
        System.out.println("Thread Id  | Scenario Num       | Step Count");
    }

    @AfterClass
    public static void after() {
        duration = System.currentTimeMillis() - duration;
        System.out.println("DURATION - "+ duration);
    }
}






