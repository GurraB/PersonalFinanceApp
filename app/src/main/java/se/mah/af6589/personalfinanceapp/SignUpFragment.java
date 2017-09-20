package se.mah.af6589.personalfinanceapp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpFragment extends Fragment {

    private Controller controller;
    private Button btnSignUp, btnHaveAccount;
    private EditText etUsername, etPassword, etName, etLastname;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initializeComponents(rootView);
        attachListeners();
        return rootView;
    }

    private void initializeComponents(View rootView) {
        btnSignUp = (Button) rootView.findViewById(R.id.btn_sign_up);
        btnHaveAccount = (Button) rootView.findViewById(R.id.btn_have_account);
        etUsername = (EditText) rootView.findViewById(R.id.et_sign_up_username);
        etPassword = (EditText) rootView.findViewById(R.id.et_sign_up_password);
        etName = (EditText) rootView.findViewById(R.id.et_sign_up_name);
        etLastname = (EditText) rootView.findViewById(R.id.et_sign_up_lastname);
    }

    private void attachListeners() {
        Listener listener = new Listener();
        btnSignUp.setOnClickListener(listener);
        btnHaveAccount.setOnClickListener(listener);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void registrationFailed() {
        etUsername.setText("");
        etPassword.setText("");
        etName.setText("");
        etLastname.setText("");
        Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_LONG).show();
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view == btnHaveAccount)
                controller.setSignInFragment();
            if (view == btnSignUp) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String name = etName.getText().toString();
                String lastname = etLastname.getText().toString();
                controller.register((LoginActivity) getActivity(), username, password, name, lastname);
            }
        }
    }
}
