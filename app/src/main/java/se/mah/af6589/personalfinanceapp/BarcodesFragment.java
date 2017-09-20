package se.mah.af6589.personalfinanceapp;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class BarcodesFragment extends Fragment {

    View rootView;
    ListView lvBarcodes;
    private Controller controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_barcodes, container, false);
        initalizeComponents();
        attachListeners();
        return rootView;
    }

    private void initalizeComponents() {
        lvBarcodes = (ListView) rootView.findViewById(R.id.lv_barcodes);
    }

    @Override
    public void onResume() {
        super.onResume();
        controller = ((MainActivity) getActivity()).getController();
        controller.updateBarcodesFragment();
    }

    private void attachListeners() {
        lvBarcodes.setOnItemClickListener(new Listener());
    }

    public void setLvAdapter(BarcodeListAdapter adapter) {
        lvBarcodes.setAdapter(adapter);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private class Listener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            controller.showBarcode(((BarcodeListAdapter) adapterView.getAdapter()).getItem(i));
        }
    }
}
