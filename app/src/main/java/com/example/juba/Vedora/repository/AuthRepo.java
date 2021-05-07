package com.example.juba.chatmessenger.repository;

import com.example.juba.chatmessenger.datasource.repo.FireBaseAuthRepo;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AuthRepo {

    FireBaseAuthRepo fireBaseAuthRepo;

    @Inject
    public AuthRepo(FireBaseAuthRepo fireBaseAuthRepo) {
        this.fireBaseAuthRepo = fireBaseAuthRepo;
    }

    public Completable createUser(String email, String password) {
        return fireBaseAuthRepo.createNewUser(email, password);

    }

    public Completable loginUser(String email, String password) {
        return fireBaseAuthRepo.loginUser(email, password);

    }

    // log out user method
    public void logOut() {
        fireBaseAuthRepo.logOut();

    }

    public String getCurrentUId() {
        return fireBaseAuthRepo.getCurrentUid();
    }


}
