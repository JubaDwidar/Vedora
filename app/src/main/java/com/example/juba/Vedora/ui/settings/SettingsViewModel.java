package com.example.juba.chatmessenger.ui.settings;

import android.graphics.Bitmap;
import com.example.juba.chatmessenger.datasource.model.Users;
import com.example.juba.chatmessenger.repository.AuthRepo;
import com.example.juba.chatmessenger.repository.DataRepo;
import com.google.firebase.firestore.DocumentSnapshot;
import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsViewModel extends ViewModel {


    private static final String TAG = "SettingsViewModel";

    private String uId;
    private AuthRepo authRepo;
    private DataRepo dataRepo;
    private CompositeDisposable disposable = new CompositeDisposable();


    private MediatorLiveData<Users> onUser = new MediatorLiveData<>();

    @Inject
    public SettingsViewModel(AuthRepo authRepo, DataRepo dataRepo) {
        this.dataRepo = dataRepo;
        this.authRepo = authRepo;
        uId=this.authRepo.getCurrentUId();
        getUserInfo(uId);

    }


    public void getUserInfo(String uId) {
        dataRepo.getUserInfo(uId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<DocumentSnapshot>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DocumentSnapshot documentSnapshot) {
                        Users user = documentSnapshot.toObject(Users.class);
                        onUser.setValue(user);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }


    public void updateUserInfo(String name, String status) {
        dataRepo.updateName_Status(name, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });


    }

    public void updateImage(Bitmap image) {
        dataRepo.updateProfileImage(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });

    }

    public LiveData<Users> onUserInfo() {
        return onUser;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

}




