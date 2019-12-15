package com.duynm.qlbanhang.data.product;

/**
 * Created by DuyNM on 15/12/2019
 */
public class Product {

    private int id;
    private String name;
    private String description;
    private String imagePath;
    private double price;
    private String type;

    public Product(int id, String name, String description, String imagePath, double price, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.price = price;
        this.type = type;
    }

    public Product(String name, String description, String imagePath, double price, String type) {
        this.id = -1;
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.price = price;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
