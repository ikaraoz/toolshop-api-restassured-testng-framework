package steps;

import api.CartApi;
import api.ProductApi;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import payloads.carts.AddItemToCartRequest;
import responses.carts.CreateCartResponse;
import responses.carts.GetCartByIdResponse;
import responses.products.ProductListResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class CartSteps {

    private Response response;
    private String cartId;
    private String productId;
    private AddItemToCartRequest currentRequest;

    @When("I create a new cart")
    public void i_create_a_new_cart() {
        response = CartApi.createCart();
        cartId = response.as(CreateCartResponse.class).getId();
    }

    @Then("the cart response status is {int}")
    public void the_cart_response_status_is(int statusCode) {
        assertThat(response).as("Cart response should not be null").isNotNull();
        assertThat(response.statusCode()).isEqualTo(statusCode);
    }

    @Then("a cart id is generated")
    public void a_cart_id_is_generated() {
        assertThat(cartId).as("Cart id").isNotBlank();
    }

    @Given("I have an existing cart")
    public void i_have_an_existing_cart() {
        Response cartResponse = CartApi.createCart();
        cartId = cartResponse.as(CreateCartResponse.class).getId();
        assertThat(cartId).as("Cart id").isNotBlank();
    }

    @Given("a product is available")
    public void a_product_is_available() {
        Response productResponse = ProductApi.listProducts();
        ProductListResponse list = productResponse.as(ProductListResponse.class);
        assertThat(list.getData()).as("Product list should not be empty").isNotEmpty();
        productId = list.getData().get(0).getId();
        assertThat(productId).as("Product id").isNotBlank();
    }

    @When("I add {int} units of the product to the cart")
    public void i_add_units_of_the_product_to_the_cart(int quantity) {
        ensureCartAndProduct();
        currentRequest = AddItemToCartRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
        response = CartApi.addItemToCart(currentRequest, cartId);
    }

    @Then("the cart item is added successfully")
    public void the_cart_item_is_added_successfully() {
        assertThat(response).as("Cart item response should not be null").isNotNull();
        assertThat(response.statusCode()).isIn(200, 201);
    }

    @When("I retrieve the cart by id")
    public void i_retrieve_the_cart_by_id() {
        assertThat(cartId).as("Cart id").isNotBlank();
        response = CartApi.getCartById(cartId);
    }

    @Then("the retrieved cart matches the stored cart")
    public void the_retrieved_cart_matches_the_stored_cart() {
        GetCartByIdResponse cart = response.as(GetCartByIdResponse.class);
        assertThat(cart.getId()).as("Returned cart id").isEqualTo(cartId);
    }

    @When("I delete the cart")
    public void i_delete_the_cart() {
        assertThat(cartId).as("Cart id").isNotBlank();
        response = CartApi.deleteCartById(cartId);
    }

    @Then("the cart deletion response status is {int}")
    public void the_cart_deletion_response_status_is(int statusCode) {
        assertThat(response).as("Delete cart response should not be null").isNotNull();
        assertThat(response.statusCode()).isEqualTo(statusCode);
    }

    @Given("I have a cart containing {int} units of a product")
    public void i_have_a_cart_containing_units_of_a_product(int quantity) {
        i_have_an_existing_cart();
        a_product_is_available();
        currentRequest = AddItemToCartRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
        Response addResponse = CartApi.addItemToCart(currentRequest, cartId);
        assertThat(addResponse.statusCode()).as("Add item response status").isIn(200, 201);
    }

    @When("I update the cart item quantity to {int}")
    public void i_update_the_cart_item_quantity_to(int quantity) {
        assertThat(cartId).as("Cart id").isNotBlank();
        assertThat(currentRequest).as("Existing cart request").isNotNull();
        currentRequest.setQuantity(quantity);
        response = CartApi.updateQuantityOfItemInCart(currentRequest, cartId);
        assertThat(response.statusCode()).as("Update quantity response status").isIn(200, 201);
    }

    @Then("the cart item quantity is {int}")
    public void the_cart_item_quantity_is(int expectedQuantity) {
        Response cartResponse = CartApi.getCartById(cartId);
        GetCartByIdResponse cart = cartResponse.as(GetCartByIdResponse.class);
        assertThat(cart.getCartItems()).as("Cart items").isNotEmpty();
        Integer actualQuantity = cart.getCartItems().get(0).getQuantity();
        assertThat(actualQuantity).as("Cart quantity").isEqualTo(expectedQuantity);
    }

    @When("I remove the product from the cart")
    public void i_remove_the_product_from_the_cart() {
        assertThat(cartId).as("Cart id").isNotBlank();
        assertThat(productId).as("Product id").isNotBlank();
        response = CartApi.deleteProductFromCart(cartId, productId);
    }

    @Then("the cart has no items")
    public void the_cart_has_no_items() {
        assertThat(response).as("Delete product response should not be null").isNotNull();
        assertThat(response.statusCode()).as("Delete product status code").isEqualTo(204);

        Response cartResponse = CartApi.getCartById(cartId);
        GetCartByIdResponse cart = cartResponse.as(GetCartByIdResponse.class);
        assertThat(cart.getCartItems()).as("Cart items").isEmpty();
    }

    private void ensureCartAndProduct() {
        if (cartId == null) {
            i_have_an_existing_cart();
        }
        if (productId == null) {
            a_product_is_available();
        }
    }
}