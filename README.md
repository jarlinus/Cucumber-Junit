# Selenium-Cucumber-JUnit5 Test Framework

## Introduction
This is a modernized test automation framework demonstrating web application UI testing using:
- **Selenium 4.x** with WebDriverManager for automated driver management
- **Cucumber 7.x** for behavior-driven development (BDD)
- **JUnit 5** as the testing framework
- **Java 17** with modern language features

## Prerequisites
- Java 17 or higher
- Maven 3.6+

## Key Features
- ✅ **Automatic WebDriver Management** - No need to download drivers manually
- ✅ **Modern Selenium 4** patterns with explicit waits and Duration-based timeouts
- ✅ **JUnit 5** integration with Cucumber
- ✅ **Headless execution** support
- ✅ **Enhanced screenshot capabilities** with automatic attachment to reports
- ✅ **Comprehensive logging** with SLF4J and Logback
- ✅ **Modern Java 17** features (switch expressions, var keyword, etc.)

## Configuration
Update the [configuration.properties](src/test/resources/configs/configuration.properties) file:
- `browser`: chrome, firefox, or edge
- `headless`: true/false for headless execution
- `webdriver.implicit_wait`: implicit wait timeout in seconds
- `webdriver.timeout`: page load timeout in seconds

## Running Tests

### Command Line
```bash
# Run all tests
mvn test

# Run with headless mode
mvn test -Dheadless=true

# Run with specific browser
mvn test -Dbrowser=firefox

# Run specific tags
mvn test -Dcucumber.filter.tags="@demoTest"
```

### IDE
Run the `TestRunner` class in your IDE or execute individual feature files.

## Reports
After execution, reports are available at:
- **Cucumber HTML Reports**: `target/cucumber-reports/cucumber-html-reports/`
- **JSON Reports**: `target/cucumber-reports/cucumber-json-reports/`
- **JUnit XML**: `target/cucumber-reports/cucumber-junit-reports/`
- **Screenshots**: `target/screenshots/`

## Project Structure
```
src/
├── main/java/net/cucumber/demo/
│   ├── Configuration.java          # Centralized configuration management
│   ├── engine/
│   │   ├── TestContext.java        # Test context and dependency injection
│   │   └── WebdriverFactory.java   # Modern WebDriver factory with WebDriverManager
│   ├── operations/
│   │   └── ScreenshotOperations.java # Selenium 4 screenshot utilities
│   ├── pages/                      # Page Object Model classes
│   └── steps/                      # Cucumber step definitions
└── test/
    ├── java/net/cucumber/demo/runner/
    │   └── TestRunner.java          # JUnit 5 + Cucumber test runner
    └── resources/
        ├── configs/
        │   └── configuration.properties
        └── features/                # Cucumber feature files
```