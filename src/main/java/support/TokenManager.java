package support;

import io.restassured.response.Response;
import payloads.LoginRequest;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static io.restassured.RestAssured.given;
import static constants.Endpoints.USERS_LOGIN;

public final class TokenManager {
    private static final AtomicReference<String> TOKEN = new AtomicReference<>();
    private static volatile Instant expiry = Instant.EPOCH;

    private TokenManager() {}

    public static String getToken() {
        if (Instant.now().isAfter(expiry)) {
            synchronized (TokenManager.class) {
                if (Instant.now().isAfter(expiry)) {
                    Response res = given()
                            .spec(BaseSpecProvider.jsonSpec())
                            .body(LoginRequest.builder()
                                    .email(ConfigManager.username())
                                    .password(ConfigManager.password())
                                    .build())
                            .post(USERS_LOGIN);

                    res.then().statusCode(200);
                    String token = extractToken(res);
                    TOKEN.set(token);
                    expiry = Instant.now().plusSeconds(50 * 60);
                }
            }
        }
        return TOKEN.get();
    }

    private static String extractToken(Response res) {
        Map<String, Object> map = res.jsonPath().getMap("$");
        if (map == null) throw new IllegalStateException("Login response was not JSON");
        for (String key : new String[]{"access_token", "token", "jwt"}) {
            Object v = map.get(key);
            if (v instanceof String && !((String) v).isBlank()) return (String) v;
        }
        try {
            String nested = res.jsonPath().getString("data.token");
            if (nested != null && !nested.isBlank()) return nested;
        } catch (Exception ignore) {}
        throw new IllegalStateException("Could not extract token from response: " + map);
    }
}
