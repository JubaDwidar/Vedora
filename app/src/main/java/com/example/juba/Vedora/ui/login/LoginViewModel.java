package com.example.juba.chatmessenger.ui.login;

import android.util.Log;

import com.example.juba.chatmessenger.repository.AuthRepo;
import javax.inject.Inject;
import androidx.lifecycle.ViewModel;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";
    AuthRepo authRepo;
    CompositeDisposable disposable = new CompositeDisposable();


    @Inject
    public LoginViewModel(AuthRepo authRepo) {
        Log.e(TAG,"loginViewModel exist");
        this.authRepo = authRepo;
    }



    //void not completable
    public void loginUser(String email, String password) {
        authRepo.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete success login");


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: problem here"+e.getMessage());


                    }
                });


    }


    public void logOut() {
        authRepo.logOut();

    }



}

