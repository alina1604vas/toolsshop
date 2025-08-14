package org.example.mail.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {

    @SerializedName("id")
    private Long id;

    @SerializedName("parent_id")
    private Long parentId;

    @SerializedName("name")
    private String name;

    @SerializedName("slug")
    private String slug;

    @SerializedName("sub_categories")
    private List<Category> subCategories;

    public Long getId() {
        return id;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

}
