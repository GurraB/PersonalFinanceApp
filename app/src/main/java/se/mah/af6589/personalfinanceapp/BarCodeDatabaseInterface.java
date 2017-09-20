package se.mah.af6589.personalfinanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Gustaf Bohlin on 18/09/2017.
 */

public class BarCodeDatabaseInterface extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "BARCODES";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_AMOUNT = "amount";

    private static final String DATABASE_NAME = "personalfinance.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CREATE =
            "create table " + TABLE_NAME + "(" +
                    COLUMN_ID + " text primary key not null, " +
                    COLUMN_CATEGORY + " text not null, " +
                    COLUMN_TITLE + " text not null, " +
                    COLUMN_AMOUNT + " text not null);";


    public BarCodeDatabaseInterface(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public BarCodeDatabaseInterface(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BarCodeDatabaseInterface(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addBarcode(Barcode barcode) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, barcode.getId());
        values.put(COLUMN_CATEGORY, barcode.getCategory().toString());
        values.put(COLUMN_TITLE, barcode.getName());
        values.put(COLUMN_AMOUNT, String.valueOf(barcode.getAmount()));
        db.insert(TABLE_NAME, null, values);
        return true;
    }

    public Barcode getBarCodeById(String id) {
        Barcode barcode = null;
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=?", new String[]{id});
            if (cursor != null) {
                cursor.moveToFirst();
                barcode = new Barcode(cursor.getString(0), cursor.getString(2), Transaction.Category.valueOf(cursor.getString(1)), Float.parseFloat(cursor.getString(3)));
            }
        } catch (Exception e) {
            Log.e("DB EXCEPTION", "GET BARCODE");
        }
        return barcode;
    }

    public ArrayList<Barcode> getAllBarcodes() {
        ArrayList<Barcode> barcodes = new ArrayList<>();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
            if (cursor != null) {
                cursor.moveToFirst();
                do {
                    barcodes.add(new Barcode(cursor.getString(0), cursor.getString(2), Transaction.Category.valueOf(cursor.getString(1)), Float.parseFloat(cursor.getString(3))));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB EXCEPTION", "GET BARCODES");
        }
        return barcodes;
    }
}
