package api;

import constants.Endpoints;
import io.restassured.response.Response;
import payloads.LoginRequest;
import payloads.RegisterUserRequest;
import support.BaseSpecProvider;

import static io.restassured.RestAssured.given;

public final class UserApi {
    private UserApi() {
    }

    public static Response login(LoginRequest body) {
        return given()
                .spec(BaseSpecProvider.jsonSpec())
                .body(body)
                .when()
                .post(Endpoints.USERS_LOGIN)
                .then()
                .extract().response();
    }

    public static Response register(RegisterUserRequest body) {
        return given()
                .spec(BaseSpecProvider.jsonSpec())
                .body(body)
                .when()
                .post(Endpoints.USERS_REGISTER)
                .then()
                .extract().response();
    }
}
