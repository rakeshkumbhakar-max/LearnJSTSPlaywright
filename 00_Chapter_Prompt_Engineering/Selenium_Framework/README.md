# Salesforce Selenium Test Automation Framework

Production-ready UI automation framework for **salesforce.com** built with **Selenium 4 + Java 17 + Maven + TestNG**.
Designed for parallel execution, clear reporting, and drop-in CI/CD integration.

## Tech Stack

| Concern            | Choice                                   |
|--------------------|------------------------------------------|
| Language           | Java 17                                  |
| UI Automation      | Selenium 4                               |
| Test Runner        | TestNG                                   |
| Build Tool         | Maven                                    |
| Driver Binaries    | WebDriverManager                         |
| Reporting          | ExtentReports (Spark, dark theme)        |
| Logging            | Log4j2 (console + rolling file)          |
| Test Data          | JSON (Jackson) and Excel (Apache POI)    |

## Key Features

- **ThreadLocal WebDriver** via `DriverManager` — safe for `parallel="methods"`.
- **Page Object Model** with a fluent `BasePage`; no hard-coded `Thread.sleep()` — explicit waits only.
- **Multi-environment** config: `qa`, `staging`, `prod`, selected with `-Denv=<name>`.
- **Automatic screenshots** on failure, embedded in the Extent report and saved to `screenshots/`.
- **Retry analyzer** wired for every test through an `IAnnotationTransformer`.
- **Data-driven tests** through `@DataProvider` backed by JSON/Excel utilities.
- **Fails fast** on missing configuration — no silent nulls.

## Project Structure

```
Selenium_Framework/
├── pom.xml
├── testng.xml
├── .gitignore
├── README.md
├── .github/workflows/ci.yml
└── src
    ├── main
    │   ├── java/com/qa/salesforce
    │   │   ├── base/          # BasePage, BaseTest
    │   │   ├── config/        # ConfigManager, FrameworkConstants
    │   │   ├── driver/        # DriverFactory, DriverManager (ThreadLocal)
    │   │   ├── enums/         # BrowserType
    │   │   ├── exceptions/    # FrameworkException
    │   │   ├── listeners/     # TestListener, RetryAnalyzer, AnnotationTransformer
    │   │   ├── pages/         # LoginPage, HomePage
    │   │   ├── reports/       # ExtentManager, ExtentTestManager
    │   │   └── utils/         # WaitUtils, JavaScriptUtils, ScreenshotUtils, JsonUtils, ExcelUtils
    │   └── resources
    │       ├── config/        # config.properties + qa/staging/prod overrides
    │       └── log4j2.xml
    └── test
        ├── java/com/qa/salesforce/tests   # LoginTest, HomePageTest
        └── resources/testdata             # invalid-login.json
```

## Prerequisites

- JDK 17+
- Maven 3.8+
- A Salesforce org (sandbox recommended) with a test user

## Configuration

Configuration resolves in this order (highest wins):

1. JVM system property — `-Dbrowser=firefox`
2. Environment file — `src/main/resources/config/<env>.properties`
3. Base file — `src/main/resources/config/config.properties`

Set your credentials locally in `src/main/resources/config/qa.properties` (never commit real secrets),
or pass them at runtime:

```bash
mvn test -Dsalesforce.username=user@example.com -Dsalesforce.password=Secret
```

### Common keys

| Key                    | Default | Description                          |
|------------------------|---------|--------------------------------------|
| `env`                  | `qa`    | Active environment                   |
| `browser`              | `chrome`| `chrome` \| `firefox` \| `edge`      |
| `headless`             | `false` | Run without a visible browser        |
| `explicit.wait`        | `20`    | Explicit wait timeout (seconds)      |
| `retry.count`          | `1`     | Retries per failed test              |
| `screenshot.on.failure`| `true`  | Attach screenshots on failure        |

## Running Tests

```bash
# Full suite (default env = qa, browser = chrome)
mvn clean test

# Another environment and browser
mvn clean test -Denv=staging -Dbrowser=firefox

# Headless (CI)
mvn clean test -Dheadless=true

# Only smoke tests
mvn clean test -Dgroups=smoke
```

## Reports & Logs

- HTML report: `reports/ExtentReport.html`
- Failed-test screenshots: `screenshots/`
- Logs: `logs/automation.log` (rolling, gzipped)

## Parallel Execution

`testng.xml` runs methods in parallel (`parallel="methods"`, `thread-count="2"`).
Because the driver is thread-bound, you can raise `thread-count` for faster suites.
Remember to keep page objects stateless — they already are.

## Extending the Framework

1. Add a page object under `pages/` extending `BasePage`.
2. Add a test under `tests/` extending `BaseTest`.
3. Add locators as `private static final By` constants — keep them out of test code.
4. The `TestListener` and `RetryAnalyzer` apply automatically; no per-test wiring needed.

## CI/CD

`.github/workflows/ci.yml` provides a ready-to-use pipeline that runs the suite headless on
every push/PR and uploads the Extent report, screenshots, and logs as artefacts. Set the
`SALESFORCE_USERNAME` / `SALESFORCE_PASSWORD` repository secrets to enable the login tests.
