package org.example.tools.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {

    @SerializedName("id")
    private String id;

    @SerializedName("parent_id")
    private String parentId;

    @SerializedName("name")
    private String name;

    @SerializedName("slug")
    private String slug;

    @SerializedName("sub_categories")
    private List<Category> subCategories;

    public String getId() {
        return id;
    }

    public String getParentId() {
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
