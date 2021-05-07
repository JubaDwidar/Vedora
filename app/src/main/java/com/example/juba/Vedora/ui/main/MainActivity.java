package com.example.juba.chatmessenger.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.juba.chatmessenger.R;
import com.example.juba.chatmessenger.repository.AuthRepo;
import com.example.juba.chatmessenger.ui.login.LoginActivity;
import com.example.juba.chatmessenger.ui.main.users.UsersFragment;
import com.example.juba.chatmessenger.ui.settings.SettingsActivity;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import dagger.android.support.DaggerAppCompatActivity;


public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    AuthRepo authRepo;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createFragment();
        intView();


    }

    private void createFragment() {
        Fragment fragment = new UsersFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void intView() {
        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vedora");
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (authRepo.getCurrentUId() == null) {
            moveToLoginActivity();

        }
    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveToSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            moveToSettingsActivity();
        } else if (id == R.id.log_out) {
            authRepo.logOut();
            moveToLoginActivity();
        }

        return super.onOptionsItemSelected(item);

    }
}


