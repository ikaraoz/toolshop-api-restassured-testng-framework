package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "steps",
        plugin = {"pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        }
)
@Test
public class CucumberTest extends AbstractTestNGCucumberTests {
//    @BeforeTest
//    void beforeTest() {
//
//    }
//
//    @AfterTest
//    void AfterTest() {
//
//    }
}