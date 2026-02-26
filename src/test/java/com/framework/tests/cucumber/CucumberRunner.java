package com.framework.tests.cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Cucumber TestNG runner.
 * Activate with: mvn test -Pcucumber
 * <p>
 * Add feature files under src/test/resources/features/
 * Add step definitions under com.framework.tests.cucumber.steps
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.framework.tests.cucumber.steps", "com.framework.tests.cucumber.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json"
        },
        monochrome = true
)
public class CucumberRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
