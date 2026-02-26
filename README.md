# Playwright Java Framework

A clean, extensible test automation framework built with **Playwright for Java**, supporting both **UI** and **API** testing with first-class **Page Object Pattern** enforcement and optional **Cucumber BDD** integration.

## Project Structure

```
src/
├── main/java/com/framework/
│   ├── api/                    # API client & response wrapper
│   │   ├── ApiClient.java
│   │   └── ApiResponseWrapper.java
│   ├── config/                 # Environment configuration
│   │   └── ConfigManager.java
│   ├── factory/                # Browser factory
│   │   └── BrowserFactory.java
│   └── pages/                  # Page Objects
│       ├── BasePage.java       # Abstract base for all pages
│       ├── HomePage.java
│       └── LoginPage.java
├── test/java/com/framework/
│   ├── base/                   # Test base classes
│   │   ├── BaseAPITest.java
│   │   └── BaseUITest.java
│   ├── tests/
│   │   ├── api/                # API tests
│   │   │   └── SampleApiTest.java
│   │   ├── cucumber/           # Cucumber BDD (optional)
│   │   │   ├── CucumberRunner.java
│   │   │   ├── hooks/PlaywrightHooks.java
│   │   │   └── steps/LoginSteps.java
│   │   └── ui/                 # UI tests
│   │       └── LoginTest.java
│   └── resources/
│       ├── config/             # Environment properties
│       │   ├── dev.properties
│       │   ├── staging.properties
│       │   └── prod.properties
│       ├── features/           # Cucumber feature files
│       │   └── login.feature
│       ├── logback-test.xml
│       ├── testng.xml
│       └── testng-cucumber.xml
```

## Prerequisites

- **Java 17+**
- **Maven 3.8+**

## Quick Start

```bash
# Install Playwright browsers
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"

# Run all tests (default: dev environment)
mvn test

# Run with specific environment
mvn test -Denv=staging
mvn test -Pstaging

# Run only UI tests
mvn test -Dtest="com.framework.tests.ui.*"

# Run only API tests
mvn test -Dtest="com.framework.tests.api.*"

# Run Cucumber tests
mvn test -Pcucumber
```

## Configuration

Configuration is resolved with the following priority (highest first):

1. **System properties** (`-Dkey=value`)
2. **Environment variables**
3. **Properties file** (`config/{env}.properties`)
4. **Default values**

### Key Properties

| Property | Default | Description |
|---|---|---|
| `base.url` | `http://localhost` | Application base URL |
| `api.base.url` | `http://localhost/api` | API base URL |
| `browser` | `chromium` | Browser type: `chromium`, `firefox`, `webkit` |
| `headless` | `true` | Run in headless mode |
| `default.timeout` | `30000` | Default timeout in ms |
| `tracing.enabled` | `false` | Enable Playwright tracing |
| `video.enabled` | `false` | Enable video recording |
| `slow.mo` | `0` | Slow down operations (ms) |

### Switch Environments

```bash
# Via Maven profile
mvn test -Pstaging

# Via system property
mvn test -Denv=prod

# Override individual properties
mvn test -Denv=dev -Dbrowser=firefox -Dheadless=false
```

## Writing Tests

### UI Tests (Page Object Pattern)

1. **Create a Page Object** extending `BasePage`:

```java
public class DashboardPage extends BasePage {
    private final Locator header;

    public DashboardPage(Page page) {
        super(page);
        this.header = page.locator("h1.dashboard-header");
    }

    public DashboardPage open() {
        navigateTo(ConfigManager.getInstance().getBaseUrl() + "/dashboard");
        return this;
    }

    public String getHeaderText() {
        return header.textContent();
    }
}
```

2. **Write a test** extending `BaseUITest`:

```java
public class DashboardTest extends BaseUITest {
    private DashboardPage dashboardPage;

    @BeforeMethod(dependsOnMethods = "setupTest")
    public void initPages() {
        dashboardPage = new DashboardPage(page);
    }

    @Test
    public void testDashboardLoads() {
        dashboardPage.open();
        assertThat(dashboardPage.getHeaderText()).contains("Dashboard");
    }
}
```

### API Tests

Extend `BaseAPITest` and use the built-in `apiClient`:

```java
public class UserApiTest extends BaseAPITest {

    @Test
    public void testGetUser() {
        ApiResponseWrapper response = apiClient.get("/users/1");
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.bodyAsJson().get("name").asText()).isNotEmpty();
    }

    @Test
    public void testCreateUser() {
        var body = Map.of("name", "John", "email", "john@example.com");
        ApiResponseWrapper response = apiClient.post("/users", body);
        assertThat(response.statusCode()).isEqualTo(201);
    }
}
```

### Cucumber BDD Tests

1. Add feature files under `src/test/resources/features/`
2. Add step definitions under `com.framework.tests.cucumber.steps`
3. Run with: `mvn test -Pcucumber`

## Architecture

| Layer | Responsibility |
|---|---|
| `ConfigManager` | Centralized, environment-aware configuration |
| `BrowserFactory` | Creates Playwright → Browser → Context → Page |
| `BasePage` | Common UI interactions, enforces Page Object pattern |
| `ApiClient` | REST client wrapper over Playwright's API context |
| `BaseUITest` | Browser lifecycle for TestNG UI tests |
| `BaseAPITest` | API context lifecycle for TestNG API tests |
| `PlaywrightHooks` | Browser lifecycle for Cucumber scenarios |

## Tracing
### Option 1
In *.properties, change:
```
tracing.enabled=true
```

### Option 2
Pass it as a system property (no file changes needed)
```
mvn test -Dtracing.enabled=true
```

### View the Trace
After running tests with tracing enabled, trace files are saved to playwright-traces/<testName>.zip. Open them with:
```
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="show-trace playwright-traces/testValidLogin.zip"
```

This launches the Playwright Trace Viewer in your browser, where you can step through each action, see screenshots, DOM snapshots, network requests, and console logs.

You can also open traces at trace.playwright.dev by dragging and dropping the .zip file — no install needed.

## Test Artifacts

On test failure:
- **Screenshots** saved to `screenshots/`
- **Traces** saved to `playwright-traces/` (when enabled)
- **Videos** saved to `videos/` (when enabled)
- **Logs** written to `target/test.log`