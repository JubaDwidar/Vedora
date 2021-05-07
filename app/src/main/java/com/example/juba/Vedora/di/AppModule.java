package com.example.juba.chatmessenger.di;


import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.juba.chatmessenger.R;
import com.example.juba.chatmessenger.datasource.repo.FireBaseAuthRepo;
import com.example.juba.chatmessenger.datasource.repo.FireBaseDataRepo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {


    @Singleton
    @Provides
    static FirebaseAuth provideFireBaseAuth() {
        return FirebaseAuth.getInstance();
    }


    @Singleton
    @Provides
    static StorageReference provideFireBaseFirestore() {
        return FirebaseStorage.getInstance().getReference();
    }

    @Singleton
    @Provides
    static FirebaseFirestore provideFirestore() {
        return FirebaseFirestore.getInstance();

    }


    @Singleton
    @Provides
    static RequestOptions provideRequstOptions() {
        return RequestOptions.placeholderOf(R.drawable.profile_image).error(R.drawable.profile_image);
    }

    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions) {
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }


    @Provides
    static FireBaseDataRepo provideFireDataRepo(FirebaseFirestore firebaseFirestore, FireBaseAuthRepo authRepo, StorageReference reference) {
        return new FireBaseDataRepo(firebaseFirestore, authRepo, reference);
    }

    @Provides
    static FireBaseAuthRepo provideFireDataAuth(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        return new FireBaseAuthRepo(firebaseAuth, firebaseFirestore);
    }


}


