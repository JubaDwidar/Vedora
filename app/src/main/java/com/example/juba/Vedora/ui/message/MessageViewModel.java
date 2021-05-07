package com.example.juba.chatmessenger.ui.message;

import com.example.juba.chatmessenger.datasource.model.Messages;
import com.example.juba.chatmessenger.datasource.model.Users;
import com.example.juba.chatmessenger.datasource.repo.FireBaseDataRepo;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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

public class MessageViewModel extends ViewModel {


    private static final String TAG = "MessageViewModel";

    private FireBaseDataRepo fireBaseDataRepo;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MediatorLiveData<List<Messages>> getAllMessage = new MediatorLiveData<>();
    private MediatorLiveData<Messages> getNewMessage = new MediatorLiveData<>();
    private MediatorLiveData<Users> friendInfo = new MediatorLiveData<>();
    private MediatorLiveData<Users> userInfo = new MediatorLiveData<>();

    private String friendId;
    private int counter = 0;
    private List<Messages> messageList = new ArrayList<>();

    @Inject
    public MessageViewModel(FireBaseDataRepo fireBaseDataRepo) {
        this.fireBaseDataRepo = fireBaseDataRepo;
    }

    public void setFriendID(String id) {
        this.friendId = id;
        getFriendInfo();
        getMessageList();

    }


    public void sendMessage(Messages message) {
        fireBaseDataRepo.sendMessage(friendId, message)
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

    public void getFriendInfo() {
        fireBaseDataRepo.getUserInfo(friendId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<DocumentSnapshot>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(DocumentSnapshot documentSnapshot) {
                        Users user = documentSnapshot.toObject(Users.class);
                        friendInfo.setValue(user);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getMessageList() {
        fireBaseDataRepo.getMessageList(friendId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<QuerySnapshot>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {

                            for (DocumentChange qs : queryDocumentSnapshots.getDocumentChanges()) {
                                if (counter == 0) {
                                    Messages message = qs.getDocument().toObject(Messages.class);
                                    messageList.add(message);
                                } else {
                                    Messages message = qs.getDocument().toObject(Messages.class);
                                    getNewMessage.setValue(message);
                                }
                                if (counter == 0) {
                                    getAllMessage.setValue(messageList);
                                    counter++;
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {


                    }

                });
    }

    public void getUserName(String uId) {


        fireBaseDataRepo.getUserInfoS(uId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<DocumentSnapshot>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DocumentSnapshot documentSnapshot) {
                        Users users = documentSnapshot.toObject(Users.class);
                        userInfo.setValue(users);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public LiveData<List<Messages>> getAllMessageLiveData() {
        return getAllMessage;
    }

    public LiveData<Messages> getNewMessageLiveData() {
        return getNewMessage;
    }

    public LiveData<Users> getFriendInfoLiveData() {
        return friendInfo;
    }

    public LiveData<Users> getUserInfoLiveData() {
        return userInfo;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}



