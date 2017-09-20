package se.mah.af6589.personalfinanceapp;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;


public class TransactionFragment extends Fragment {

    private View rootView;
    private Controller controller;
    private CollapsingToolbarLayout appbar;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private FloatingActionButton fabSearch;
    private Date before, after;
    private TextView tvAfter, tvBefore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_transaction, container, false);
        initializeComponents();
        attachListeners();
        return rootView;
    }

    private void initializeComponents() {
        appbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_transaction);
        spinner = (Spinner) rootView.findViewById(R.id.spinner_datepick);
        fabSearch = (FloatingActionButton) rootView.findViewById(R.id.fab_search);
        tvAfter = (TextView) rootView.findViewById(R.id.tvStartDate);
        tvBefore = (TextView) rootView.findViewById(R.id.tvStopDate);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        TransactionItemDecoration itemDecoration = new TransactionItemDecoration(25);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void attachListeners() {
        Listener listener = new Listener();
        fabSearch.setOnClickListener(listener);
        spinner.setOnItemSelectedListener(listener);
        ItemTouchHelper touchy = new ItemTouchHelper(new Touchy(0, ItemTouchHelper.RIGHT));
        touchy.attachToRecyclerView(recyclerView);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void onResume() {
        super.onResume();
        controller = ((MainActivity) getActivity()).getController();
        controller.updateTransactionFragment(this);
    }

    public void setReclerViewAdapter(TransactionAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void setToolbarTitle(String title) {
        appbar.setTitle(title);
    }

    public void setSpinnerAdapter(SpinnerAdapter adapter) {
        spinner.setAdapter(adapter);
    }

    public void setTvAfterText(String text) {
        tvAfter.setText(text);
    }

    public void setTvBeforeText(String text) {
        tvBefore.setText(text);
    }

    public void setBefore(Date before) {
        this.before = before;
    }

    public Date getBefore() {
        return before;
    }

    public void setAfter(Date after) {
        this.after = after;
    }

    public Date getAfter() {
        return after;
    }

    public void updateTransactions(ArrayList<Transaction> transactions) {
        ((TransactionAdapter)recyclerView.getAdapter()).updateData(transactions);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void removeItem(ArrayList<Transaction> transactions, int adapterPosition) {
        ((TransactionAdapter)recyclerView.getAdapter()).updateData(transactions);
        recyclerView.getAdapter().notifyItemRemoved(adapterPosition);
    }

    public void addItem(ArrayList<Transaction> transactions, int adapterPosition) {
        ((TransactionAdapter)recyclerView.getAdapter()).updateData(transactions);
        recyclerView.getAdapter().notifyItemInserted(adapterPosition);
    }


    private class Listener implements View.OnClickListener, AdapterView.OnItemSelectedListener {

        @Override
        public void onClick(View view) {
            controller.transactionSearch(after, before);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            controller.dateSpinnerItemClick(i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private class Touchy extends ItemTouchHelper.SimpleCallback {

        public Touchy(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            controller.swipe(viewHolder.getAdapterPosition(), ((TransactionAdapter.ViewHolder) viewHolder)._id, after, before);
        }
    }
}
