package org.example.tools.network.api;

public final class Endpoints {

    public static final String BRANDS = "/brands";
    public static final String CATEGORIES_TREE = "/categories/tree";
    public static final String PRODUCTS = "/products";

    public static String product(String productId) {
        return "/products/" + productId;
    }

    public static String relatedProducts(String productId) {
        return "/products/" + productId + "/related";
    }

    private Endpoints() {
    }

}
