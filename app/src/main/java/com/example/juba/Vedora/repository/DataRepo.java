package com.example.juba.chatmessenger.repository;

import android.graphics.Bitmap;

import com.example.juba.chatmessenger.datasource.model.Messages;
import com.example.juba.chatmessenger.datasource.model.Users;
import com.example.juba.chatmessenger.datasource.repo.FireBaseDataRepo;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class DataRepo {

    FireBaseDataRepo fireBaseDataRepo;

    @Inject
    public DataRepo(FireBaseDataRepo fireBaseDataRepo) {

        this.fireBaseDataRepo = fireBaseDataRepo;
    }

    public FirestoreRecyclerOptions<Users> getUserList() {
        return   fireBaseDataRepo.getUserList();
    }


    public FirestoreRecyclerOptions<Messages> getChatList(String uId) {
        return fireBaseDataRepo.getChatList(uId);
    }

    //update user name and status

    public Completable updateName_Status(final String name, final String status) {
        return fireBaseDataRepo.updateName_Status(name, status);
    }

    //get user info
    public Flowable<DocumentSnapshot> getUserInfo(final String uid) {

        return fireBaseDataRepo.getUserInfo(uid);
    }

    public Flowable<DocumentSnapshot> getUserInfoS(final String uid) {

        return fireBaseDataRepo.getUserInfoS(uid);
    }



    public Completable sendMessage(final String friendId, final Messages messages) {
        return fireBaseDataRepo.sendMessage(friendId, messages);

    }


    //get message
    public Flowable<QuerySnapshot> getMessageList(final String friendUi) {
        return fireBaseDataRepo.getMessageList(friendUi);

    }


    // upload and update profile image
    public Completable updateProfileImage(Bitmap image) {
        return fireBaseDataRepo.updateProfileImage(image);

    }
}
