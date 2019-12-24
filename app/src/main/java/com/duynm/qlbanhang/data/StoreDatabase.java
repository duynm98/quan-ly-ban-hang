package com.duynm.qlbanhang.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DuyNM on 15/12/2019
 */
public class StoreDatabase extends SQLiteOpenHelper {

    public final static int DATABASE_VERSION = 1;
    public final static String DATABASE_NAME = "store.db";

    public StoreDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS product (" +
                "id INTEGER PRIMARY KEY NOT NULL," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "image BLOB," +
                "price DOUBLE NOT NULL," +
                "type TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS orders (" +
                "id INTEGER PRIMARY KEY NOT NULL," +
                "customer TEXT NOT NULL," +
                "address TEXT NOT NULL," +
                "phone TEXT NOT NULL," +
                "list_product TEXT NOT NULL," +
                "status INTEGER NOT NULL," +
                "date LONG NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onCreate(sqLiteDatabase);
    }
}
