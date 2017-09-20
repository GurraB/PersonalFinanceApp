package se.mah.af6589.personalfinanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class TransactionDatabaseInterface extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "TRANSACTIONS";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_EXPENDITURE = "expenditure";
    public static final String COLUMN_USER = "user";

    private static final String DATABASE_NAME = "personalfinance.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table " + TABLE_NAME + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_DATE + " text not null, " +
                    COLUMN_CATEGORY + " integer not null, " +
                    COLUMN_TITLE + " text not null, " +
                    COLUMN_AMOUNT + " text not null, " +
                    COLUMN_EXPENDITURE + " integer not null, " +
                    COLUMN_USER + " text not null);";

    private static final String QUERY_TRANSACTION_WHERE_EXPENDITURE = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER + " = ? AND " + COLUMN_EXPENDITURE + " = ?";

    public TransactionDatabaseInterface(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public TransactionDatabaseInterface(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
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

    public ArrayList<Transaction> getTransactions(User user) {
        /*ArrayList<Transaction> incomes = new ArrayList<>();
        incomes.add(new Transaction(1, new Date(0), Transaction.Category.Salary, "Salary", 100, false));
        incomes.add(new Transaction(1, new Date(0), Transaction.Category.Salary, "Sossebidrag", 50, false));
        incomes.add(new Transaction(1, new Date(0), Transaction.Category.Other, "Swish", 20, false));
        incomes.add(new Transaction(1, new Date(0), Transaction.Category.Other, "Stuff", 80, false));
        return incomes;*/

        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_DATE, COLUMN_CATEGORY, COLUMN_TITLE, COLUMN_AMOUNT, COLUMN_EXPENDITURE}, COLUMN_USER + "=?", new String[]{user.getUsername()}, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                do {
                    transactions.add(new Transaction(cursor.getInt(0), Date.valueOf(cursor.getString(1)), Transaction.Category.valueOf(cursor.getString(2)), cursor.getString(3), Float.parseFloat(cursor.getString(4)), cursor.getInt(5) == 1));
                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB exception", "getTransactions");
        }
        return transactions;
    }

    public ArrayList<Transaction> getIncomes(User user) {
        ArrayList<Transaction> incomes = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            String args[] = {user.getUsername(), "0"};
            Cursor cursor = db.rawQuery(QUERY_TRANSACTION_WHERE_EXPENDITURE, args);
            if (cursor != null) {
                cursor.moveToFirst();
                do {
                    incomes.add(new Transaction(cursor.getInt(0), Date.valueOf(cursor.getString(1)), Transaction.Category.valueOf(cursor.getString(2)), cursor.getString(3), Float.parseFloat(cursor.getString(4)), cursor.getInt(5) == 1));
                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB exception", "getIncomes");
        }
        return incomes;
    }

    public ArrayList<Transaction> getExpenditures(User user) {
        ArrayList<Transaction> expenditures = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            String args[] = {user.getUsername(), "1"};
            Cursor cursor = db.rawQuery(QUERY_TRANSACTION_WHERE_EXPENDITURE, args);
            if (cursor != null) {
                cursor.moveToFirst();
                do {
                    expenditures.add(new Transaction(cursor.getInt(0), Date.valueOf(cursor.getString(1)), Transaction.Category.valueOf(cursor.getString(2)), cursor.getString(3), Float.parseFloat(cursor.getString(4)), cursor.getInt(5) == 1));
                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB exception", "getExpenditures");
        }
        return expenditures;
    }

    public boolean addTransaction(Transaction transaction, User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, transaction.getDate().toString());
        values.put(COLUMN_CATEGORY, transaction.getCategory().toString());
        values.put(COLUMN_TITLE, transaction.getTitle());
        values.put(COLUMN_AMOUNT, String.valueOf(transaction.getAmount()));
        values.put(COLUMN_EXPENDITURE, (transaction.isExpenditure() ? 1 : 0));
        values.put(COLUMN_USER, user.getUsername());
        db.insert(TABLE_NAME, null, values);
        return true;
    }

    public boolean addTransactionWithId(Transaction transaction, User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, transaction.getId());
        values.put(COLUMN_DATE, transaction.getDate().toString());
        values.put(COLUMN_CATEGORY, transaction.getCategory().toString());
        values.put(COLUMN_TITLE, transaction.getTitle());
        values.put(COLUMN_AMOUNT, String.valueOf(transaction.getAmount()));
        values.put(COLUMN_EXPENDITURE, (transaction.isExpenditure() ? 1 : 0));
        values.put(COLUMN_USER, user.getUsername());
        db.insert(TABLE_NAME, null, values);
        return true;
    }

    public Transaction getTransaction(int id) {
        Transaction transaction = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_DATE, COLUMN_CATEGORY, COLUMN_TITLE, COLUMN_AMOUNT, COLUMN_EXPENDITURE}, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                transaction = new Transaction(cursor.getInt(0), Date.valueOf(cursor.getString(1)), Transaction.Category.valueOf(cursor.getString(2)), cursor.getString(3), Float.parseFloat(cursor.getString(4)), cursor.getInt(5) == 1);
            }
        } catch (Exception e) {
            Log.e("DB exception", "getTransaction");
        }
        return transaction;
    }

    public ArrayList<Transaction> getExpendituresByDate(User user, Date after, Date before) {
        ArrayList<Transaction> expenditures = getExpenditures(user);
        if (after == null && before == null) return expenditures;
        ArrayList<Transaction> expendituresByDate = new ArrayList<>();
        for (Transaction t:expenditures) {
            if (after == null) if (t.getDate().before(before)) expendituresByDate.add(t);
            if (before == null){ if (t.getDate().after(after)) expendituresByDate.add(t); }
            else if (t.getDate().after(after) && t.getDate().before(before)) expendituresByDate.add(t);
        }
        return expendituresByDate;
    }

    public ArrayList<Transaction> getIncomesByDate(User user, Date after, Date before) {
        ArrayList<Transaction> incomes = getIncomes(user);
        if (after == null && before == null) return incomes;
        ArrayList<Transaction> incomesByDate = new ArrayList<>();
        for (Transaction t:incomes) {
            if (after == null) if (t.getDate().before(before)) incomesByDate.add(t);
            if (before == null) { if (t.getDate().after(after)) incomesByDate.add(t); }
            else if (t.getDate().after(after) && t.getDate().before(before)) incomesByDate.add(t);
        }
        return incomesByDate;
    }

    public boolean transferTransactions(User olduser, User newUser) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_USER + "= '" + newUser.getUsername() + "' WHERE " + COLUMN_USER + "= '" + olduser.getUsername() + "'";
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            Log.e("DB exception", "transfer transactions");
            Log.e("DB exception", e.getMessage());
        }
        return false;
    }

    public void remove(int id) {
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        getWritableDatabase().delete(TABLE_NAME, whereClause, whereArgs);
    }
}
