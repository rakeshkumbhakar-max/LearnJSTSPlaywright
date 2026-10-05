package com.qa.salesforce.pages;

import com.qa.salesforce.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends BasePage {

    @FindBy(xpath = "//button[@title='App Launcher'] | //div[contains(@class,'slds-icon-waffle')]")
    private WebElement appLauncherButton;

    @FindBy(xpath = "//span[contains(@class,'uiImage')] | //button[contains(@class,'branding-userProfile-button')]")
    private WebElement userProfileAvatar;

    @FindBy(xpath = "//input[contains(@placeholder,'Search')] | //button[@aria-label='Search']")
    private WebElement globalSearchInput;

    @FindBy(xpath = "//a[@title='Setup']")
    private WebElement setupLink;

    @FindBy(xpath = "//input[@placeholder='Search apps and items...']")
    private WebElement appLauncherSearch;

    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean isLoaded() {
        return isDisplayed(userProfileAvatar);
    }

    public HomePage openAppLauncher() {
        click(appLauncherButton);
        return this;
    }

    public HomePage searchApp(String appName) {
        type(appLauncherSearch, appName);
        return this;
    }

    public void openApp(String appName) {
        openAppLauncher()
                .searchApp(appName)
                .click(By.xpath("//a[.//span[normalize-space()='" + appName + "']]"));
    }

    public void globalSearch(String query) {
        type(globalSearchInput, query);
    }

    public boolean isSetupAvailable() {
        return isDisplayed(setupLink);
    }
}
