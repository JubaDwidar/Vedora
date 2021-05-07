package com.example.juba.chatmessenger.notification;

import android.util.Log;

import com.example.juba.chatmessenger.repository.AuthRepo;
import com.example.juba.chatmessenger.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

import javax.inject.Inject;

import androidx.annotation.NonNull;

public class MyFireBaseService extends FirebaseMessagingService {

    private final static String TAG = "MyFireBaseService";

    @Inject
    AuthRepo authRepo;

    @Inject
    FirebaseFirestore firebaseFirestore;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            String refreshToken = task.getResult().getToken();
            if (firebaseUser != null) {
                updateToken(refreshToken);

            }
        });

    }

    private void updateToken(String refreshToken) {

        MyToken myToken1 = new MyToken(refreshToken);
        DocumentReference documentReference = firebaseFirestore.collection(Constants.User_node).document(authRepo.getCurrentUId());
        documentReference.update("tokens", myToken1)
                .addOnFailureListener(e -> Log.e(TAG, " token failure")).addOnSuccessListener(aVoid -> Log.e(TAG, " token success"));
    }

}
