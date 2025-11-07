package payloads.carts;

import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddItemToCartRequest {
    private String productId;
    private int quantity;

}
