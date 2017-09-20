package se.mah.af6589.personalfinanceapp;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.Calendar;


public class AddTransactionFragment extends DialogFragment {

    private View rootView;
    private Controller controller;
    private Spinner spinnerCategory;
    private EditText etTitle, etAmount;
    private Button btnDate;
    private ImageButton ibTitle, ibCancel, ibSave;
    private boolean expenditure = true;

    private Date date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_transaction, container, false);
        initializeComponents();
        attachListeners();
        return rootView;
    }

    private void initializeComponents() {
        spinnerCategory = (Spinner) rootView.findViewById(R.id.spinner_add_category);
        etTitle = (EditText) rootView.findViewById(R.id.et_add_title);
        etAmount = (EditText) rootView.findViewById(R.id.et_add_amount);
        btnDate = (Button) rootView.findViewById(R.id.btnDate);
        ibTitle = (ImageButton) rootView.findViewById(R.id.ibTitle);
        ibCancel = (ImageButton) rootView.findViewById(R.id.ib_add_cancel);
        ibSave = (ImageButton) rootView.findViewById(R.id.ib_add_save);
    }

    private void attachListeners() {
        Listener listener = new Listener();
        btnDate.setOnClickListener(listener);
        ibTitle.setOnClickListener(listener);
        ibCancel.setOnClickListener(listener);
        ibSave.setOnClickListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        expenditure = true;
        if (controller == null)
            controller = ((MainActivity) getActivity()).getController();
        initializeComponents();
        attachListeners();
        controller.updateAddTransactionFragment(this, expenditure);
    }

    @Override
    public void onPause() {
        super.onPause();
        String spinnerSelection = ((TextView)spinnerCategory.getSelectedView()).getText().toString();
        String title = etTitle.getText().toString();
        float amount = -1;
        try {
            amount = Float.parseFloat(etAmount.getText().toString());
        } catch (Exception e) {}
        controller.setAddTransactionData(expenditure, spinnerSelection, title, amount, date);
    }

    public void setBtnDateText(String text) {
        btnDate.setText(text);
    }

    public void setSelectedDate(Date date) {
        this.date = date;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setSpinnerAdapter(SpinnerAdapter adapter) {
        spinnerCategory.setAdapter(adapter);
    }

    public void setEtTitleText(String text) {
        etTitle.setText(text);
    }

    public void setEtAmountText(String text) {
        etAmount.setText(text);
    }

    public void setIbTitleState(int imageresource, int backgroundcolor) {
        ibTitle.setImageResource(imageresource);
        ibTitle.setBackgroundResource(backgroundcolor);
    }

    public void setSpinnerSelected(int selection) {
        spinnerCategory.setSelection(selection, true);
    }

    public void setIsExpenditure(boolean expenditure) {
        this.expenditure = expenditure;
        controller.updateExpenditureLabel(expenditure);
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view == ibCancel) controller.btnDismiss();
            if (view == ibSave) {
                try {
                    String spinnerSelection = ((TextView)spinnerCategory.getSelectedView()).getText().toString();
                    String title = etTitle.getText().toString();
                    float amount = Float.parseFloat(etAmount.getText().toString());
                    if (title.equals(""))
                        throw new Exception();

                    controller.addTransaction(expenditure, spinnerSelection, title, date, amount);
                } catch (Exception e) {
                    Log.e("AddTransaction", "ERROR parsing data");
                }
            }
            if (view == btnDate) controller.btnDateClicked();
            if (view == ibTitle) {
                expenditure = !expenditure;
                controller.ibTitleClicked(expenditure);
            }
        }
    }
}
