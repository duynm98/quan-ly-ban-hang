package com.duynm.qlbanhang.data.order;

import android.content.Context;

import com.duynm.qlbanhang.data.product.Product;
import com.duynm.qlbanhang.data.product.ProductController;

/**
 * Created by DuyNM on 24/12/2019
 */
public class OrderDetail {

    private int productID;
    private int amount;

    public OrderDetail(int productID, int amount) {
        this.productID = productID;
        this.amount = amount;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
