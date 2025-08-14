package org.example.mail.entity;

public class ProductCard {

    private String name;
    private String image;
    private String price;

    public ProductCard(String name, String image, String price) {
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


    //String
    public String getPrice() {
        return price;
    }

}
