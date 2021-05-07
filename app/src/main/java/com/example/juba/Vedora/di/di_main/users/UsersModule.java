package com.example.juba.chatmessenger.di.di_main.users;

import android.app.Application;

import com.bumptech.glide.RequestManager;
import com.example.juba.chatmessenger.Adapter.UsersRecyclerAdapter;
import com.example.juba.chatmessenger.datasource.model.Users;
import com.example.juba.chatmessenger.datasource.repo.FireBaseDataRepo;
import com.example.juba.chatmessenger.repository.DataRepo;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import dagger.Module;
import dagger.Provides;
@Module
public class UsersModule {


    @Provides
    static FirestoreRecyclerOptions<Users> provideOption(FireBaseDataRepo dataRepo){
        return dataRepo.getUserList();
    }

    @Provides
    static UsersRecyclerAdapter provideAdapter(FirestoreRecyclerOptions<Users> options, RequestManager requestManager,DataRepo dataRepo, Application application){
        return new UsersRecyclerAdapter(options,requestManager,dataRepo);
    }
}



