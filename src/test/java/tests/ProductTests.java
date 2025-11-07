package tests;

import base.BaseTest;
import api.ProductApi;
import filters.ProductFilter;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import payloads.ProductRequest;
import responses.products.ProductListResponse;
import utils.DataFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTests extends BaseTest {

    @Test(groups = {"smoke"})
    public void list_products_ok_default() {
        Response res = ProductApi.listProducts();
    }

    @Test(groups = {"regression"})
    public void list_products_with_filters_ok() {
        ProductFilter filter = ProductFilter.builder()
                .isRental("0")
                .page(1)
                .sort("price:asc")
                .build();

        Response res = ProductApi.listProducts(filter);
        assertThat(res.statusCode()).isEqualTo(200);
        assertThat(res.jsonPath().getList("data")).isNotNull();
    }

    @Test(groups = {"regression"})
    public void get_product_by_id_ok() {
        Response list = ProductApi.listProducts();
        String id = list.jsonPath().getString("data[0].id");
        assertThat(id).as("first product id").isNotNull();

        Response res = ProductApi.getProductById(id);
        assertThat(res.statusCode()).isEqualTo(200);
        assertThat(res.jsonPath().getString("id")).isEqualTo(id);
    }

    @Test(groups = {"regression"})
    public void create_product_returns_201_or_200() {
        ProductRequest request = ProductRequest.builder()
                .name("API Product " + DataFactory.randomString(6))
                .description("Automated product")
                .price(49.99)
                .categoryId(2)
                .brandId(2)
                .productImageId(DataFactory.randomString(10))
                .isLocationOffer(1)
                .isRental(0)
                .co2Rating("A")
                .build();

        Response res = ProductApi.createProduct(request);
        assertThat(res.statusCode()).isIn(200, 201);
    }
}
