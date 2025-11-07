package payloads.invoices;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateInvoiceRequest {

    // --- Core Invoice Fields ---
    private String billingStreet;
    private String billingCity;
    private String billingState;
    private String billingCountry;
    private String billingPostalCode;
    private String paymentMethod;   // e.g., "credit_card", "bank_transfer", etc.
    private String cartId;

    // Generic container for polymorphic payment details
    private Object paymentDetails;

    // --- Nested Classes for Payment Methods ---

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CreditCardDetails {
        private String creditCardNumber;
        private String expirationDate; // e.g., "12/29"
        private String cvv;
        private String cardHolderName;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BankTransferDetails {
        private String bankName;
        private String accountName;
        private String accountNumber;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GiftCardDetails {
        private String giftCardNumber;
        private String validationCode;
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BuyNowPayLaterDetails {
        private String monthlyInstallments; // or Integer, if the API expects a number
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CashOnDeliveryDetails {
        // No fields required
    }
}
