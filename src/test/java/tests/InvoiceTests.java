package tests;

import api.CartApi;
import base.BaseTest;
import api.InvoiceApi;
import api.ProductApi;
import filters.InvoiceFilter;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import payloads.invoices.*;

import static org.assertj.core.api.Assertions.assertThat;

public class InvoiceTests extends BaseTest {

    @Test(groups = {"smoke"})
    public void list_invoices_first_page_ok() {
        InvoiceFilter filter = InvoiceFilter.builder().page(1).build();
        Response res = InvoiceApi.listInvoices(filter);
        assertThat(res.statusCode()).isEqualTo(200);
    }

    @Test(groups = {"regression"})
    public void create_invoice_returns_201_or_200() {
// First call create cart api to get cartId
        Response cart = CartApi.createCart();
        String cartId = cart.jsonPath().getString("id");
        assertThat(cartId).as("cart id").isNotNull();

        CreateInvoiceRequest invoice = CreateInvoiceRequest.builder()
                .billingStreet("123 Main St")
                .billingCity("Testville")
                .billingState("CA")
                .billingCountry("US")
                .billingPostalCode("90001")
                .paymentMethod("bank-transfer")
                .cartId(cartId)
                .paymentDetails(
                        CreateInvoiceRequest.BankTransferDetails.builder()
                                .accountName("John Dow")
                                .accountNumber("12345678")
                                .bankName("BankX")
                                .build()
                )
                .build();

//                                .creditCardNumber("4111111111111111")
//                .expirationDate("12/29")
//                .cvv("123")
//                .cardHolderName("Jane Doe")
//                .build()

//        InvoiceRequest payload = InvoiceRequest.builder()
//                .productId(Integer.parseInt(productId))
//                .quantity(1)
//                .build();

        Response res = InvoiceApi.createInvoice(invoice);
        assertThat(res.statusCode()).isIn(200, 201);
    }
}
