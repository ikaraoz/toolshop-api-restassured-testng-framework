package utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * A custom Rest Assured filter that logs every request and response
 * to both the console and a file under build/logs/api.log.
 */
public class ApiLogFilter implements Filter {

    private static final File LOG_FILE = new File("build/logs/api.log");

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        // Proceed with the actual request execution
        Response res = ctx.next(requestSpec, responseSpec);

        StringBuilder sb = new StringBuilder();

        sb.append("\n==================== API CALL ====================\n");
        sb.append("Timestamp: ").append(LocalDateTime.now()).append("\n");
        sb.append("Method: ").append(requestSpec.getMethod()).append("\n");
        sb.append("Request URI: ").append(requestSpec.getURI()).append("\n");

        // Safely handle request body of any type
        Object rawBody = requestSpec.getBody();
        if (rawBody != null) {
            String bodyString;
            if (rawBody instanceof String) {
                bodyString = (String) rawBody;
            } else if (rawBody instanceof char[]) {
                bodyString = new String((char[]) rawBody);
            } else {
                bodyString = String.valueOf(rawBody);
            }
            sb.append("Request Body:\n").append(bodyString).append("\n");
        }

        // Response info
        sb.append("Status Code: ").append(res.statusCode()).append("\n");
        try {
            String responseBody = res.getBody() == null ? "" : res.getBody().asPrettyString();
            if (responseBody != null && !responseBody.isBlank()) {
                sb.append("Response Body:\n").append(responseBody).append("\n");
            }
        } catch (Exception e) {
            sb.append("Response Body: [Error reading body: ").append(e.getMessage()).append("]\n");
        }

        sb.append("==================================================\n\n");

        // Print to console
        System.out.println(sb);

        // Save to file
        LOG_FILE.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(sb.toString());
        } catch (IOException e) {
            System.err.println("Failed to write API log: " + e.getMessage());
        }

        return res;
    }
}
