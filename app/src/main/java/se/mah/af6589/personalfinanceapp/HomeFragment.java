package se.mah.af6589.personalfinanceapp;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;

public class HomeFragment extends Fragment {

    private Controller controller;
    private TabLayout tabLayout;
    private View rootView;
    private PieChart pieChart;
    private TextView tvWelcome, tvIncomeSum, tvExpenditureSum;
    private BalanceTextView btvBalance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initializeComponents();
        attachListeners();
        return rootView;
    }

    private void initializeComponents() {
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        pieChart = (PieChart) rootView.findViewById(R.id.piechart);
        pieChart.getLegend().setEnabled(false);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(15);
        tvWelcome = (TextView) rootView.findViewById(R.id.tvWelcome);
        tvIncomeSum = (TextView) rootView.findViewById(R.id.tvIncomeSum);
        tvExpenditureSum = (TextView) rootView.findViewById(R.id.tvExpenditureSum);
        btvBalance = (BalanceTextView) rootView.findViewById(R.id.btvBalance);
    }

    private void attachListeners() {
        tabLayout.addOnTabSelectedListener(new Listener());
    }

    @Override
    public void onResume() {
        super.onResume();
        controller = ((MainActivity)getActivity()).getController();
        controller.updateHomeFragment();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void updateGraph() {
        pieChart.setCenterText(controller.getChartTitle());
        pieChart.setData(controller.getChartData());
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    public void updateFinancialInformation(String incomes, String expenditures, String balance) {
        tvIncomeSum.setText(incomes);
        tvExpenditureSum.setText(expenditures);
        btvBalance.setText(balance);
    }

    public void updateWelcome(String text) {
        tvWelcome.setText(text);
    }

    public int getSelectedTab() {
        return tabLayout.getSelectedTabPosition();
    }

    private class Listener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.d("TABS", String.valueOf(tab.getPosition()));
            if (tab.getPosition() == 0) {
                controller.tabExpenditureClicked();
            } else if (tab.getPosition() == 1) {
                controller.tabIncomeClicked();
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }
}
