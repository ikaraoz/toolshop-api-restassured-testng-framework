package tests;

import api.ProductApi;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.testng.annotations.Test;
import responses.products.ProductListResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTestsDS {

    @Test(description = "Verify list products returns a valid product list")
    @Description("Ensures /products endpoint returns HTTP 200 and a non-empty list of products")
    public void list_products_ok_default() {
        ProductListResponse response = listProducts();

        // ✅ Validate general structure
        assertThat(response)
                .as("Product list response should not be null")
                .isNotNull();

        assertThat(response.getData())
                .as("Product list should not be empty")
                .isNotEmpty();

        // ✅ Validate first product structure
        ProductListResponse.Product first = response.getData().get(0);
        System.out.println("first = " + first);
        assertThat(first.getId()).as("Product ID").isNotBlank();
        assertThat(first.getName()).as("Product name").isNotBlank();
        assertThat(first.getPrice()).as("Product price").isGreaterThan(0);
        assertThat(first.getCategory().getName()).as("Category name").isNotBlank();

        // ✅ Validate metadata
        assertThat(response.getCurrentPage()).isEqualTo(1);
        assertThat(response.getTotal()).isGreaterThan(0);
    }

    @Step("Fetch product list from /products endpoint")
    private ProductListResponse listProducts() {
        return ProductApi.listProducts()
                .then()
                .statusCode(200)
                .extract()
                .as(ProductListResponse.class);
    }
}
