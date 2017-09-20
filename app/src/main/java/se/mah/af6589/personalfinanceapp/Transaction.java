package se.mah.af6589.personalfinanceapp;

import java.sql.Date;

/**
 * Created by Gustaf Bohlin on 12/09/2017.
 */

public class Transaction {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public enum Category {
        Food, Leisure, Travel, Accomodation, Other, Salary
    }

    private Date date;
    private int id;
    private boolean expenditure;
    private String title;
    private float amount;
    private Category category;


    public Transaction(int id, Date date, Category category, String title, float amount, boolean expenditure) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.title = title;
        this.amount = amount;
        this.expenditure = expenditure;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isExpenditure() {
        return expenditure;
    }

    public void setExpenditure(boolean expenditure) {
        this.expenditure = expenditure;
    }

}
