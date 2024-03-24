package com.example.shoppingapp.model;

import com.google.firebase.firestore.DocumentId;

public class Product {
    @DocumentId
    String productId;
    String title,iamgeUrl,description;
    int price,quantity;

    public Product(){

    }

    public Product(String productId, String title, String iamgeUrl, String description, int price, int quantity) {
        this.productId = productId;
        this.title = title;
        this.iamgeUrl = iamgeUrl;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIamgeUrl() {
        return iamgeUrl;
    }

    public void setIamgeUrl(String iamgeUrl) {
        this.iamgeUrl = iamgeUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
