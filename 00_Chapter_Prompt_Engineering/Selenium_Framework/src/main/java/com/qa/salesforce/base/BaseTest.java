package com.qa.salesforce.base;

import com.qa.salesforce.config.ConfigManager;
import com.qa.salesforce.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver();
        String baseUrl = ConfigManager.getInstance().get("base.url");
        getDriver().get(baseUrl);
        log.info("Navigated to '{}' [env={}]", baseUrl, ConfigManager.getInstance().getEnvironment());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
