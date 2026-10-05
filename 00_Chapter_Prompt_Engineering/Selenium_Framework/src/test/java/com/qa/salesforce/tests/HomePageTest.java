package com.qa.salesforce.tests;

import com.qa.salesforce.base.BaseTest;
import com.qa.salesforce.config.ConfigManager;
import com.qa.salesforce.pages.HomePage;
import com.qa.salesforce.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void loginFirst() {
        ConfigManager config = ConfigManager.getInstance();
        homePage = new LoginPage(getDriver())
                .login(config.get("salesforce.username"), config.get("salesforce.password"));
        Assert.assertTrue(homePage.isLoaded(), "Login failed - unable to reach the home page");
    }

    @Test(description = "Verify the App Launcher is available on the home page",
            groups = {"regression"})
    public void testAppLauncherIsAvailable() {
        homePage.openAppLauncher();
        Assert.assertTrue(homePage.getPageTitle().contains("Salesforce") || homePage.getCurrentUrl().contains("/lightning/"),
                "App Launcher should open without leaving the Lightning app");
    }

    @Test(description = "Verify global search is available on the home page",
            groups = {"regression"})
    public void testGlobalSearchIsAvailable() {
        homePage.globalSearch("Accounts");
        Assert.assertTrue(homePage.getCurrentUrl().contains("/lightning/"),
                "Global search should be usable from the home page");
    }
}
