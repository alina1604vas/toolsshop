package org.example.tools.pageobject.entity;

public class UiProduct {

    private final String name;
    private final String image;
    private final String price;

    public UiProduct(String name, String image, String price) {
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

}
