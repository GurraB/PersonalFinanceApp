package se.mah.af6589.personalfinanceapp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SignInFragment extends Fragment {

    private Controller controller;
    private Button btnSignIn, btnDontHaveAccount;
    private EditText etUsername, etPassword;
    private TextView tvSignIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initializeComponents(rootView);
        attachListeners();
        return rootView;
    }

    private void initializeComponents(View rootView) {
        btnSignIn = (Button) rootView.findViewById(R.id.btn_sign_in);
        btnDontHaveAccount = (Button) rootView.findViewById(R.id.btn_dont_have_account);
        etUsername = (EditText) rootView.findViewById(R.id.et_sign_in_username);
        etPassword = (EditText) rootView.findViewById(R.id.et_sign_in_password);
        tvSignIn = (TextView) rootView.findViewById(R.id.tvSignIn);
    }

    private void attachListeners() {
        Listener listener = new Listener();
        btnSignIn.setOnClickListener(listener);
        btnDontHaveAccount.setOnClickListener(listener);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void loginFailed() {

    }

    public void setText(String contents) {
        tvSignIn.setText(contents);
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if  (view == btnDontHaveAccount)
                controller.setSignUpFragment();
            if (view == btnSignIn) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                controller.login((LoginActivity) getActivity(), username, password);
            }
        }
    }

}
