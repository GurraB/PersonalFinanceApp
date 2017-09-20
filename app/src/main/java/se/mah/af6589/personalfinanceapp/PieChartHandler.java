package se.mah.af6589.personalfinanceapp;

import android.graphics.Color;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

/**
 * Created by Gustaf Bohlin on 12/09/2017.
 */

public class PieChartHandler {

    private int colors[];
    private Controller controller;

    public PieChartHandler(int colors[], Controller controller) {
        this.colors = colors;
        this.controller = controller;
    }

    public PieData getData(ArrayList<Transaction> data) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        float food = 0, leisure = 0, travel = 0, accomodation = 0, salary = 0, other = 0;
        for (Transaction transaction:data) {
            switch (transaction.getCategory()) {
                case Food:
                    food += transaction.getAmount();
                    break;

                case Leisure:
                    leisure += transaction.getAmount();
                    break;

                case Travel:
                    travel += transaction.getAmount();
                    break;

                case Accomodation:
                    accomodation += transaction.getAmount();
                    break;

                case Salary:
                    salary += transaction.getAmount();
                    break;

                case Other:
                    other += transaction.getAmount();
                    break;
            }
        }
        /*PieEntry foodEntry = new PieEntry(food, "Food");
        PieEntry leisureEntry = new PieEntry(leisure, "Leisure");
        PieEntry travelEntry = new PieEntry(travel, "Travel");
        PieEntry accomodationEntry = new PieEntry(accomodation, "Accomodation");
        PieEntry salaryEntry = new PieEntry(salary, "Salary");
        PieEntry otherEntry = new PieEntry(other, "Other");*/
        PieEntry foodEntry = new PieEntry(food, controller.getFood());
        PieEntry leisureEntry = new PieEntry(leisure, controller.getLeisure());
        PieEntry travelEntry = new PieEntry(travel, controller.getTravel());
        PieEntry accomodationEntry = new PieEntry(accomodation, controller.getAccomodation());
        PieEntry salaryEntry = new PieEntry(salary, controller.getSalary());
        PieEntry otherEntry = new PieEntry(other, controller.getOther());
        if (food > 0)   entries.add(foodEntry);
        if (leisure > 0)   entries.add(leisureEntry);
        if (travel > 0)   entries.add(travelEntry);
        if (accomodation > 0)   entries.add(accomodationEntry);
        if (salary > 0)   entries.add(salaryEntry);
        if (other > 0)   entries.add(otherEntry);

        PieDataSet pieDataSet = new PieDataSet(entries, "Expenses");
        pieDataSet.setColors(colors);
        pieDataSet.setDrawValues(false);
        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }
}
