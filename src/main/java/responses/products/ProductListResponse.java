package responses.products;

import lombok.Data;
import java.util.List;

@Data
public class ProductListResponse {
    private int currentPage;
    private List<Product> data;
    private int from;
    private int lastPage;
    private int perPage;
    private int to;
    private int total;

    @Data
    public static class Product {
        private String id;
        private String name;
        private String description;
        private double price;
        private boolean isLocationOffer;
        private boolean isRental;
        private String co2Rating;
        private boolean inStock;
        private boolean isEcoFriendly;
        private ProductImage productImage;
        private Category category;
        private Brand brand;
    }

    @Data
    public static class ProductImage {
        private String id;
        private String byName;
        private String byUrl;
        private String sourceName;
        private String sourceUrl;
        private String fileName;
        private String title;
    }

    @Data
    public static class Category {
        private String id;
        private String name;
        private String slug;
    }

    @Data
    public static class Brand {
        private String id;
        private String name;
    }
}
