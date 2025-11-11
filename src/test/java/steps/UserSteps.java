package steps;

import api.UserApi;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import payloads.LoginRequest;
import payloads.RegisterUserRequest;
import responses.users.LoginResponse;
import utils.DataFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSteps {

    private Response response;
    private LoginRequest loginRequest;
    private RegisterUserRequest registerRequest;

    @Given("I am an existing user with email {string} and password {string}")
    public void i_am_an_existing_user(String email, String password) {
        loginRequest = LoginRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    @When("I submit the login request")
    public void i_submit_the_login_request() {
        response = UserApi.login(loginRequest);
    }

    @Then("the login is successful and an access token is returned")
    public void the_login_is_successful_and_an_access_token_is_returned() {
        assertThat(response).as("Login response should not be null").isNotNull();
        assertThat(response.statusCode()).isEqualTo(200);

        String token = response.as(LoginResponse.class).getAccessToken();
        if (token == null || token.isBlank()) {
            token = response.jsonPath().getString("token");
        }
        assertThat(token).as("Access token").isNotNull().isNotBlank();
    }

    @Given("I provide new user details")
    public void i_provide_new_user_details() {
        registerRequest = RegisterUserRequest.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email(DataFactory.randomEmail())
                .password(DataFactory.randomString(10) + "@1")
                .build();
    }

    @When("I submit the registration request")
    public void i_submit_the_registration_request() {
        response = UserApi.register(registerRequest);
    }

    @Then("the user is created successfully")
    public void the_user_is_created_successfully() {
        assertThat(response).as("Registration response should not be null").isNotNull();
        assertThat(response.statusCode()).isIn(200, 201, 204);
    }
}