package org.example.tools.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductsPerPage {

    @SerializedName("current_page")
    private Long currentPage;

    @SerializedName("data")
    private List<Product> products;

    @SerializedName("from")
    private int from;

    @SerializedName("last_page")
    private int lastPage;

    @SerializedName("per_page")
    private int perPage;

    @SerializedName("to")
    private int to;

    @SerializedName("total")
    private int totalProducts;

    public Long getCurrentPage() {
        return currentPage;
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getTotalProducts() {
        return totalProducts;
    }
}
