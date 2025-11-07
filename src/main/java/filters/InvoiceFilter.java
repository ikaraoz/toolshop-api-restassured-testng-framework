package filters;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceFilter {
    private Integer page;
}
