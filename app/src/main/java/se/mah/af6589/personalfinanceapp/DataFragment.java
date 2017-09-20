package se.mah.af6589.personalfinanceapp;


import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Date;

public class DataFragment extends Fragment {

    private User user;
    private int currentFragment;
    private Transaction addTransactionData;
    private String barcodeId;
    private String addBarcodeId;
    private Transaction removedTransaction;
    private int removedPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(int currentFragment) {
        this.currentFragment = currentFragment;
    }

    public void setAddTransactionData(Transaction addTransactionData) {
        this.addTransactionData = addTransactionData;
    }

    public Transaction getAddTransactionData() {
        return addTransactionData;
    }

    public void setBarcodeId(String barcodeId) {
        this.barcodeId = barcodeId;
    }

    public String getBarcodeId() {
        return barcodeId;
    }

    public String getAddBarcodeId() {
        return addBarcodeId;
    }

    public void setAddBarcodeId(String addBarcodeId) {
        this.addBarcodeId = addBarcodeId;
    }

    public void setRemovedTransaction(Transaction removedTransaction) {
        this.removedTransaction = removedTransaction;
    }

    public Transaction getRemovedTransaction() {
        return removedTransaction;
    }

    public void setRemovedPosition(int removedPosition) {
        this.removedPosition = removedPosition;
    }

    public int getRemovedPosition() {
        return removedPosition;
    }
}
