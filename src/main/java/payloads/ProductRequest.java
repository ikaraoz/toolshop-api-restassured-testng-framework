package payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequest {
    private String name;
    private String description;
    private double price;
    private int categoryId;
    private int brandId;
    private String productImageId;
    private int isLocationOffer;
    private int isRental;
    private String co2Rating;
}
