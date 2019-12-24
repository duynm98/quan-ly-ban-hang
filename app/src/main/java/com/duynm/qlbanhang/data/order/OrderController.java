package com.duynm.qlbanhang.data.order;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.duynm.qlbanhang.data.StoreDatabase;
import com.duynm.qlbanhang.data.product.Product;
import com.duynm.qlbanhang.data.product.ProductController;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DuyNM on 24/12/2019
 */
public class OrderController {

    private final static String TABLE_NAME = "orders";

    private final static String COL_ID = "id";
    private final static String COL_CUSTOMER = "customer";
    private final static String COL_ADDRESS = "address";
    private final static String COL_PHONE = "phone";
    private final static String COL_LIST_PRODUCT = "list_product";
    private final static String COL_STATUS = "status";
    private final static String COL_DATE = "date";

    private Context context;
    private StoreDatabase storeDatabase;

    private CompositeDisposable compositeDisposable;

    public OrderController(Context context) {
        this.context = context;
        this.storeDatabase = new StoreDatabase(context);

        compositeDisposable = new CompositeDisposable();
    }

    public int getSumPrice(Order order) {
        int sumPrice = 0;
        ProductController productController = new ProductController(context, null);
        ArrayList<OrderDetail> allProducts = order.getListProduct();
        for (OrderDetail orderDetail : allProducts) {
            Product product = productController.selectProductByID(orderDetail.getProductID());
            sumPrice += product.getPrice() * orderDetail.getAmount();
        }

        return sumPrice;
    }

    public void getAllOrder(final OnDataListener onDataListener) {
        compositeDisposable.clear();
        compositeDisposable.add(
                selectAllOrderObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ArrayList<Order>>() {
                            @Override
                            public void accept(ArrayList<Order> orders) throws Exception {
                                if (onDataListener != null)
                                    onDataListener.onData(orders);
                            }
                        })
        );
    }

    public Order getOrderByID(int id) {
        SQLiteDatabase database = storeDatabase.getWritableDatabase();
        Cursor cursor = database.query(
                TABLE_NAME,
                new String[]{COL_ID, COL_CUSTOMER, COL_ADDRESS, COL_PHONE, COL_LIST_PRODUCT, COL_STATUS, COL_DATE},
                COL_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
        if (cursor != null)
            cursor.moveToFirst();

        Order order = new Order(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getInt(5) != 0,
                cursor.getLong(6)
        );

        cursor.close();

        return order;
    }

    public void addOrder(Order order, OnDataListener onDataListener) {
        SQLiteDatabase database = storeDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_CUSTOMER, order.getCustomer());
        contentValues.put(COL_ADDRESS, order.getAddress());
        contentValues.put(COL_PHONE, order.getPhone());
        contentValues.put(COL_LIST_PRODUCT, order.getJsonListProduct());
        contentValues.put(COL_STATUS, order.isPaid() ? 1 : 0);
        contentValues.put(COL_DATE, order.getDate());

        database.insert(TABLE_NAME, null, contentValues);

        if (onDataListener != null)
            getAllOrder(onDataListener);
        database.close();
    }

    public void updateOrder(Order order) {
        SQLiteDatabase database = storeDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_CUSTOMER, order.getCustomer());
        contentValues.put(COL_ADDRESS, order.getAddress());
        contentValues.put(COL_PHONE, order.getPhone());
        contentValues.put(COL_LIST_PRODUCT, order.getJsonListProduct());
        contentValues.put(COL_STATUS, order.isPaid() ? 1 : 0);
        contentValues.put(COL_DATE, order.getDate());

        database.update(
                TABLE_NAME,
                contentValues,
                COL_ID + " = ?",
                new String[]{String.valueOf(order.getId())}
        );
    }

    public void deleteOrder(Order order, OnDataListener onDataListener) {
        SQLiteDatabase database = storeDatabase.getWritableDatabase();

        database.delete(TABLE_NAME, COL_ID + " = ?",
                new String[]{String.valueOf(order.getId())});

        getAllOrder(onDataListener);
        database.close();
    }

    private Observable<ArrayList<Order>> selectAllOrderObservable() {
        return Observable.fromCallable(new Callable<ArrayList<Order>>() {
            @Override
            public ArrayList<Order> call() throws Exception {
                return selectAllOrder();
            }
        });
    }

    private ArrayList<Order> selectAllOrder() {
        return selectOrdersByQuery("SELECT * FROM " + TABLE_NAME);
    }

    private ArrayList<Order> selectOrdersByQuery(String query) {
        ArrayList<Order> orders = new ArrayList<>();

        try {
            SQLiteDatabase database = storeDatabase.getWritableDatabase();

            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                orders.add(orderByCursor(cursor));
                cursor.moveToNext();
            }

            cursor.close();
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }

        return orders;
    }

    private Order orderByCursor(Cursor cursor) {
        return new Order(
                cursor.getInt(cursor.getColumnIndex(COL_ID)),
                cursor.getString(cursor.getColumnIndex(COL_CUSTOMER)),
                cursor.getString(cursor.getColumnIndex(COL_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(COL_PHONE)),
                cursor.getString(cursor.getColumnIndex(COL_LIST_PRODUCT)),
                cursor.getInt(cursor.getColumnIndex(COL_STATUS)) != 0,
                cursor.getLong(cursor.getColumnIndex(COL_DATE))
        );
    }

    public interface OnDataListener {
        public void onData(ArrayList<Order> listOrder);
    }
}
