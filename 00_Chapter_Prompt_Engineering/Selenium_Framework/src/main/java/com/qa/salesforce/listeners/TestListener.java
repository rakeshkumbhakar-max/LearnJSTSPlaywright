package com.qa.salesforce.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.salesforce.config.ConfigManager;
import com.qa.salesforce.driver.DriverManager;
import com.qa.salesforce.reports.ExtentManager;
import com.qa.salesforce.reports.ExtentTestManager;
import com.qa.salesforce.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        LOG.info("===== Starting test context: {} =====", context.getName());
        ExtentManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("STARTED  : {}", result.getMethod().getMethodName());
        ExtentTestManager.startTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("PASSED   : {}", result.getMethod().getMethodName());
        withTest(test -> test.log(Status.PASS, "Test passed"));
        ExtentTestManager.endTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("FAILED   : {} - {}", result.getMethod().getMethodName(), result.getThrowable());
        withTest(test -> test.fail(result.getThrowable()));
        attachScreenshot(result.getMethod().getMethodName());
        ExtentTestManager.endTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("SKIPPED  : {}", result.getMethod().getMethodName());
        withTest(test -> test.log(Status.SKIP, "Test skipped: " + result.getThrowable()));
        ExtentTestManager.endTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        LOG.info("===== Finished test context: {} | Passed={} Failed={} Skipped={} =====",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        ExtentManager.flush();
    }

    private void attachScreenshot(String testName) {
        if (!DriverManager.isDriverInitialised() || !ConfigManager.getInstance().getBoolean("screenshot.on.failure")) {
            return;
        }
        try {
            String base64 = ScreenshotUtils.captureBase64(DriverManager.getDriver());
            withTest(test -> test.fail("Failure Screenshot",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64, testName).build()));
            ScreenshotUtils.captureToFile(DriverManager.getDriver(), testName);
        } catch (Exception e) {
            LOG.error("Unable to capture screenshot for test '{}'", testName, e);
        }
    }

    private void withTest(java.util.function.Consumer<com.aventstack.extentreports.ExtentTest> action) {
        com.aventstack.extentreports.ExtentTest test = ExtentTestManager.getTest();
        if (test != null) {
            action.accept(test);
        }
    }
}
