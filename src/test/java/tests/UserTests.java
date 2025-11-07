package tests;

import base.BaseTest;
import api.UserApi;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import payloads.LoginRequest;
import payloads.RegisterUserRequest;
import utils.DataFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTests extends BaseTest {

    @Test(groups = {"smoke"})
    public void login_returns_token() {
        Response res = UserApi.login(LoginRequest.builder()
                .email("customer@practicesoftwaretesting.com")
                .password("welcome01")
                .build());

        assertThat(res.statusCode()).isEqualTo(200);
        String token = res.jsonPath().getString("access_token");
        if (token == null || token.isBlank()) token = res.jsonPath().getString("token");
        assertThat(token).isNotBlank();
    }

    @Test(groups = {"regression"})
    public void register_creates_user() {
        RegisterUserRequest payload = RegisterUserRequest.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email(DataFactory.randomEmail())
                .password(DataFactory.randomString(10)+"@1")
                .build();

        Response res = UserApi.register(payload);
        assertThat(res.statusCode()).isIn(200, 201, 204);
    }
}
