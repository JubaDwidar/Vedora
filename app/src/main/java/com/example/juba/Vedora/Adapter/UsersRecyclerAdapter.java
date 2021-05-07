package com.example.juba.chatmessenger.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.juba.chatmessenger.R;
import com.example.juba.chatmessenger.datasource.model.Users;
import com.example.juba.chatmessenger.repository.DataRepo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UsersRecyclerAdapter extends FirestoreRecyclerAdapter<Users, UsersRecyclerAdapter.UsersViewHolder> {

    private static final String TAG = "UsersRecyclerAdapter";
    private RequestManager requestManager;
    private DataRepo dataRepo;
    private UserListener listener = null;



    public UsersRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Users> options, RequestManager requestManager, DataRepo dataRepo) {
        super(options);
        this.requestManager = requestManager;
        this.dataRepo = dataRepo;
    }

    public void setClickListener(UserListener userListener) {
        this.listener = userListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final UsersViewHolder holder, final int j, @NonNull Users users) {

        dataRepo.getUserInfo(getSnapshots().getSnapshot(j).getId())
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
                        holder.userName.setText(users.getName());
                        holder.userStatus.setText(users.getStatus());
                        requestManager.load(users.getImage()).into(holder.userImage);


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.user_list_item, parent, false);
        return new UsersViewHolder(view);
    }


    public class UsersViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        TextView userName;
        TextView userStatus;

        public UsersViewHolder(@NonNull final View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            userStatus = itemView.findViewById(R.id.user_status);
            itemView.setOnClickListener(v -> {
                try {

                    String id = getSnapshots().getSnapshot(getAdapterPosition()).getId();
                    Log.e(TAG, "onClick: run successfully" + id);
                    listener.onUserClick(id);

                } catch (NullPointerException ignored) {
                }

            });

        }


    }

    public interface UserListener {
        void onUserClick(String id);
    }

}

