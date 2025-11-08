package responses.carts;

import lombok.Data;
import java.util.List;

@Data
public class GetCartByIdResponse {
    private String id;
    private Double additionalDiscountPercentage;
    private Double lat;
    private Double lng;
    private List<CartItem> cartItems;

    @Data
    public static class CartItem {
        private String id;
        private Integer quantity;
        private Double discountPercentage;
        private String cartId;
        private String productId;
        private Product product;
    }

    @Data
    public static class Product {
        private String id;
        private String name;
        private String description;
        private Double price;
        private Boolean isLocationOffer;
        private Boolean isRental;
        private String co2Rating;
        private Boolean inStock;
        private Boolean isEcoFriendly;
    }
}
