package payloads.brands;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class StoreNewBrandRequest {
    private String name;
    private String slug;

}