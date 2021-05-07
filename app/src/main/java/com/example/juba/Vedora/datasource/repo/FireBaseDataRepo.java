package com.example.juba.chatmessenger.datasource.repo;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.juba.chatmessenger.datasource.model.Messages;
import com.example.juba.chatmessenger.datasource.model.Users;
import com.example.juba.chatmessenger.utils.Constants;
import com.example.juba.chatmessenger.utils.DataConverter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;


public class FireBaseDataRepo {
    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "FirebaseDataRepo";

    private String currentId;
    private FireBaseAuthRepo authRepo;
    private StorageReference storageReference;

    @Inject
    public FireBaseDataRepo(FirebaseFirestore firebaseFirestore, FireBaseAuthRepo authRepo, StorageReference reference) {
        this.firebaseFirestore = firebaseFirestore;
        this.storageReference = reference;
        this.authRepo = authRepo;
        this.currentId = authRepo.getCurrentUid();
    }


    // get All users
    public Query getUserQuery() {

        return firebaseFirestore.collection(Constants.User_node);

    }

    public FirestoreRecyclerOptions<Users> getUserList() {
        return new FirestoreRecyclerOptions.Builder<Users>()
                .setQuery(getUserQuery(), Users.class)
                .build();
    }

    // get Chat list

    public Query getChatQuery(String uId) {
        return firebaseFirestore.collection(Constants.MESSAGE_NODE).document(currentId).collection(uId);

    }


    public FirestoreRecyclerOptions<Messages> getChatList(String uId) {
        return new FirestoreRecyclerOptions.Builder<Messages>()
                .setQuery(getChatQuery(uId), Messages.class)
                .build();

    }

    //update user name and status

    public Completable updateName_Status(final String name, final String status) {

        return Completable.create(emitter -> {
            final DocumentReference documentReference = firebaseFirestore.collection(Constants.User_node).document(currentId);
            documentReference.update("name", name)
                    .addOnFailureListener(e -> emitter.onError(e)).addOnCompleteListener(task -> documentReference.update("status", status)
                    .addOnFailureListener(e -> emitter.onError(e)).addOnCompleteListener(task1 -> task1.isComplete()));
        });

    }


    public Flowable<DocumentSnapshot> getUserInfo(final String friendId) {
        return Flowable.create(emitter -> {
            final DocumentReference reference = firebaseFirestore.collection(Constants.User_node).document(friendId);
            final ListenerRegistration registration = reference.addSnapshotListener((documentSnapshot, e) -> {

                if (documentSnapshot != null) {
                    emitter.onNext(documentSnapshot);
                }
            });

            emitter.setCancellable(() -> registration.remove());
        }, BackpressureStrategy.BUFFER);

    }


    public Flowable<DocumentSnapshot> getUserInfoS(final String uID) {
        return Flowable.create(emitter -> {
            final DocumentReference reference = firebaseFirestore.collection(Constants.User_node).document(uID);
            final ListenerRegistration registration = reference.addSnapshotListener((documentSnapshot, e) -> {

                if (documentSnapshot != null) {
                    emitter.onNext(documentSnapshot);
                }
            });

            emitter.setCancellable(() -> registration.remove());
        }, BackpressureStrategy.BUFFER);

    }




    public Completable sendMessage(final String friendId, final Messages messages) {


        return Completable.create(emitter -> {
            messages.setSenderId(authRepo.getCurrentUid());
            messages.setReceiverId(friendId);
            WriteBatch requestBatch = firebaseFirestore.batch();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            DocumentReference sender_refrence = firebaseFirestore.collection(Constants.MESSAGE_NODE).document(currentId).collection(friendId).document(timestamp.toString());
            DocumentReference receiver_reference = firebaseFirestore.collection(Constants.MESSAGE_NODE).document(friendId).collection(currentId).document(timestamp.toString());

            requestBatch.set(sender_refrence, messages);
            requestBatch.set(receiver_reference, messages);

            requestBatch.commit()
                    .addOnFailureListener(e -> emitter.onError(e)).addOnCompleteListener(task -> task.isComplete());

        });

    }


    //get message
    public Flowable<QuerySnapshot> getMessageList(final String friendUi) {
        return Flowable.create(emitter -> {
            CollectionReference reference = firebaseFirestore.collection(Constants.MESSAGE_NODE).document(currentId).collection(friendUi);
            final ListenerRegistration registration = reference.addSnapshotListener((queryDocumentSnapshots, e) -> {


                if (queryDocumentSnapshots != null) {
                    emitter.onNext(queryDocumentSnapshots);
                }
            });

            emitter.setCancellable(() -> registration.remove());
        }, BackpressureStrategy.BUFFER);
    }


    // upload and update profile image


    public Completable updateProfileImage(final Bitmap image) {

        return Completable.create(emitter -> {

            final StorageReference reference = storageReference.child(Constants.profile_image_node).child(currentId + ".jpg");
            final DocumentReference documentReference = firebaseFirestore.collection(Constants.User_node).document(currentId);

            reference.putBytes(DataConverter.convertImage2ByteArray(image)).addOnFailureListener(e -> {
                Log.e(TAG, "onFailure: data repo failure");
                emitter.onError(e);
            })
                    .addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: there is a failure one");
                            emitter.onError(e);
                        }
                    }).addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "onComplete: prooblem");
                            emitter.onError(task.getException());

                        } else {
                            documentReference.update("image", task.getResult().toString())
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "onFailure: there is a failure");
                                        emitter.onError(e);
                                    })
                                    .addOnSuccessListener(aVoid -> {
                                        Log.e(TAG, "onComplete:success Completed" + task.getResult().toString());
                                        emitter.onComplete();
                                    });

                        }
                    }));

        });


    }
}