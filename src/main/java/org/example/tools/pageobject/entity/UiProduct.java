package org.example.tools.pageobject.entity;

import java.util.Objects;

public class UiProduct {

    private String name;
    private String image;
    private String price;
    //private boolean isInStock;

    public UiProduct(String name, String image, String price) {
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public static UiProduct withImage(String name, String image ) {
        return new UiProduct(name, image, null);
    }

    public static UiProduct withPrice(String name, String price) {
        return new UiProduct(name, null, price);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UiProduct that = (UiProduct) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        return "UiProduct{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

}
