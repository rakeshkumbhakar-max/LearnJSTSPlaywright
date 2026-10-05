package com.qa.salesforce.tests;

import com.qa.salesforce.base.BaseTest;
import com.qa.salesforce.config.ConfigManager;
import com.qa.salesforce.pages.HomePage;
import com.qa.salesforce.pages.LoginPage;
import com.qa.salesforce.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Verify the login page is displayed on application launch",
            groups = {"smoke"})
    public void testLoginPageIsDisplayed() {
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoaded(), "Salesforce login page should be displayed");
    }

    @Test(description = "Verify a valid user can log in and reach the home page",
            groups = {"smoke", "regression"})
    public void testValidLogin() {
        ConfigManager config = ConfigManager.getInstance();
        HomePage homePage = new LoginPage(getDriver())
                .login(config.get("salesforce.username"), config.get("salesforce.password"));
        Assert.assertTrue(homePage.isLoaded(), "User should land on the home page after a valid login");
    }

    @Test(description = "Verify an error message is shown for invalid credentials",
            dataProvider = "invalidLoginData", groups = {"regression"})
    public void testInvalidLogin(String username, String password, String expectedError) {
        LoginPage loginPage = new LoginPage(getDriver())
                .enterUsername(username)
                .enterPassword(password)
                .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorDisplayed(), "An error message should be displayed for invalid credentials");
        Assert.assertTrue(loginPage.getErrorMessage().contains(expectedError),
                "Error message should contain: '" + expectedError + "'");
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return JsonUtils.toDataProvider("testdata/invalid-login.json", "username", "password", "expectedError");
    }
}
