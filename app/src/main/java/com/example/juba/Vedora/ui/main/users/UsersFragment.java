package com.example.juba.chatmessenger.ui.main.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.example.juba.chatmessenger.Adapter.UsersRecyclerAdapter;
import com.example.juba.chatmessenger.R;
import com.example.juba.chatmessenger.di.ViewModelProviderFactory;
import com.example.juba.chatmessenger.repository.AuthRepo;
import com.example.juba.chatmessenger.ui.message.MessageActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import javax.inject.Inject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerFragment;

public class UsersFragment extends DaggerFragment implements UsersRecyclerAdapter.UserListener {

    private UsersFragmentViewModel usersViewModel;
    private RecyclerView recyclerView;

    private static final String TAG = "UsersFragment";

    @Inject
    UsersRecyclerAdapter userRecyclerAdapter;


    @Inject
    RequestManager requestManager;
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    FirebaseFirestore firestore;

    @Inject
    AuthRepo authRepo;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        usersViewModel = new ViewModelProvider(getViewModelStore(), viewModelProviderFactory).get(UsersFragmentViewModel.class);
        recyclerView = view.findViewById(R.id.users_recycler);

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userRecyclerAdapter);
        userRecyclerAdapter.setClickListener(this);
        userRecyclerAdapter.startListening();
    }




    @Override
    public void onStop() {
        super.onStop();
        userRecyclerAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        userRecyclerAdapter.startListening();
    }





    @Override
    public void onUserClick(String id) {
        moveToMessageActivity(id);
    }

    public void moveToMessageActivity(String id) {
        Intent intent = new Intent(getContext(), MessageActivity.class);
        intent.putExtra("key_id", id);
        startActivity(intent);

    }
}

