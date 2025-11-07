package responses.products;

import lombok.Data;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private double price;
    private boolean isLocationOffer;
    private boolean isRental;
    private String co2Rating;
    private boolean inStock;
    private boolean isEcoFriendly;
    private ProductListResponse.ProductImage productImage;
    private ProductListResponse.Category category;
    private ProductListResponse.Brand brand;
}
