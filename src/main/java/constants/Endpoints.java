package constants;

public final class Endpoints {
    private Endpoints() {
    }

    public static final String USERS_LOGIN = "/users/login";
    public static final String USERS_REGISTER = "/users/register";

    public static final String PRODUCTS = "/products";
    public static final String PRODUCT_BY_ID = "/products/{id}";

    public static final String INVOICES = "/invoices";
    public static final String INVOICE_BY_ID = "/invoices/{id}";

    public static final String CARTS = "/carts";
    public static final String CARTS_BY_ID = "/carts/{id}";



}
