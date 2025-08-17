package org.example.mail.network.entity;

import java.util.List;

public class HomeData {

    private List<Category> categories;
    private List<Brand> brands;
    private ProductsPerPage productsPerPage;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public ProductsPerPage getProductsPerPage() {
        return productsPerPage;
    }

    public void setProductsPerPage(ProductsPerPage products) {
        this.productsPerPage = products;
    }

}
