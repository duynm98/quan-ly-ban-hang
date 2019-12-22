package com.duynm.qlbanhang.data.product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.duynm.qlbanhang.data.StoreDatabase;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DuyNM on 15/12/2019
 */
public class ProductController {

    public final static Double MAX_PRICE = 9999999999D;

    private final static String TABLE_NAME = "product";

    private final static String COL_ID = "id";
    private final static String COL_NAME = "name";
    private final static String COL_DESCRIPTION = "description";
    private final static String COL_IMAGE = "image";
    private final static String COL_PRICE = "price";
    private final static String COL_TYPE = "type";

    public enum SortType {
        NAME_INC, NAME_DEC, PRICE_INC, PRICE_DEC
    }

    //Filter
    private SortType sortType = SortType.NAME_INC;
    private String type = "";
    private double minPrice = -1;
    private double maxPrice = MAX_PRICE;

    private Context context;
    private ProductNavigator productNavigator;
    private StoreDatabase storeDatabase;

    private CompositeDisposable compositeDisposable;

    public ProductController(Context context, ProductNavigator productNavigator) {
        this.context = context;
        this.productNavigator = productNavigator;
        this.storeDatabase = new StoreDatabase(context);

        compositeDisposable = new CompositeDisposable();
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void resetFilter() {
        type = "";
        minPrice = -1;
        maxPrice = MAX_PRICE;
    }

    public void getAllProducts() {
        compositeDisposable.clear();
        compositeDisposable.add(
                selectAllProductsObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ArrayList<Product>>() {
                            @Override
                            public void accept(ArrayList<Product> products) {
                                if (productNavigator != null) productNavigator.displayProducts(products);
                            }
                        })
        );
    }

    public void getProductsByKeyword(String keyword) {
        compositeDisposable.clear();
        compositeDisposable.add(
                selectProductsByKeywordObservable(keyword)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ArrayList<Product>>() {
                            @Override
                            public void accept(ArrayList<Product> products) {
                                if (productNavigator != null) productNavigator.displayProducts(products);
                            }
                        })
        );
    }

    private Observable<ArrayList<Product>> selectProductsByKeywordObservable(final String keyword) {
        return Observable.fromCallable(new Callable<ArrayList<Product>>() {
            @Override
            public ArrayList<Product> call() throws Exception {
                return selectProductsByKeyword(keyword);
            }
        });
    }

    private Observable<ArrayList<Product>> selectAllProductsObservable() {
        return Observable.fromCallable(new Callable<ArrayList<Product>>() {
            @Override
            public ArrayList<Product> call() throws Exception {
                return selectAllProducts();
            }
        });
    }

    private ArrayList<Product> selectProductsByQuery(String query) {
        String filterType = "";
        if (type != null && !type.isEmpty())
            filterType = " WHERE type LIKE '" + type + "'";

        String filterPrice = "";
        String prefig = filterType.isEmpty() ? " WHERE" : " AND";
        if (minPrice > 0 && maxPrice < MAX_PRICE) {
            filterPrice = " price BETWEEN " + minPrice + " AND " + maxPrice;
        } else if (minPrice < 0) {
            filterPrice = " price < " + maxPrice;
        } else if (maxPrice >= MAX_PRICE) {
            filterPrice = " price > " + minPrice;
        }
        if (!filterPrice.isEmpty()) filterPrice = prefig + filterPrice;

        String filterSortType = "";
        switch (sortType) {
            case NAME_INC:
                filterSortType = " ORDER BY " + COL_NAME;
                break;
            case NAME_DEC:
                filterSortType = " ORDER BY " + COL_NAME + " DESC";
                break;
            case PRICE_INC:
                filterSortType = " ORDER BY " + COL_PRICE;
                break;
            case PRICE_DEC:
                filterSortType = " ORDER BY " + COL_PRICE + " DESC";
                break;
        }
        query += filterType + filterPrice + filterSortType;
        Log.d("__PRODUCT", "Query = " + query);
        ArrayList<Product> products = new ArrayList<>();

        try {
            SQLiteDatabase database = storeDatabase.getWritableDatabase();

            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                products.add(productByCursor(cursor));
                cursor.moveToNext();
            }

            cursor.close();
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }

        return products;
    }

    private ArrayList<Product> selectProductsByKeyword(String keyword) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE name LIKE '%" + keyword + "%'";
        return selectProductsByQuery(query);
    }

    public ArrayList<String> selectAllType() {
        String query = "SELECT DISTINCT type FROM " + TABLE_NAME;
        ArrayList<String> allTypes = new ArrayList<>();
        try {
            SQLiteDatabase database = storeDatabase.getWritableDatabase();

            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String type = cursor.getString(cursor.getColumnIndex(COL_TYPE));
                if (type != null && !type.isEmpty())
                    allTypes.add(cursor.getString(cursor.getColumnIndex(COL_TYPE)));
//                products.add(productByCursor(cursor));
                cursor.moveToNext();
            }

            cursor.close();
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }

        return allTypes;
    }

    public void addProduct(Product product) {
        SQLiteDatabase database = storeDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

//        contentValues.put(COL_ID, product.getId());
        contentValues.put(COL_NAME, product.getName());
        contentValues.put(COL_DESCRIPTION, product.getDescription());
        contentValues.put(COL_IMAGE, product.getImage());
        contentValues.put(COL_PRICE, product.getPrice());
        contentValues.put(COL_TYPE, product.getType());

        database.insert(TABLE_NAME, null, contentValues);
    }

    public Product selectProductByID(int id) {
        SQLiteDatabase database = storeDatabase.getWritableDatabase();

        Cursor cursor = database.query(
                TABLE_NAME,
                new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_IMAGE, COL_PRICE, COL_TYPE},
                COL_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
        if (cursor != null)
            cursor.moveToFirst();

        Product product = new Product(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getBlob(3),
                Double.parseDouble(cursor.getString(4)),
                cursor.getString(5)
        );

        cursor.close();

        return product;
    }

    private ArrayList<Product> selectAllProducts() {
        String query = "SELECT * FROM " + TABLE_NAME;
        return selectProductsByQuery(query);
    }

    private Product productByCursor(Cursor cursor) {
        return new Product(
                cursor.getInt(cursor.getColumnIndex(COL_ID)),
                cursor.getString(cursor.getColumnIndex(COL_NAME)),
                cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION)),
                cursor.getBlob(cursor.getColumnIndex(COL_IMAGE)),
                cursor.getDouble(cursor.getColumnIndex(COL_PRICE)),
                cursor.getString(cursor.getColumnIndex(COL_TYPE))
        );
    }

    public int getProductCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase database = storeDatabase.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int update(Product product) {
        SQLiteDatabase database = storeDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_NAME, product.getName());
        contentValues.put(COL_DESCRIPTION, product.getDescription());
        contentValues.put(COL_IMAGE, product.getImage());
        contentValues.put(COL_PRICE, product.getPrice());
        contentValues.put(COL_TYPE, product.getType());

        // updating row
        return database.update(TABLE_NAME, contentValues, COL_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }

    public void deleteProduct(Product product) {
        SQLiteDatabase database = storeDatabase.getWritableDatabase();

        database.delete(TABLE_NAME, COL_ID + " = ?",
                new String[]{String.valueOf(product.getId())});

        getAllProducts();
        database.close();
    }
}
