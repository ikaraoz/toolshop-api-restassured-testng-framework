package support;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;

public final class BaseSpecProvider {
    private static RequestSpecification jsonSpec;

    private BaseSpecProvider() {}

    public static RequestSpecification jsonSpec() {
        if (jsonSpec == null) {
            enableLoggingOfRequestAndResponseIfValidationFails();

            // One mapper for the whole suite
            ObjectMapper mapper = new ObjectMapper()
                    .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .findAndRegisterModules();

            // RestAssured config that uses that mapper
            RestAssuredConfig raConfig = RestAssuredConfig.config()
                    .objectMapperConfig(new ObjectMapperConfig()
                            .jackson2ObjectMapperFactory((cls, charset) -> mapper));

            // ✅ Apply globally (affects all RestAssured calls, including response.as(...))
            RestAssured.config = raConfig;

            // ✅ And also on our shared RequestSpecification
            jsonSpec = new RequestSpecBuilder()
                    .setBaseUri(ConfigManager.baseUrl())
                    .setContentType(ContentType.JSON)
                    .setAccept(ContentType.JSON)
                    .setRelaxedHTTPSValidation()
                    .setConfig(raConfig)
                    .log(LogDetail.URI)
                    .build();
        }
        return jsonSpec;
    }
}
