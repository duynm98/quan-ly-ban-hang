package com.duynm.qlbanhang.data.order;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by DuyNM on 24/12/2019
 */
public class Order {

    private int id;
    private String customer;
    private String address;
    private String phone;
    private String jsonListProduct;
    private boolean paid;
    private long date;

    public Order(int id, String customer, String address, String phone, String jsonListProduct, boolean paid, long date) {
        this.id = id;
        this.customer = customer;
        this.address = address;
        this.phone = phone;
        this.jsonListProduct = jsonListProduct;
        this.paid = paid;
        this.date = date;
    }

    public Order(String customer, String address, String phone, String jsonListProduct, boolean paid, long date) {
        this.customer = customer;
        this.address = address;
        this.phone = phone;
        this.jsonListProduct = jsonListProduct;
        this.paid = paid;
        this.date = date;
    }

    public Order(int id, String customer, String address, String phone, ArrayList<OrderDetail> listProduct, boolean paid, long date) {
        this.id = id;
        this.customer = customer;
        this.address = address;
        this.phone = phone;
        this.jsonListProduct = new Gson().toJson(listProduct);
        this.paid = paid;
        this.date = date;
    }

    public Order(String customer, String address, String phone, ArrayList<OrderDetail> listProduct, boolean paid, long date) {
        this.customer = customer;
        this.address = address;
        this.phone = phone;
        this.jsonListProduct = new Gson().toJson(listProduct);
        this.paid = paid;
        this.date = date;
    }

    public ArrayList<OrderDetail> getListProduct() {
        return new Gson().fromJson(jsonListProduct, new TypeToken<ArrayList<OrderDetail>>() {
        }.getType());
    }

    public void setListProduct(ArrayList<OrderDetail> listProduct) {
        this.jsonListProduct = new Gson().toJson(listProduct);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJsonListProduct() {
        return jsonListProduct;
    }

    public void setJsonListProduct(String jsonListProduct) {
        this.jsonListProduct = jsonListProduct;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
