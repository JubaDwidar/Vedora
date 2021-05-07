package com.example.juba.chatmessenger.ui.message;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.example.juba.chatmessenger.Adapter.MessageRecyclerAdapter;
import com.example.juba.chatmessenger.R;
import com.example.juba.chatmessenger.datasource.model.Messages;
import com.example.juba.chatmessenger.datasource.model.Users;
import com.example.juba.chatmessenger.di.ViewModelProviderFactory;
import com.example.juba.chatmessenger.notification.MyClient;
import com.example.juba.chatmessenger.notification.Data;
import com.example.juba.chatmessenger.notification.MyNotificationApiService;
import com.example.juba.chatmessenger.notification.MyNotificationSender;
import com.example.juba.chatmessenger.notification.MyResponse;
import com.example.juba.chatmessenger.notification.MyToken;
import com.example.juba.chatmessenger.repository.AuthRepo;
import com.example.juba.chatmessenger.repository.DataRepo;
import com.example.juba.chatmessenger.utils.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

public class MessageActivity extends DaggerAppCompatActivity {

    private static final String TAG = "MessageActivity";
    MessageViewModel messageViewModel;
    private EditText messageInput;
    private ImageView messageSendBtn;
    private RecyclerView recyclerView;
    private MessageRecyclerAdapter messageRecyclerAdapter = new MessageRecyclerAdapter();
    private MyNotificationApiService apiService;


    @Inject
    ViewModelProviderFactory viewModelProviderFactory;
    @Inject
    AuthRepo authRepo;
    @Inject
    RequestManager requestManager;

    @Inject
    DataRepo dataRepo;

    @Inject
    FirebaseFirestore firestore;
    private String friToken;
    private String senderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        intToolBar();
        messageViewModel = new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(MessageViewModel.class);
        intView();
        getFriendInfo();
        getUserName(authRepo.getCurrentUId());
        observeUserInfo();
        observeFriendInfo();
        updateToken();
        observeMessageList();
        observeNewMessage();
        messageSendBtn.setOnClickListener(v -> sendMessage());


    }


    private void updateToken() {

        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        DocumentReference documentReference = firestore.collection(Constants.User_node).document(authRepo.getCurrentUId());
        documentReference.update("tokens", refreshToken);
    }

    public void sendNotification(String friendToken, String title, String message) {
        Data data = new Data(title, message);
        MyNotificationSender sender = new MyNotificationSender(data, friendToken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 0) {
                        Log.e(TAG, "there is fail retrofit");


                    }
                    Log.e(TAG, "there is complete retrofit" + response.message());

                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.e(TAG, "there is fail retrofit" + t.getMessage());
            }
        });

    }

    private void intToolBar() {
        Toolbar toolbar;

        try {
            toolbar = findViewById(R.id.message_toolbar);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);

        } catch (NullPointerException ignored) {
        }

    }


    private void intView() {
        messageInput = findViewById(R.id.message_input);
        messageSendBtn = findViewById(R.id.message_send_btn);
        recyclerView = findViewById(R.id.recyclerView);
        apiService = MyClient.getClient().create(MyNotificationApiService.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    private void getFriendInfo() {
        Intent intent = getIntent();
        if (intent.hasExtra("key_id")) {
            String uId = intent.getStringExtra("key_id");
            messageViewModel.setFriendID(uId);

        }
    }

    private void observeNewMessage() {
        messageViewModel.getNewMessageLiveData().observe(this, message -> {

            messageRecyclerAdapter.setNewMessageAdd(message);
            recyclerView.smoothScrollToPosition(messageRecyclerAdapter.getItemCount() - 1);
        });
    }

    public void getUserName(String uId)
    {
        messageViewModel.getUserName(uId);
    }

    private void observeUserInfo() {
        messageViewModel.getUserInfoLiveData().observe(this, users -> senderName = users.getName());
    }

    private void observeFriendInfo() {
        messageViewModel.getFriendInfoLiveData().observe(this, user -> {
            friToken = user.getTokens();
            Log.e(TAG, "user token is" + friToken);
            messageRecyclerAdapter.setUserInfo(authRepo.getCurrentUId(), user.getImage(), requestManager);
            try {
                (getSupportActionBar()).setTitle(user.getName());
            } catch (NullPointerException ignored) {
            }


        });
    }


    private void observeMessageList() {
        messageViewModel.getAllMessageLiveData().observe(this, messages -> {

            authRepo.getCurrentUId();
            messageRecyclerAdapter.setMessageList(messages);

            Log.e(TAG, "onChanged: live data is running");
            recyclerView.setAdapter(messageRecyclerAdapter);
            if (messageRecyclerAdapter.getItemCount() > 1) {
                recyclerView.smoothScrollToPosition(messageRecyclerAdapter.getItemCount() - 1);
            }
        });
    }

    private void sendMessage() {
        String inputMessage = messageInput.getText().toString();
        if (!inputMessage.isEmpty()) {
            Messages message = new Messages(Constants.TEXT_TYPE, inputMessage, "default");
            messageViewModel.sendMessage(message);
            sendNotification(friToken, "New Message From " + senderName, inputMessage);
            messageInput.setText("");
        }
    }
}

