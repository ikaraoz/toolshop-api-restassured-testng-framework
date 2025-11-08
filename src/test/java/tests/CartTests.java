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
import responses.carts.GetCartByIdResponse;
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
        String cId = createCart().as(CreateCartResponse.class).getId();
        assertThat(cId).as("cart id").isNotNull();

        //Get product list and Id
        String pId = getProductId();

        // Build post cart request payload
        AddItemToCartRequest request = AddItemToCartRequest.builder()
                .productId(pId)
                .quantity(10)
                .build();

        Response resp = addItemToCart(request, cId);
        assertThat(resp.statusCode()).isIn(201, 200);
    }

    @Test(groups = {"regression"})
    public void get_cart_by_id_returns_specific_cart() {
        String createCartId = createCart().as(CreateCartResponse.class).getId();
        String getCartByIdCartId = getCartById(createCartId).as(CreateCartResponse.class).getId();
        assertThat(createCartId.equals(getCartByIdCartId)).isTrue();
    }

    @Test(groups = {"regression"})
    public void delete_cart_by_id_removes_cart() {
        String createCartId = createCart().as(CreateCartResponse.class).getId();
        Response response = deleteCartById(createCartId);
        assertThat(response.statusCode()).isIn(204);
    }

    @Test(groups = {"regression"})
    public void update_quantity_of_item_updates_cart() {
        int productQuantityBeforeUpdate = 10;
        int productQuantityAfterUpdate = 20;
        // Cart Id
        String cId = createCart().as(CreateCartResponse.class).getId();

        //Get product list and Id
        String pId = getProductId();

        // Build post cart addItemToCartRequest payload
        AddItemToCartRequest addItemToCartRequest = AddItemToCartRequest.builder()
                .productId(pId)
                .quantity(productQuantityBeforeUpdate)
                .build();

        Response resp = addItemToCart(addItemToCartRequest, cId);
        Response getCart = getCartById(cId);
        assertThat(getCart.as(GetCartByIdResponse.class).getCartItems().getFirst().getQuantity()).isEqualTo(productQuantityBeforeUpdate);
        assertThat(resp.statusCode()).isIn(201, 200);

        // update the quantity
        addItemToCartRequest.setQuantity(productQuantityAfterUpdate);
        updateQuantityOfItemInCart(addItemToCartRequest, cId);
        Response updateResponse = getCartById(cId);
        assertThat(updateResponse.as(GetCartByIdResponse.class).getCartItems().getFirst().getQuantity()).isEqualTo(productQuantityAfterUpdate);
        assertThat(resp.statusCode()).isIn(201, 200);
    }

    @Test(groups = {"regression"})
    public void delete_product_from_cart_removes_product() {
        int productQuantity = 10;
        // Cart Id
        String cId = createCart().as(CreateCartResponse.class).getId();

        //Get product list and id
        String pId = getProductId();
        System.out.println("pId = " + pId);

        // Build post cart addItemToCartRequest payload
        AddItemToCartRequest addItemToCartRequest = AddItemToCartRequest.builder()
                .productId(pId)
                .quantity(productQuantity)
                .build();

        Response resp = addItemToCart(addItemToCartRequest, cId);
        Response getCart = getCartById(cId);
        assertThat(getCart.as(GetCartByIdResponse.class).getCartItems().getFirst().getProductId()).isEqualTo(pId);
        assertThat(resp.statusCode()).isIn(201, 200);

        // remove product from the cart
        Response removeProductResponse = deleteProductFromCart(cId, pId);
        assertThat(removeProductResponse.statusCode()).isEqualTo(204);

        //Get cart and assert that it is empty
        getCart = getCartById(cId);
        assertThat(getCart.as(GetCartByIdResponse.class).getCartItems().isEmpty()).isTrue();
    }


    private static String getProductId() {
        //Get product list and Id
        Response pList = listProducts();
        ProductListResponse p = pList.as(ProductListResponse.class);
        String pId = p.getData().getFirst().getId();
        return pId;
    }


}

