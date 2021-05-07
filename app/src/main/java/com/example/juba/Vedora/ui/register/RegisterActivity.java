package com.example.juba.chatmessenger.ui.register;

import androidx.lifecycle.ViewModelProvider;
import dagger.android.support.DaggerAppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.juba.chatmessenger.R;
import com.example.juba.chatmessenger.di.ViewModelProviderFactory;
import com.example.juba.chatmessenger.ui.login.LoginActivity;
import javax.inject.Inject;

public class RegisterActivity extends DaggerAppCompatActivity {
    public static final String TAG = "RegisterActivity";
    private RegisterViewModel registerViewModel;
    private EditText email;
    private EditText password;
    private Button register;
    private Button haveAccount;


    @Inject
    ViewModelProviderFactory viewModelProviderFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(RegisterViewModel.class);

        intView();
        haveAccount.setOnClickListener(v -> moveToLoginActivity());

        register.setOnClickListener(v -> {
            performRegister();
            moveToLoginActivity();
        });


    }


    private void intView() {


        email = findViewById(R.id.register_user_email);
        password = findViewById(R.id.register_user_password);
        register = findViewById(R.id.register_btn);
        haveAccount = findViewById(R.id.register_back_to_login);
    }

    public void performRegister() {
        String input_email = email.getText().toString();
        String input_Password = password.getText().toString();
        if (AuthValidation(input_email, input_Password)) {
            registerViewModel.createUser(input_email, input_Password);
        }

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

    private void moveToLoginActivity()

    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
