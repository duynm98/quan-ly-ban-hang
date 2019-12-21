package com.duynm.qlbanhang.data.product;

import android.content.Context;

import com.duynm.qlbanhang.data.StoreDatabase;

import java.util.ArrayList;

/**
 * Created by DuyNM on 15/12/2019
 */
public class ProductController {

    private Context context;

    private StoreDatabase storeDatabase;

    public ProductController(Context context) {
        this.context = context;
        this.storeDatabase = new StoreDatabase(context);
    }
}
