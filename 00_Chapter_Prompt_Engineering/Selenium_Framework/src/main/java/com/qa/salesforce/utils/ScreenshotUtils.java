package com.qa.salesforce.utils;

import com.qa.salesforce.config.FrameworkConstants;
import com.qa.salesforce.exceptions.FrameworkException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
    }

    public static String captureBase64(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    public static String captureToFile(WebDriver driver, String testName) {
        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String safeName = testName.replaceAll("[^a-zA-Z0-9-_]", "_");
        String fileName = safeName + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png";
        Path destination = Paths.get(FrameworkConstants.SCREENSHOTS_DIR, fileName);
        try {
            Files.createDirectories(destination.getParent());
            FileUtils.copyFile(source, destination.toFile());
        } catch (IOException e) {
            throw new FrameworkException("Failed to save screenshot: " + fileName, e);
        }
        return destination.toAbsolutePath().toString();
    }
}
