package steps;

import api.InvoiceApi;
import filters.InvoiceFilter;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class InvoiceSteps {

    private Response response;

    @When("I request invoices for page {int}")
    public void i_request_invoices_for_page(int page) {
        InvoiceFilter filter = InvoiceFilter.builder().page(page).build();
        response = InvoiceApi.listInvoices(filter);
    }

    @Then("the invoice response status is {int}")
    public void the_invoice_response_status_is(int statusCode) {
        assertThat(response).as("Invoice response should not be null").isNotNull();
        assertThat(response.statusCode()).isEqualTo(statusCode);
    }
}