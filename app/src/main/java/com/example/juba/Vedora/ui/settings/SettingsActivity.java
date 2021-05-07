package com.example.juba.chatmessenger.ui.settings;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.example.juba.chatmessenger.Adapter.UsersRecyclerAdapter;
import com.example.juba.chatmessenger.R;
import com.example.juba.chatmessenger.datasource.model.Users;
import com.example.juba.chatmessenger.di.ViewModelProviderFactory;
import com.example.juba.chatmessenger.repository.DataRepo;
import com.example.juba.chatmessenger.ui.main.MainActivity;
import com.example.juba.chatmessenger.utils.DataConverter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.vanniktech.rxpermission.RealRxPermission;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

public class SettingsActivity extends DaggerAppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final String TAG = "SettingsViewModel";
    @Inject
    RequestManager requestManager;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;


    private SettingsViewModel settingsViewModel;
    private EditText name;
    private EditText status;
    private Button saveBtn;
    private CircleImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        intToolBar();
        intView();

        settingsViewModel = new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(SettingsViewModel.class);
        observeDataChanged();

        image.setOnClickListener(v -> showGallery());
        saveBtn.setOnClickListener(v -> {
            updateInfo();
            moveToHomeActivity();
        });


    }


    private void showGallery() {
        //Storage permission
        RealRxPermission.getInstance(getApplicationContext())
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe();
        //Open gallery Intent
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    convertToByte(selectedImage);
                }
            }
        }
    }

    private void convertToByte(Uri selectedImage) {
        File imageFile = new File(DataConverter.getRealPathFromURI(selectedImage, this));
        try {
            Bitmap bitmap = new Compressor(getApplication()).compressToBitmap(imageFile);

            Log.e(TAG, "convertToByte: " + bitmap.toString());
            settingsViewModel.updateImage(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void intToolBar() {
        Toolbar toolbar;

        try {
            toolbar = findViewById(R.id.register_toolbar);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Settings");
        } catch (NullPointerException ignored) {
        }

    }


    private void intView() {
        name = findViewById(R.id.settings_user_name);
        status = findViewById(R.id.settings_user_status);
        image = findViewById(R.id.settings_img);
        saveBtn = findViewById(R.id.settings_save_btn);

    }

    public void observeDataChanged() {

        Log.e(TAG, "observeDataChanged: is working" );



        settingsViewModel.onUserInfo().observe(SettingsActivity.this, new Observer<Users>() {
            @Override
            public void onChanged(Users users) {
                Log.e(TAG, "onChanged: mig" +users.getImage() );
                Log.e(TAG, "onChanged: name" +users.getName() );
                Log.e(TAG, "onChanged: status" +users.getStatus() );

                name.setText(users.getName());
                status.setText(users.getStatus());
                requestManager.load(users.getImage()).into(image);


            }
        });


    }

    public void updateInfo() {
        String user_name = name.getText().toString();
        String user_status = status.getText().toString();

        if (user_name != null || user_status != null) {
            settingsViewModel.updateUserInfo(user_name, user_status);

        }


    }

    private void moveToHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
