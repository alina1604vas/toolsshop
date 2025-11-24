package org.example.tools.pageobject.entity;

import java.util.*;

public class Cart {

    private List<UiCartElement> items = new ArrayList<>();

    public void add(UiCartElement cartElement) {
        items.add(cartElement);
    }

    public List<UiCartElement> getItems() {
        return items;
    }

}
