package tests;

import api.BrandApi;
import api.ProductApi;
import base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import payloads.brands.StoreNewBrandRequest;
import responses.brands.GetAllBrandOrBrandsResponse;
import responses.brands.StoreNewBrandResponse;
import responses.carts.CreateCartResponse;
import utils.DataFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.random.RandomGenerator;

import static api.CartApi.createCart;
import static api.CartApi.deleteCartById;
import static org.assertj.core.api.Assertions.assertThat;
import static api.BrandApi.*;


public class BrandTests extends BaseTest {

    @Test(groups = {"regression"})
    public void store_new_brand_returns_201() {
        String name = DataFactory.randomString(4);
        String slug = DataFactory.randomString(10);
        StoreNewBrandRequest request = StoreNewBrandRequest.builder()
                .slug(slug)
                .name(name)
                .build();

        Response response = BrandApi.storeNewBrand(request);
        assertThat(response.statusCode()).isIn(201);
        StoreNewBrandResponse sresponse = response.as(StoreNewBrandResponse.class);
        assertThat(sresponse.getSlug().equalsIgnoreCase(slug)).isTrue();
        assertThat(sresponse.getName().equalsIgnoreCase(name)).isTrue();
        assertThat(sresponse.getId()).isNotNull().isNotBlank();
    }

    @Test(groups = {"regression"})
    public void get_all_brands_list_all_brands_with_status_code_200() {
        Response response = BrandApi.getAllBrands();
        assertThat(response.statusCode()).isIn(200);
        List<GetAllBrandOrBrandsResponse> brands = Arrays.asList(response.as(GetAllBrandOrBrandsResponse[].class));
        for (GetAllBrandOrBrandsResponse b : brands) {
            assertThat(b.getSlug()).isNotNull().isNotNull();
            assertThat(b.getName()).isNotNull().isNotNull();
            assertThat(b.getId()).isNotNull().isNotNull();
        }

    }

    @Test(groups = {"regression"})
    public void get_brand_by_id_ok() {
        String id = Arrays.asList(getAllBrands().as(GetAllBrandOrBrandsResponse[].class)).getFirst().getId();
        Response res = BrandApi.getBrandById(id);

        assertThat(res.statusCode()).isEqualTo(200);
        assertThat(res.as(GetAllBrandOrBrandsResponse.class).getId().equals(id)).isTrue();
        assertThat(res.as(GetAllBrandOrBrandsResponse.class).getName()).isNotNull().isNotBlank();
        assertThat(res.as(GetAllBrandOrBrandsResponse.class).getSlug()).isNotNull().isNotBlank();
    }

    @Test(groups = {"regression"})
    public void delete_brand_by_id_returns_204() {
        String id = Arrays.asList(getAllBrands().as(GetAllBrandOrBrandsResponse[].class)).getFirst().getId();

        Response dResponse = BrandApi.deleteBrandById(id);
        assertThat(dResponse.statusCode()).isIn(204);

    }
}
