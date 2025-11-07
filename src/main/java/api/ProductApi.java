package api;

import constants.Endpoints;
import filters.ProductFilter;
import io.restassured.response.Response;
import payloads.ProductRequest;
import responses.products.ProductListResponse;
import responses.products.ProductResponse;
import support.BaseSpecProvider;

import static io.restassured.RestAssured.given;

public final class ProductApi {

    private ProductApi() {
    }

    /**
     * Fetches the list of products with optional query parameters.
     *
     * @param filter Product filter parameters (nullable)
     * @return Deserialized ProductListResponse object
     */
    public static Response listProducts(ProductFilter filter) {
        var req = given().spec(BaseSpecProvider.jsonSpec());

        if (filter != null) {
            if (filter.getByBrand() != null) req.queryParam("by_brand", filter.getByBrand());
            if (filter.getByCategory() != null) req.queryParam("by_category", filter.getByCategory());
            if (filter.getIsRental() != null) req.queryParam("is_rental", filter.getIsRental());
            if (filter.getEcoFriendly() != null) req.queryParam("eco_friendly", filter.getEcoFriendly());
            if (filter.getBetween() != null) req.queryParam("between", filter.getBetween());
            if (filter.getSort() != null) req.queryParam("sort", filter.getSort());
            if (filter.getPage() != null) req.queryParam("page", filter.getPage());
        }

        return req.when()
                .get(Endpoints.PRODUCTS)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    /** Convenience method to list all products without filters */
    public static Response listProducts() {
        return listProducts(null);
    }

    /**
     * Fetches a single product by ID.
     *
     * @param id Product ID
     * @return Deserialized ProductResponse object
     */
    public static Response getProductById(String id) {
        return given()
                .spec(BaseSpecProvider.jsonSpec())
                .pathParam("id", id)
                .when()
                .get(Endpoints.PRODUCT_BY_ID)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    /**
     * Creates a new product.
     *
     * @param payload ProductRequest payload
     * @return Deserialized ProductResponse object
     */
    public static Response createProduct(ProductRequest payload) {
        return given()
                .spec(BaseSpecProvider.jsonSpec())
                .body(payload)
                .when()
                .post(Endpoints.PRODUCTS)
                .then()
                .statusCode(201)
                .extract()
                .response();
    }
}
