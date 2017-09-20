package se.mah.af6589.personalfinanceapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gustaf Bohlin on 19/09/2017.
 */

public class BarcodeListAdapter extends ArrayAdapter<Barcode> {

    private LayoutInflater inflater;
    private ArrayList<Barcode> barcodes;

    public BarcodeListAdapter(@NonNull Context context, ArrayList<Barcode> barcodes) {
        super(context, R.layout.barcode_listitem);
        this.barcodes = barcodes;
        inflater = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = (CardView) inflater.inflate(R.layout.barcode_listitem, parent, false);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_barcode_name);
            holder.tvCategory = (TextView) convertView.findViewById(R.id.tv_barcode_category);
            holder.tvAmount = (TextView) convertView.findViewById(R.id.tv_barcode_amount);
            holder.tvBarcode = (BarcodeTextView) convertView.findViewById(R.id.barcode);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(barcodes.get(position).getName());
        holder.tvCategory.setText(String.valueOf(barcodes.get(position).getCategory()));
        holder.tvAmount.setText(String.valueOf(barcodes.get(position).getAmount()) + " kr");
        holder.tvBarcode.setText(barcodes.get(position).getId());
        return convertView;
    }

    @Override
    public int getCount() {
        return barcodes.size();
    }

    @Nullable
    @Override
    public Barcode getItem(int position) {
        return barcodes.get(position);
    }

    class ViewHolder {
        TextView tvName, tvCategory, tvAmount;
        BarcodeTextView tvBarcode;
    }
}
