package com.duynm.qlbanhang.data.product;

/**
 * Created by DuyNM on 15/12/2019
 */
public class Product {

    private int id;
    private String name;
    private String description;
    private byte[] image;
    private double price;
    private String type;

    public Product(int id, String name, String description, byte[] image, double price, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.type = type;
    }

    public Product(String name, String description, byte[] image, double price, String type) {
        this.id = -1;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.type = type;
    }

    public Product(String name, double price) {
        this.id = -1;
        this.name = name;
        this.price = price;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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
