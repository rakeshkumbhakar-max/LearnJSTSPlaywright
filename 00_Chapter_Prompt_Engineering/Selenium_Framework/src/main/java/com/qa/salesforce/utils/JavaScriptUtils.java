package com.qa.salesforce.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public final class JavaScriptUtils {

    private JavaScriptUtils() {
    }

    public static void click(WebDriver driver, WebElement element) {
        execute(driver, "arguments[0].click();", element);
    }

    public static void scrollIntoView(WebDriver driver, WebElement element) {
        execute(driver, "arguments[0].scrollIntoView({block:'center', inline:'center'});", element);
    }

    public static void highlight(WebDriver driver, WebElement element) {
        execute(driver, "arguments[0].style.border='3px solid red';", element);
    }

    public static Object execute(WebDriver driver, String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }
}
