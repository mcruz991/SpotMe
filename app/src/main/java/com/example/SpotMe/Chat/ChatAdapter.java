package com.example.SpotMe.Chat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.SpotMe.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private List<ChatObject> chatList;

    private Context context;

    public ChatAdapter(List<ChatObject> matchList, Context context){
        this.chatList = matchList; //passed from main activity
        this.context = context;
    }
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //everything fits well in the recycler
        layoutView.setLayoutParams(lp);


        ChatViewHolder rcv = new ChatViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
            holder.bMessage.setText(chatList.get(position).getMessage());
            if(chatList.get(position).getCurrentUser()){
                holder.bMessage.setGravity(Gravity.END);//right of the screen
                holder.bMessage.setTextColor(Color.parseColor("#404040")); //black text
                holder.bContainer.setBackgroundColor(Color.parseColor("#F4F4F4")); //Gray container
            }
            else{
                holder.bMessage.setGravity(Gravity.START);//left side
                holder.bMessage.setTextColor(Color.parseColor("#FFFFFF")); //WHITE
                holder.bContainer.setBackgroundColor(Color.parseColor("#2DB4C8")); //Gray container
            }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
