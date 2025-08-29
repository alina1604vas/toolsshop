package org.example.tools.network.entity;

import com.google.gson.annotations.SerializedName;

public class Brand {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("slug")
    private String slug;

    public Long getId() {
        return id;
    }

    public String getBrandName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

}
