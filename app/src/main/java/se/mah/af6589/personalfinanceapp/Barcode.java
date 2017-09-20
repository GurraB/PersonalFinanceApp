package se.mah.af6589.personalfinanceapp;

/**
 * Created by Gustaf Bohlin on 18/09/2017.
 */

public class Barcode {

    private String id;
    private String name;
    private Transaction.Category category;
    private float amount;

    public Barcode(String id, String name, Transaction.Category category, float amount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Transaction.Category getCategory() {
        return category;
    }

    public void setCategory(Transaction.Category category) {
        this.category = category;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
