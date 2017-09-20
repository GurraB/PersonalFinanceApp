package se.mah.af6589.personalfinanceapp;


import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class BarcodeDialogFragment extends DialogFragment {

    private View rootView;
    private BarcodeTextView barcodeTextView;
    private ImageButton ibtnClose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_barcode_dialog, container, false);
        initalizeComponents();
        attachListeners();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getController().setBarcodeId(this);
    }

    private void initalizeComponents() {
        barcodeTextView = (BarcodeTextView) rootView.findViewById(R.id.tv_barcode_dialog);
        ibtnClose = (ImageButton) rootView.findViewById(R.id.btn_close_barcode_dialog);
    }

    private void attachListeners() {
        ibtnClose.setOnClickListener(new Listener());
    }

    public void setBarcode(String id) {
        barcodeTextView.setText(id);
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            dismiss();
        }
    }

}
