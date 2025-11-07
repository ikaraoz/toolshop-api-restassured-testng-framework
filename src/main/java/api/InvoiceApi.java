package api;

import constants.Endpoints;
import filters.InvoiceFilter;
import io.restassured.response.Response;
import payloads.invoices.CreateInvoiceRequest;
import payloads.invoices.InvoiceRequest;
import support.BaseSpecProvider;
import support.TokenManager;

import static io.restassured.RestAssured.given;

public final class InvoiceApi {
    private InvoiceApi() {
    }

    public static Response listInvoices(InvoiceFilter filter) {
        var req = given()
                .spec(BaseSpecProvider.jsonSpec())
                .header("Authorization", "Bearer " + TokenManager.getToken());
        if (filter != null) {
            if (filter.getPage() != null) req.queryParam("page", filter.getPage());
        }
        return req.when()
                .get(Endpoints.INVOICES)
                .then()
                .extract().response();
    }

    public static Response listInvoices() {
        return listInvoices(null);
    }

    public static Response createInvoice(CreateInvoiceRequest payload) {
        return given()
                .spec(BaseSpecProvider.jsonSpec())
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .body(payload)
                .when()
                .post(Endpoints.INVOICES)
                .then()
                .extract().response();
    }
}
