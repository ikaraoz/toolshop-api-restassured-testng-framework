package filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductFilter {
    private String byBrand;
    private String byCategory;
    private String isRental;
    private String ecoFriendly;
    private String between;
    private String sort;
    private Integer page;
}
