package api;

import constants.Endpoints;
import io.restassured.response.Response;
import payloads.LoginRequest;
import payloads.RegisterUserRequest;
import payloads.carts.AddItemToCartRequest;
import support.BaseSpecProvider;

import static io.restassured.RestAssured.given;

public class CartApi {
    private CartApi() {
    }

    public static Response createCart() {
        return given().spec(BaseSpecProvider.jsonSpec())
                .when()
                .post(Endpoints.CARTS)
                .then()
                .extract().response();
    }

    public static Response addItemToCart(AddItemToCartRequest request, String id) {
        return given()
                .spec(BaseSpecProvider.jsonSpec())
                .pathParam("id", id)
                .and()
                .body(request)
                .when()
                .post(Endpoints.CARTS_BY_ID)
                .then()
                .extract().response();
    }

    public static Response getCartById(String cartId) {
        return given().spec(BaseSpecProvider.jsonSpec())
                .pathParam("id", cartId)
                .and()
                .when()
                .get(Endpoints.CARTS_BY_ID)
                .then()
                .extract().response();
    }

    public static Response deleteCartById(String cartId) {
        return given().spec(BaseSpecProvider.jsonSpec())
                .pathParam("cartId",  cartId)
                .when()
                .delete(Endpoints.CARTS_BY_CART_ID)
                .then()
                .extract().response();
    }

    public static Response updateQuantityOfItemInCart(AddItemToCartRequest request, String cartId) {
        return given()
                .spec(BaseSpecProvider.jsonSpec())
                .pathParam("cartId", cartId)
                .and()
                .body(request)
                .when()
                .put(Endpoints.CARTS_UPDATE_QUANTITY_BY_ID)
                .then()
                .extract().response();
    }


    public static Response deleteProductFromCart(String cartId, String productId) {
        return given()
                .spec(BaseSpecProvider.jsonSpec())
                .pathParam("cartId", cartId)
                .pathParam("productId", productId)
                .when()
                .delete(Endpoints.CARTS_DELETE_PRODUCT_FROM_CART)
                .then()
                .extract().response();
    }




}

/// carts POST

//package api;
//
//import constants.Endpoints;
//import io.restassured.response.Response;
//import payloads.LoginRequest;
//import payloads.RegisterUserRequest;
//import support.BaseSpecProvider;
//
//import static io.restassured.RestAssured.given;
//
//public final class UserApi {
//    private UserApi() {
//    }
//
//    public static Response login(LoginRequest body) {
//        return given()
//                .spec(BaseSpecProvider.jsonSpec())
//                .body(body)
//                .when()
//                .post(Endpoints.USERS_LOGIN)
//                .then()
//                .extract().response();
//    }
//
//    public static Response register(RegisterUserRequest body) {
//        return given()
//                .spec(BaseSpecProvider.jsonSpec())
//                .body(body)
//                .when()
//                .post(Endpoints.USERS_REGISTER)
//                .then()
//                .extract().response();
//    }
//}
