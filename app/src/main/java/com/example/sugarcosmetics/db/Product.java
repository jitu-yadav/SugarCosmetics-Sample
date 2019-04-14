package com.example.sugarcosmetics.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "product", indices = {@Index(value = {"product_id"},
        unique = true)})
public class Product {

    @PrimaryKey
    @ColumnInfo(name = "product_id")
    private long product_id;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "image_url_list")
    private String imageUrlList;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "product_details_url")
    private String productDetailsUrl;

    @ColumnInfo(name = "body_desc")
    private String productDescription;

    @ColumnInfo(name = "product_type")
    private String productType;

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long productId) {
        this.product_id = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductDetailsUrl() {
        return productDetailsUrl;
    }

    public void setProductDetailsUrl(String productDetailsUrl) {
        this.productDetailsUrl = productDetailsUrl;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(String imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

}
