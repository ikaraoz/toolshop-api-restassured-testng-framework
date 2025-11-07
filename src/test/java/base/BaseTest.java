package base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import support.ConfigManager;
import utils.ApiLogFilter;

public abstract class BaseTest {

    @BeforeClass
    public void configure() {
        RestAssured.baseURI = ConfigManager.baseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured(), new ApiLogFilter());
    }
}
