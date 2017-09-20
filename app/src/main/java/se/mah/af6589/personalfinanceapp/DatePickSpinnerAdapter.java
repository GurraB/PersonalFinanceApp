package se.mah.af6589.personalfinanceapp;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * Created by Gustaf Bohlin on 13/09/2017.
 */

public class DatePickSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private String[] data;
    private Context context;

    public DatePickSpinnerAdapter(String[] data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView text = new TextView(context);
        text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        text.setPadding(10, 10, 10, 10);
        text.setTextColor(Color.DKGRAY);
        text.setBackgroundColor(Color.WHITE);
        text.setTextSize(15.0f);
        text.setText(data[i]);
        return text;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return data.length == 0;
    }
}
