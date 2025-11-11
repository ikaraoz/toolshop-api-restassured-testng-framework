package steps;

import api.ProductApi;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import responses.products.ProductListResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductSteps {

    private Response response;
    private ProductListResponse productList;
    private String productId;

    @When("I request the default product list")
    public void i_request_the_default_product_list() {
        response = ProductApi.listProducts();
        productList = response.as(ProductListResponse.class);
    }

    @Then("the product response status is {int}")
    public void the_product_response_status_is(int statusCode) {
        assertThat(response).as("Product response should not be null").isNotNull();
        assertThat(response.statusCode()).isEqualTo(statusCode);
    }

    @Then("the product list is not empty")
    public void the_product_list_is_not_empty() {
        assertThat(productList).as("Product list response should not be null").isNotNull();
        assertThat(productList.getData()).as("Product list should not be empty").isNotEmpty();
    }

    @Then("the product list contains products with required fields")
    public void the_product_list_contains_products_with_required_fields() {
        assertThat(productList).as("Product list response should not be null").isNotNull();
        assertThat(productList.getData()).as("Product list should not be empty").isNotEmpty();

        ProductListResponse.Product first = productList.getData().get(0);
        assertThat(first.getId()).as("Product id").isNotBlank();
        assertThat(first.getName()).as("Product name").isNotBlank();
        assertThat(first.getPrice()).as("Product price").isGreaterThan(0);
        assertThat(first.getCategory()).as("Product category").isNotNull();
        assertThat(first.getCategory().getName()).as("Category name").isNotBlank();
    }

    @Then("the product list metadata is present")
    public void the_product_list_metadata_is_present() {
        assertThat(productList).as("Product list response should not be null").isNotNull();
        assertThat(productList.getCurrentPage()).as("Current page should be >= 1").isGreaterThanOrEqualTo(1);
        assertThat(productList.getTotal()).as("Total products should be > 0").isGreaterThan(0);
    }

    @Given("I have the first product from the catalogue")
    public void i_have_the_first_product_from_the_catalogue() {
        i_request_the_default_product_list();
        the_product_list_is_not_empty();
        productId = productList.getData().get(0).getId();
        assertThat(productId).as("Product id").isNotBlank();
    }

    @When("I request the product details by id")
    public void i_request_the_product_details_by_id() {
        assertThat(productId).as("Product id should be captured").isNotBlank();
        response = ProductApi.getProductById(productId);
    }

    @Then("the product details response status is {int}")
    public void the_product_details_response_status_is(int statusCode) {
        the_product_response_status_is(statusCode);
    }

    @Then("the product id matches the requested id")
    public void the_product_id_matches_the_requested_id() {
        assertThat(response).as("Product details response should not be null").isNotNull();
        String returnedId = response.jsonPath().getString("id");
        assertThat(returnedId).as("Returned product id").isEqualTo(productId);
    }
}