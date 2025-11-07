package tests;

import api.CartApi;
import api.ProductApi.*;
import base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import payloads.LoginRequest;
import payloads.RegisterUserRequest;
import payloads.carts.AddItemToCartRequest;
import responses.carts.CreateCartResponse;
import responses.products.ProductListResponse;
import utils.DataFactory;

import static api.CartApi.*;
import static api.ProductApi.listProducts;
import static org.assertj.core.api.Assertions.assertThat;

public class CartTests extends BaseTest {
    @Test(groups = {"regression"})
    public void create_cart_creates_new_cart() {
        Response resp = createCart();
        assertThat(resp.statusCode()).isIn(201);
        CreateCartResponse c = resp.as(CreateCartResponse.class);
        assertThat(c.getId()).isNotNull();
    }

    @Test(groups = {"regression"})
    public void addItemToCart_succeeds_whenRequestIsValid() {
        // Cart Id
        CreateCartResponse cart = createCart().as(CreateCartResponse.class);
        String cId = cart.getId();
        assertThat(cId).as("cart id").isNotNull();

        //Get product list and Id
        Response pList = listProducts();
        ProductListResponse p = pList.as(ProductListResponse.class);
        String pId = p.getData().getFirst().getId();

        // Build post cart request payload
        AddItemToCartRequest request = AddItemToCartRequest.builder()
                .productId(pId)
                .quantity(10)
                .build();

        Response resp = addItemToCart(request, cId);
        assertThat(resp.statusCode()).isIn(201, 200);
//        assertThat(resp.)
    }
}

