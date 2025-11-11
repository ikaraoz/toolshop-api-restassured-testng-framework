package steps;

import io.cucumber.java.Before;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import support.ConfigManager;
import utils.ApiLogFilter;

public class Hooks {

    @Before
    public void configureRestAssured() {
        RestAssured.baseURI = ConfigManager.baseUrl();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured(), new ApiLogFilter());
    }
}