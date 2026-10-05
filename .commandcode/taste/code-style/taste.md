# Code Style

- In Java/Selenium test code, prefers Page Object Model using PageFactory with `@FindBy`-annotated `WebElement` fields, not `By` locator constants. Confidence: 0.6
- Prefers XPath locators only; explicitly wants CSS selectors (and `By.cssSelector`) removed/avoided. Confidence: 0.6
- Prefers source files free of comments — no Javadoc, block, or inline `//` comments. Confidence: 0.55
- Uses a BaseTest/BasePage hierarchy with WebDriverWait-based explicit waits (never `Thread.sleep()`). Confidence: 0.5
