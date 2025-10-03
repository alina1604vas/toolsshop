package org.example.tools.pageobject.entity;

public class UiProduct {

    private  String name = "";
    private  String image = "";
    private  String price = "";


    public UiProduct(String name, String image) {
        this.name = name;
        this.image = image;
    }

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
