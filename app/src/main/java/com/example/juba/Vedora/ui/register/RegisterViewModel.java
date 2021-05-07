package com.example.juba.chatmessenger.ui.register;

import android.util.Log;

import com.example.juba.chatmessenger.repository.AuthRepo;
import javax.inject.Inject;
import androidx.lifecycle.ViewModel;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends ViewModel {

    private static final String TAG = "RegisterViewModel";

    AuthRepo authRepo;
    CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public RegisterViewModel(AuthRepo authRepo) {
        this.authRepo = authRepo;
    }

    public void createUser(String email, String password) {
        authRepo.createUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG,"afterRegister");


                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });


    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

