package org.example.tools.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductsPerPage {

    @SerializedName("current_page")
    private Long currentPage;

    @SerializedName("data")
    private List<Product> products;

    public Long getCurrentPage() {
        return currentPage;
    }

    public List<Product> getProducts() {
        return products;
    }

}
