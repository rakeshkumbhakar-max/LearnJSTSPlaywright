package com.qa.salesforce.driver;

import org.openqa.selenium.WebDriver;

import java.util.Objects;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void initDriver() {
        if (Objects.isNull(DRIVER.get())) {
            DRIVER.set(DriverFactory.createDriver());
        }
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (Objects.isNull(driver)) {
            throw new IllegalStateException(
                    "WebDriver is not initialised for thread: " + Thread.currentThread().getName());
        }
        return driver;
    }

    public static boolean isDriverInitialised() {
        return Objects.nonNull(DRIVER.get());
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (Objects.nonNull(driver)) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
