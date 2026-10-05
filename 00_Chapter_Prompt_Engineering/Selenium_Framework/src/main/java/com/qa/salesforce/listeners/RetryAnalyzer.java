package com.qa.salesforce.listeners;

import com.qa.salesforce.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOG = LogManager.getLogger(RetryAnalyzer.class);

    private final int maxRetryCount = ConfigManager.getInstance().getInt("retry.count");
    private int retryCount;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            LOG.warn("Retrying '{}' - attempt {}/{}",
                    result.getMethod().getMethodName(), retryCount, maxRetryCount);
            return true;
        }
        return false;
    }
}
