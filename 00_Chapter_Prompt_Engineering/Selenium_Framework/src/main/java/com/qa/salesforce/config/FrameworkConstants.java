package com.qa.salesforce.config;

public final class FrameworkConstants {

    private FrameworkConstants() {
    }

    public static final String PROJECT_ROOT = System.getProperty("user.dir");
    public static final String REPORTS_DIR = PROJECT_ROOT + "/reports";
    public static final String SCREENSHOTS_DIR = PROJECT_ROOT + "/screenshots";
    public static final String LOGS_DIR = PROJECT_ROOT + "/logs";
    public static final String EXTENT_REPORT_PATH = REPORTS_DIR + "/ExtentReport.html";

    public static final String BASE_CONFIG_RESOURCE = "config/config.properties";
    public static final String ENV_CONFIG_RESOURCE_TEMPLATE = "config/%s.properties";
}
