package se.mah.af6589.personalfinanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Gustaf Bohlin on 14/09/2017.
 */

public class UserDataBaseInterface extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "USERS";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LASTNAME = "lastname";

    private static final String DATABASE_NAME = "personalfinance.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table " + TABLE_NAME + "(" +
                    COLUMN_USERNAME + " text not null primary key, " +
                    COLUMN_PASSWORD + " text not null, " +
                    COLUMN_NAME + " text not null, " +
                    COLUMN_LASTNAME + " text not null);";

    public UserDataBaseInterface(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public User getUser(String username) {
        User user = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_NAME, COLUMN_LASTNAME}, COLUMN_USERNAME + "=?", new String[]{username}, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            }
        } catch (Exception e) {
            Log.e("DB exception", "getUser");
        }
        return user;
    }

    public boolean addUser(User user) {
        if (getUser(user.getUsername()) == null) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, user.getUsername());
            values.put(COLUMN_PASSWORD, user.getPassword());
            values.put(COLUMN_NAME, user.getName());
            values.put(COLUMN_LASTNAME, user.getLastname());
            db.insert(TABLE_NAME, null, values);
            return true;
        }
        return false;
    }

    public User login(String username, String password) {
        User user = getUser(username);
        if (user != null)
            if (user.getPassword().equals(password))
                return user;
        return null;
    }

    public boolean editUser(User oldUser, User newUser) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, newUser.getUsername());
            values.put(COLUMN_PASSWORD, newUser.getPassword());
            values.put(COLUMN_NAME, newUser.getName());
            values.put(COLUMN_LASTNAME, newUser.getLastname());
            db.update(TABLE_NAME, values, COLUMN_USERNAME + "= '" + oldUser.getUsername() + "'", null);
            return true;
        } catch (Exception e) {
            Log.e("DB exception", "update user");
            Log.e("DB exception", e.getMessage());
        }
        return false;
    }
}
