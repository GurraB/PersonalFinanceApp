package se.mah.af6589.personalfinanceapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.github.mikephil.charting.data.PieData;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class Controller {

    FragmentActivity activity;
    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;

    private HomeFragment homeFragment;
    private TransactionFragment transactionFragment;
    private BarcodesFragment barcodesFragment;
    private AddTransactionFragment addTransactionFragment;
    private EditUserFragment editUserFragment;
    private DataFragment dataFragment;

    private UserDataBaseInterface userDB;
    private TransactionDatabaseInterface transactionDB;
    private BarCodeDatabaseInterface barcodeDB;

    private PieChartHandler pieChartHandler;

    private Drawable food, accomodation, travel, leisure, salary, other;

    private DatePickerDialog datepicker_add, datepicker_after, datepicker_before;

    private int colors[];

    private BarcodeDialogFragment barcodeDialogFragment;

    public Controller(FragmentActivity activity) {
        this.activity = activity;
        initializeResources();
        initializeFragments();
        initializeDatabase();
        pieChartHandler = new PieChartHandler(colors, this);
    }

    private void initializeResources() {
        Resources res = ((AppCompatActivity) activity).getResources();

        colors = res.getIntArray(R.array.chart_colors);
        food = res.getDrawable(R.drawable.ic_food, null);
        accomodation = res.getDrawable(R.drawable.ic_home, null);
        travel = res.getDrawable(R.drawable.ic_travel, null);
        leisure = res.getDrawable(R.drawable.ic_leisure, null);
        salary = res.getDrawable(R.drawable.ic_salary, null);
        other = res.getDrawable(R.drawable.ic_other, null);
    }

    private void initializeDatabase() {
        userDB = new UserDataBaseInterface((AppCompatActivity) activity);
        transactionDB = new TransactionDatabaseInterface((AppCompatActivity) activity);
        barcodeDB = new BarCodeDatabaseInterface((AppCompatActivity) activity);
    }

    private void initializeFragments() {
        signInFragment = new SignInFragment();
        signInFragment.setController(this);
        signUpFragment = new SignUpFragment();
        signUpFragment.setController(this);

        homeFragment = new HomeFragment();
        homeFragment.setController(this);
        transactionFragment = new TransactionFragment();
        transactionFragment.setController(this);
        barcodesFragment = new BarcodesFragment();
        barcodesFragment.setController(this);
        addTransactionFragment = new AddTransactionFragment();
        addTransactionFragment.setController(this);
        editUserFragment = new EditUserFragment();
        editUserFragment.setController(this);

        dataFragment = activity.addDataFragment();
    }

    //----------------------------------------------------------------------------------------------

    public void setSignInFragment() {
        activity.setFragment(signInFragment, false);
    }

    public void setSignUpFragment() {
        activity.setFragment(signUpFragment, false);
    }

    public void login(LoginActivity loginActivity, String username, String password) {
        User user = userDB.login(username, password);
        if (user != null) {
            Intent i = new Intent();
            i.putExtra("USER", user);
            loginActivity.setResult(Activity.RESULT_OK, i);
            loginActivity.finish();
        } else {
            signInFragment.loginFailed();
        }
    }

    public void register(LoginActivity loginActivity, String username, String password, String name, String lastname) {
        User newUser = new User(username, password, name, lastname);
        if (userDB.addUser(newUser)) {
            login(loginActivity, username, password);
        } else {
            signUpFragment.registrationFailed();
        }
    }

    public void userLoggedIn(Intent data) {
        User user = data.getParcelableExtra("USER");
        dataFragment.setUser(user);
        updateUserInformation();
    }

    public boolean isLoggedIn() {
        return dataFragment.getUser() != null;
    }

    //----------------------------------------------------------------------------------------------

    public void openCurrentFragment() {
        switch (dataFragment.getCurrentFragment()) {
            case 0:
                Log.v("CONTROLLER", "opening Home fragment");
                activity.setFragment(homeFragment, false);
                break;
            case 1:
                Log.v("CONTROLLER", "opening Expenditure fragment");
                activity.setFragment(transactionFragment, true);
                break;
            case 2:
                Log.v("CONTROLLER", "opening Income fragment");
                activity.setFragment(transactionFragment, true);
                break;
            case 3:
                Log.v("CONTROLLER", "opening Income fragment");
                activity.setFragment(barcodesFragment, true);
                break;
        }
    }

    public void setHomeFragment() {
        dataFragment.setCurrentFragment(0);
        openCurrentFragment();
    }

    public void setExpenditureFragment() {
        if (dataFragment.getCurrentFragment() == 2) {
            dataFragment.setCurrentFragment(1);
            activity.restartFragment(transactionFragment);
        } else {
            dataFragment.setCurrentFragment(1);
            openCurrentFragment();
        }
    }

    public void setIncomeFragment() {
        if (dataFragment.getCurrentFragment() == 1) {
            dataFragment.setCurrentFragment(2);
            activity.restartFragment(transactionFragment);
        } else {
            dataFragment.setCurrentFragment(2);
            openCurrentFragment();
        }
    }

    public void setBarcodeFragment() {
        dataFragment.setCurrentFragment(3);
        openCurrentFragment();
    }

    public void updateHomeFragment() {
        homeFragment.updateGraph();
        try {
            homeFragment.updateWelcome("Welcome " + dataFragment.getUser().getName());
            ArrayList<Transaction> incomes = transactionDB.getIncomes(dataFragment.getUser());
            ArrayList<Transaction> expenditures = transactionDB.getExpenditures(dataFragment.getUser());
            float incomeSum = 0;
            for (Transaction i : incomes) {
                incomeSum += i.getAmount();
            }
            float expenditureSum = 0;
            for (Transaction e : expenditures) {
                expenditureSum += e.getAmount();
            }
            float balance = incomeSum - expenditureSum;
            homeFragment.updateFinancialInformation(String.valueOf(incomeSum) + " kr", String.valueOf(expenditureSum) + " kr", String.valueOf(balance));
        } catch (Exception e) {
            Log.e("CONTROLLER", "app crashed when getting username");
        }
    }

    public PieData getChartData() {
        return ((homeFragment.getSelectedTab() == 0) ? pieChartHandler.getData(transactionDB.getExpenditures(dataFragment.getUser())) : pieChartHandler.getData(transactionDB.getIncomes(dataFragment.getUser())));
    }

    public void navExpenditureClicked() {
        setExpenditureFragment();
    }

    public void navIncomeClicked() {
        setIncomeFragment();
    }

    public void navBarcodeClicked() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE"); // "PRODUCT_MODE for bar codes // "QR_CODE_MODE"

            ((AppCompatActivity) activity).startActivityForResult(intent, 0);
        } catch (Exception e) {
            Log.e("Main", "Error starting barcode app");
        }
    }

    public void navSavedBarcodesClicked() {
        setBarcodeFragment();
    }

    public void updateTransactionFragment(TransactionFragment transactionFragment) {
        this.transactionFragment = transactionFragment;
        ArrayList<Transaction> transactions = null;
        String toolbarTitle = null;
        if (dataFragment.getCurrentFragment() == 1) {
            transactions = transactionDB.getExpendituresByDate(dataFragment.getUser(), null, null);
            toolbarTitle = "Expenditures";
        } else if (dataFragment.getCurrentFragment() == 2) {
            transactions = transactionDB.getIncomes(dataFragment.getUser());
            toolbarTitle = "Incomes";
        }
        TransactionAdapter adapter = new TransactionAdapter(transactions, this);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(((AppCompatActivity) activity), R.array.spinner_choices_time, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        this.transactionFragment.setReclerViewAdapter(adapter);
        this.transactionFragment.setToolbarTitle(toolbarTitle);
        this.transactionFragment.setSpinnerAdapter(spinnerAdapter);
    }

    public void tabExpenditureClicked() {
        homeFragment.updateGraph();
    }

    public void tabIncomeClicked() {
        homeFragment.updateGraph();
    }

    public Drawable getFood() {
        return food;
    }

    public Drawable getAccomodation() {
        return accomodation;
    }

    public Drawable getTravel() {
        return travel;
    }

    public Drawable getLeisure() {
        return leisure;
    }

    public Drawable getSalary() {
        return salary;
    }

    public Drawable getOther() {
        return other;
    }

    public void fabClicked() {
        dataFragment.setAddTransactionData(null);
        showAddTransactionFragment();
    }

    private void showAddTransactionFragment() {
        addTransactionFragment.show(((AppCompatActivity) activity).getFragmentManager(), "add");
    }

    public void addTransaction(boolean expenditure, String spinner_selection, String title, Date date, float amount) {
        Transaction transaction = new Transaction(0, date, Transaction.Category.valueOf(spinner_selection), title, amount, expenditure);
        transactionDB.addTransaction(transaction, dataFragment.getUser());
        dataFragment.setAddTransactionData(null);
        if (dataFragment.getAddBarcodeId() != null) {
            Barcode barcode = new Barcode(dataFragment.getAddBarcodeId(), title, Transaction.Category.valueOf(spinner_selection), amount);
            barcodeDB.addBarcode(barcode);
            dataFragment.setAddBarcodeId(null);
        }
        addTransactionFragment.dismiss();
        dataChanged();
    }

    private void dataChanged() {
        switch (dataFragment.getCurrentFragment()) {
            case 0:
                updateHomeFragment();
                break;
            case 1:
                ArrayList<Transaction> exp = transactionDB.getExpendituresByDate(dataFragment.getUser(), transactionFragment.getAfter(), transactionFragment.getBefore());
                transactionFragment.addItem(exp, exp.size());
                break;
            case 2:
                ArrayList<Transaction> inc = transactionDB.getIncomesByDate(dataFragment.getUser(), transactionFragment.getAfter(), transactionFragment.getBefore());
                transactionFragment.addItem(inc, inc.size());
                break;
            case 3:
                updateBarcodesFragment();
                break;
        }
    }

    public void btnDateClicked() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        datepicker_add = new DatePickerDialog(((AppCompatActivity) activity), new Listener(), year, month, day);
        datepicker_add.show();
    }

    public void removeRetainedFragments(FragmentManager fragmentManager) {
        fragmentManager.beginTransaction().remove(dataFragment).commit();
    }

    public void updateAddTransactionFragment(AddTransactionFragment fragment, boolean expenditure) {
        addTransactionFragment = fragment;
        if (dataFragment.getAddTransactionData() != null) {
            Transaction t = dataFragment.getAddTransactionData();
            addTransactionFragment.setIsExpenditure(t.isExpenditure());
            addTransactionFragment.setEtTitleText(t.getTitle());
            if (t.getAmount() != -1)
                addTransactionFragment.setEtAmountText(Float.toString(t.getAmount()));

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(((AppCompatActivity) activity), (t.isExpenditure() ? R.array.spinner_categories_expenditure : R.array.spinner_categories_income), R.layout.spinner_item_add);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            addTransactionFragment.setSpinnerAdapter(adapter);

            if (t.isExpenditure()) {
                switch (t.getCategory()) {
                    case Food:
                        addTransactionFragment.setSpinnerSelected(0);
                        break;
                    case Leisure:
                        addTransactionFragment.setSpinnerSelected(1);
                        break;
                    case Travel:
                        addTransactionFragment.setSpinnerSelected(2);
                        break;
                    case Accomodation:
                        addTransactionFragment.setSpinnerSelected(3);
                        break;
                    case Other:
                        addTransactionFragment.setSpinnerSelected(4);
                        break;
                }
            } else {
                switch (t.getCategory()) {
                    case Salary:
                        addTransactionFragment.setSpinnerSelected(0);
                        break;
                    case Other:
                        addTransactionFragment.setSpinnerSelected(1);
                        break;
                }
            }
            dataFragment.setAddTransactionData(null);
        } else {
            addTransactionFragment.setEtTitleText("");
            addTransactionFragment.setEtAmountText("");
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(((AppCompatActivity) activity), (expenditure ? R.array.spinner_categories_expenditure : R.array.spinner_categories_income), R.layout.spinner_item_add);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            addTransactionFragment.setSpinnerAdapter(adapter);
        }
        Date today = new Date(getTodaysDateInMillis());
        addTransactionFragment.setBtnDateText(today.toString());
        addTransactionFragment.setSelectedDate(today);
    }

    public void updateUserInformation() {
        try {
            ((MainActivity) activity).setNavName(dataFragment.getUser().getName());
            ((MainActivity) activity).setNavLastName(dataFragment.getUser().getLastname());
        } catch (Exception e) {
            Log.e("CONTROLLER", e.getMessage());
        }
    }

    public void transactionSearch(Date after, Date before) {
        ArrayList<Transaction> transactions = null;
        if (dataFragment.getCurrentFragment() == 1) {
            transactions = transactionDB.getExpendituresByDate(dataFragment.getUser(), after, before);
        } else if (dataFragment.getCurrentFragment() == 2) {
            transactions = transactionDB.getIncomesByDate(dataFragment.getUser(), after, before);
        }
        TransactionAdapter adapter = new TransactionAdapter(transactions, this);
        transactionFragment.setReclerViewAdapter(adapter);
    }

    private long getTodaysDateInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public void dateSpinnerItemClick(int i) {
        Date after = null, before = null;
        switch (i) {
            case 0:
                //No selection
                break;
            case 1:
                after = new Date(getTodaysDateInMillis() - DateUtils.DAY_IN_MILLIS * 364 - 1); // -1 to get the transactions of the actual day too
                before = new Date(getTodaysDateInMillis() + 1); // +1 to get transactions of the actual day too
                Log.v("SHOW TRANSACTIONS FROM", "364 days");
                break;
            case 2:
                after = new Date(getTodaysDateInMillis() - DateUtils.DAY_IN_MILLIS * 30 - 1); // -1 to get the transactions of the actual day too
                before = new Date(getTodaysDateInMillis() + 1); // +1 to get transactions of the actual day too
                Log.v("SHOW TRANSACTIONS FROM", "30 days");
                break;
            case 3:
                after = new Date(getTodaysDateInMillis() - DateUtils.DAY_IN_MILLIS * 7 - 1); // -1 to get the transactions of the actual day too
                before = new Date(getTodaysDateInMillis() + 1); // +1 to get transactions of the actual day too
                Log.v("SHOW TRANSACTIONS FROM", "7 days");
                break;
            case 4:
                after = new Date(getTodaysDateInMillis() - DateUtils.DAY_IN_MILLIS - 1); // -1 to get the transactions of the actual day too
                before = new Date(getTodaysDateInMillis() + 1); // +1 to get transactions of the actual day too
                Log.v("SHOW TRANSACTIONS FROM", "today");
                break;
            case 5:
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                datepicker_after = new DatePickerDialog(((AppCompatActivity) activity), new Listener(), year, month, day);
                datepicker_after.show();
                break;
        }
        transactionFragment.setAfter(after);
        transactionFragment.setBefore(before);
        transactionFragment.setTvAfterText((after != null ? after.toString() : "YYYY-MM-DD"));
        transactionFragment.setTvBeforeText((before != null ? before.toString() : "YYYY-MM-DD"));
    }

    public void editUserClicked() {
        Log.v("CONTROLLER", "EDIT USER");
        editUserFragment.show(((AppCompatActivity) activity).getFragmentManager(), "edit");
    }

    public void updateUser(String username, String password, String name, String lastname) {
        User newUser = new User(username, password, name, lastname);
        userDB.editUser(dataFragment.getUser(), newUser);
        transactionDB.transferTransactions(dataFragment.getUser(), newUser);
        dataFragment.setUser(newUser);
        updateUserInformation();
        if (dataFragment.getCurrentFragment() == 0)
            homeFragment.updateWelcome("Welcome " + dataFragment.getUser().getName());
        editUserFragment.dismiss();
    }

    public void updateEditUserFragment(EditUserFragment editUserFragment) {
        this.editUserFragment = editUserFragment;
        editUserFragment.setUser(dataFragment.getUser());
    }

    public void ibTitleClicked(boolean expenditure) {
        if (!expenditure) {
            addTransactionFragment.setIbTitleState(R.drawable.ic_income_white, R.color.colorPrimary);
        } else {
            addTransactionFragment.setIbTitleState(R.drawable.ic_expenditure_white, R.color.colorBad);
        }
        updateAddTransactionFragment(addTransactionFragment, expenditure);
    }

    public void failedActivityResult() {
        if (dataFragment.getUser() == null) {   //User didn't login
            Intent i = new Intent((MainActivity) activity, LoginActivity.class);
            ((AppCompatActivity) activity).startActivityForResult(i, MainActivity.requestLoginCode);
        }
    }

    public void barcodeScanned(String content) {
        Barcode barcode = barcodeDB.getBarCodeById(content);
        if (barcode != null) {
            dataFragment.setAddTransactionData(new Transaction(1, new Date(getTodaysDateInMillis()), barcode.getCategory(), barcode.getName(), barcode.getAmount(), true));
        }
        else {
            dataFragment.setAddTransactionData(null);
            dataFragment.setAddBarcodeId(content);
            Toast.makeText(((AppCompatActivity) activity), "Could not recognise barcode", Toast.LENGTH_SHORT).show();
        }
        showAddTransactionFragment();
    }

    public void setAddTransactionData(boolean expenditure, String selectedItem, String title, float amount, Date date) {
        dataFragment.setAddTransactionData(new Transaction(1, date, Transaction.Category.valueOf(selectedItem), title, amount, expenditure));
    }

    public void btnDismiss() {
        dataFragment.setAddTransactionData(null);
        dataFragment.setAddBarcodeId(null);
        addTransactionFragment.dismiss();
    }

    public void updateExpenditureLabel(boolean expenditure) {
        if (!expenditure) {
            addTransactionFragment.setIbTitleState(R.drawable.ic_income_white, R.color.colorPrimary);
        } else {
            addTransactionFragment.setIbTitleState(R.drawable.ic_expenditure_white, R.color.colorBad);
        }
    }

    public void updateBarcodesFragment() {
        ArrayList<Barcode> barcodes = barcodeDB.getAllBarcodes();
        BarcodeListAdapter adapter = new BarcodeListAdapter((AppCompatActivity) activity, barcodes);
        barcodesFragment.setLvAdapter(adapter);
    }

    public void showBarcode(Barcode item) {
        barcodeDialogFragment = new BarcodeDialogFragment();
        barcodeDialogFragment.show(((AppCompatActivity) activity).getFragmentManager(), "barcode");
        dataFragment.setBarcodeId(item.getId());
    }

    public void setBarcodeId(BarcodeDialogFragment barcodeDialogFragment) {
        this.barcodeDialogFragment = barcodeDialogFragment;
        barcodeDialogFragment.setBarcode(dataFragment.getBarcodeId());
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void swipe(int adapterPosition, int id, Date after, Date before) {
        dataFragment.setRemovedTransaction(transactionDB.getTransaction(id));
        dataFragment.setRemovedPosition(adapterPosition);
        transactionDB.remove(id);
        Snackbar.make(((AppCompatActivity)activity).findViewById(R.id.fab), "Deleted #" + String.valueOf(id), Snackbar.LENGTH_LONG)
                .setAction("Undo", new Listener())
                .setActionTextColor(Color.GREEN)
                .show();

        ArrayList<Transaction> transactions = null;
        if (dataFragment.getCurrentFragment() == 1) {
            transactions = transactionDB.getExpendituresByDate(dataFragment.getUser(), after, before);
        } else if (dataFragment.getCurrentFragment() == 2) {
            transactions = transactionDB.getIncomesByDate(dataFragment.getUser(), after, before);
        }
        transactionFragment.removeItem(transactions, adapterPosition);
    }

    private void restoreRemovedTransaction() {
        Transaction transaction = dataFragment.getRemovedTransaction();
        if (transaction != null) {
            transactionDB.addTransactionWithId(transaction, dataFragment.getUser());
        }

        ArrayList<Transaction> transactions = null;
        if (dataFragment.getCurrentFragment() == 1) {
            transactions = transactionDB.getExpendituresByDate(dataFragment.getUser(), transactionFragment.getAfter(), transactionFragment.getBefore());
        } else if (dataFragment.getCurrentFragment() == 2) {
            transactions = transactionDB.getIncomesByDate(dataFragment.getUser(), transactionFragment.getAfter(), transactionFragment.getBefore());
        }
        transactionFragment.addItem(transactions, dataFragment.getRemovedPosition());
        dataFragment.setRemovedTransaction(null);
        dataFragment.setRemovedPosition(-1);
    }

    public String getChartTitle() {
        return homeFragment.getSelectedTab() == 0 ? "Expenditure" : "Income";
    }

    private class Listener implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            try {
                if (datePicker == datepicker_add.getDatePicker()) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    calendar.getTimeInMillis();
                    Date date = new Date(calendar.getTimeInMillis());
                    addTransactionFragment.setSelectedDate(date);
                    addTransactionFragment.setBtnDateText(date.toString());
                }
            } catch (Exception e) {
            }

            try {
                if (datePicker == datepicker_after.getDatePicker()) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    calendar.getTimeInMillis();
                    Date date = new Date(calendar.getTimeInMillis());
                    transactionFragment.setAfter(date);
                    transactionFragment.setTvAfterText(date.toString());
                    datepicker_after.dismiss();

                    datepicker_before = new DatePickerDialog(((AppCompatActivity) activity), this, year, month, day);
                    datepicker_before.show();
                }
            } catch (Exception e) {
            }

            try {
                if (datePicker == datepicker_before.getDatePicker()) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    calendar.getTimeInMillis();
                    Date date = new Date(calendar.getTimeInMillis());
                    transactionFragment.setBefore(date);
                    transactionFragment.setTvBeforeText(date.toString());
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void onClick(View view) {
            restoreRemovedTransaction();
        }
    }
}