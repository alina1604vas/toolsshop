package org.example.tools.network.entity;

import com.google.gson.annotations.SerializedName;

public class ProductImage {

    @SerializedName("id")
    private Long id;

    @SerializedName("by_name")
    private String byName;

    @SerializedName("by_url")
    private String byUrl;

    @SerializedName("source_name")
    private String sourceName;

    @SerializedName("source_url")
    private String sourceUrl;

    @SerializedName("file_name")
    private String fileName;

    @SerializedName("title")
    private String title;

    public Long getId() {
        return id;
    }

    public String getByName() {
        return byName;
    }

    public String getByUrl() {
        return byUrl;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public String getTitle() {
        return title;
    }
}
