package org.example.tools.pageobject.entity;

import java.util.Objects;

public class UiCartElement {
    private UiProduct product;
    private int quantity;
    private double subtotal;

//decided to get rid of the quantity as cart UI merges duplicate products and my expected list stores duplicates as separate entries.
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true; // check if it's the same object first
//        if (!(o instanceof UiCartElement that)) return false; // use pattern matching for cleaner code
//        return quantity == that.quantity &&
//                Objects.equals(product, that.product);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(product, quantity);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UiCartElement that)) return false;
        return Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }

    @Override
    public String toString() {
        return "UiCartElement{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    public UiCartElement (UiProduct product, int quantity ) {
        this.product = product;
        this.quantity = quantity;

    }

    public UiCartElement (UiProduct product, int quantity, double subtotal) {
        this.product = product;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public UiProduct getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
