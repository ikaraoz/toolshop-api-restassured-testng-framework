package payloads.invoices;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceRequest {
    private int productId;

    private int quantity;
}
