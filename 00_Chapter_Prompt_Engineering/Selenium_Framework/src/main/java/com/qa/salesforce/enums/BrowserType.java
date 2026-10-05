package com.qa.salesforce.enums;

import com.qa.salesforce.exceptions.FrameworkException;

import java.util.Arrays;

public enum BrowserType {

    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge");

    private final String value;

    BrowserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BrowserType from(String value) {
        return Arrays.stream(values())
                .filter(browser -> browser.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new FrameworkException("Unsupported browser: '" + value
                        + "'. Supported values: " + Arrays.toString(values())));
    }
}
