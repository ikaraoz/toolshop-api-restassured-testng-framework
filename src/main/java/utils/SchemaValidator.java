package utils;

import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public final class SchemaValidator {
    private SchemaValidator() {
    }

    public static void validate(Response response, String schemaClasspathPath) {
        response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaClasspathPath));
    }
}
