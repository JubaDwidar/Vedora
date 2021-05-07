package com.example.juba.chatmessenger.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.juba.chatmessenger.R;
import com.example.juba.chatmessenger.datasource.model.Messages;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.MessagesViewHolder> {
    private int SENDER = 0;
    private int RECEIVER = 1;

    private String currentUid;
    private String imageUrl;
    private List<Messages> messageList = new ArrayList<>();

    RequestManager requestManager;


    public MessageRecyclerAdapter() {
    }

    public void setUserInfo(String currentUid, String imageUrl, RequestManager requestManager) {
        this.currentUid = currentUid;
        this.imageUrl = imageUrl;
        this.requestManager=requestManager;
    }

    public void setNewMessageAdd(Messages message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    public void setMessageList(List<Messages> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderId().equals(currentUid)) {
            return SENDER;
        } else {
            return RECEIVER;
        }
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view;
        if (viewType == SENDER) {

            view = inflater.inflate(R.layout.sender_item, parent, false);
            return new MessagesViewHolder(view, SENDER);
        } else {
            view = inflater.inflate(R.layout.reciver_item, parent, false);
            return new MessagesViewHolder(view, RECEIVER);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        if (messageList.get(position).getSenderId().equals(currentUid)) {
            holder.sender.setText(messageList.get(position).getMessage());

        } else {
            holder.receiver.setText(messageList.get(position).getMessage());
            requestManager.load(imageUrl).into(holder.userImage);

        }

    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public class MessagesViewHolder extends RecyclerView.ViewHolder {


        CircleImageView userImage;
        TextView receiver;
        TextView sender;

        public MessagesViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if (viewType == 0) {
                sender = itemView.findViewById(R.id.sender_message);
            } else {
                receiver = itemView.findViewById(R.id.receiver_message);
                userImage = itemView.findViewById(R.id.profile_image);

            }
        }
    }
}
