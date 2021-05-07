package com.example.juba.chatmessenger.datasource.repo;


import android.util.Log;
import com.example.juba.chatmessenger.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

import javax.inject.Inject;
import io.reactivex.Completable;
public class FireBaseAuthRepo {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private static final String TAG = "FireBaseAuthRepo";

    @Inject
    public FireBaseAuthRepo(FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseFirestore = firebaseFirestore;

    }

    //get current user uid

    public String getCurrentUid() {
        return firebaseAuth.getUid();

    }


    // create new user method
    public Completable createNewUser(final String email, final String password) {
        return Completable.create(emitter -> firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(e -> emitter.onError(e)).addOnCompleteListener(task -> {
                    final HashMap<String, String> map = new HashMap<>();
                    map.put("name", "");
                    map.put("email", email);
                    map.put("password", password);
                    map.put("image", "");
                    map.put("id", getCurrentUid());
                    map.put("status", "");
                    map.put("tokens","default");



                    firebaseFirestore.collection(Constants.User_node)
                            .document(getCurrentUid())
                            .set(map)
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "log auth Problem");
                                emitter.onError(e);
                            }).addOnCompleteListener(task12 -> task12.isComplete());


                }));
    }

    // login user method
    public Completable loginUser(final String email, final String password) {
        return Completable.create(emitter -> firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(e -> emitter.onError(e)).addOnCompleteListener(task -> task.isComplete()));


    }

    // log out user method
    public void logOut() {
        firebaseAuth.signOut();

    }
}
