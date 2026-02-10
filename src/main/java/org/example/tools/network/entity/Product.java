package org.example.tools.network.entity;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private Double price;

    @SerializedName("is_location_offer")
    private boolean is_location_offer;

    @SerializedName("is_rental")
    private boolean is_rental;

    @SerializedName("product_image")
    private ProductImage productImage;

    @SerializedName("category")
    private Category  category;

    @SerializedName("brand")
    private Brand brand;

    public String getId() {
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

    public boolean isIs_location_offer() {
        return is_location_offer;
    }

    public boolean isIs_rental() {
        return is_rental;
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
