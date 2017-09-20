package se.mah.af6589.personalfinanceapp;


import android.app.DialogFragment;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class EditUserFragment extends DialogFragment {

    private View rootView;
    private Controller controller;
    private EditText et_username, et_password, et_name, et_lastname;
    private ImageButton ibCancel, ibSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.fragment_edit_user, container, false);
        initializeComponents();
        attachListeners();
        return rootView;
    }

    private void initializeComponents() {
        et_username = (EditText) rootView.findViewById(R.id.et_edit_username);
        et_password = (EditText) rootView.findViewById(R.id.et_edit_password);
        et_name = (EditText) rootView.findViewById(R.id.et_edit_name);
        et_lastname = (EditText) rootView.findViewById(R.id.et_edit_lastname);

        ibCancel = (ImageButton) rootView.findViewById(R.id.ib_edit_cancel);
        ibSave = (ImageButton) rootView.findViewById(R.id.ib_edit_save);
    }

    private void attachListeners() {
        Listener listener = new Listener();
        ibCancel.setOnClickListener(listener);
        ibSave.setOnClickListener(listener);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setUser(User user) {
        et_username.setText(user.getUsername());
        et_password.setText(user.getPassword());
        et_name.setText(user.getName());
        et_lastname.setText(user.getLastname());
    }

    @Override
    public void onResume() {
        super.onResume();
        controller = ((MainActivity)getActivity()).getController();
        controller.updateEditUserFragment(this);
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view == ibCancel) dismiss();
            if (view == ibSave) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String name = et_name.getText().toString();
                String lastname = et_lastname.getText().toString();
                controller.updateUser(username, password, name, lastname);
            }
        }
    }
}
