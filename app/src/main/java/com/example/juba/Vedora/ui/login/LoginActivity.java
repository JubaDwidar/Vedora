package com.example.juba.chatmessenger.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juba.chatmessenger.R;
import com.example.juba.chatmessenger.di.ViewModelProviderFactory;
import com.example.juba.chatmessenger.ui.main.MainActivity;
import com.example.juba.chatmessenger.ui.register.RegisterActivity;
import javax.inject.Inject;

import androidx.lifecycle.ViewModelProvider;

import dagger.android.support.DaggerAppCompatActivity;


public class LoginActivity extends DaggerAppCompatActivity {

    private static final String TAG = "LoginActivity";
    LoginViewModel loginViewModel;
    private EditText email;
    private EditText password;
    private Button login;
    private Button createNewAccount;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(LoginViewModel.class);

        intViews();
        login.setOnClickListener(v -> {
            performLogin();
        });

        createNewAccount.setOnClickListener(v -> moveToRegisterActivity());
    }


    private boolean AuthValidation(String email, String password) {
        if (email == null || email.length() == 0) {

            Toast.makeText(this, "Check your Email", Toast.LENGTH_LONG).show();
            return false;
        }

        if (password == null || password.length() == 0) {

            Toast.makeText(this, "Check your Password", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public void moveToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void intViews() {
        email = findViewById(R.id.login_user_email);
        password = findViewById(R.id.login_user_password);
        login = findViewById(R.id.login_btn);
        createNewAccount = findViewById(R.id.login_create_btn);


    }


    private void moveToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void performLogin() {
        String input_email = email.getText().toString();
        String input_Password = password.getText().toString();
        if (AuthValidation(input_email, input_Password)) {
            loginViewModel.loginUser(input_email, input_Password);
            Log.e(TAG, "login successfully: ");
            moveToMainActivity();

        }

    }


}

