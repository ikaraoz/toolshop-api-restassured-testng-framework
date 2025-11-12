package api;

import constants.Endpoints;
import io.restassured.response.Response;
import payloads.brands.StoreNewBrandRequest;
import support.BaseSpecProvider;

import static io.restassured.RestAssured.given;

public class BrandApi {
    private BrandApi(){

    }
    public static Response storeNewBrand(StoreNewBrandRequest request){
        return given().spec(BaseSpecProvider.jsonSpec())
                .and()
                .body(request)
                .when()
                .post(Endpoints.BRANDS)
                .then()
                .extract().response();
    }

    public static Response getAllBrands(){
        return given().spec(BaseSpecProvider.jsonSpec())
                .when()
                .get(Endpoints.BRANDS)
                .then()
                .extract().response();
    }

    public static Response getBrandById(String brandId){
        return given().spec(BaseSpecProvider.jsonSpec())
                .pathParam("brandId", brandId)
                .when()
                .get(Endpoints.BRANDS_BY_ID)
                .then()
                .extract().response();
    }

    public static Response deleteBrandById(String brandId){
        return given().spec(BaseSpecProvider.jsonSpec())
                .pathParam("brandId", brandId)
                .when()
                .delete(Endpoints.BRANDS_BY_ID)
                .then()
                .extract().response();
    }


}
