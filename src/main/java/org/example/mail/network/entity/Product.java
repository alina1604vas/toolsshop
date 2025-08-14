package org.example.mail.network.entity;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private Double price;

    @SerializedName("product_image")
    private ProductImage productImage;

    @SerializedName("category")
    private Category  category;

    @SerializedName("brand")
    private Brand brand;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public ProductImage getProductImage() {
        return productImage;
    }

    public Category getCategory() {
        return category;
    }

    public Brand getBrand() {
        return brand;
    }
}
