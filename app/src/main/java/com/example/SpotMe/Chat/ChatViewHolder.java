package com.example.SpotMe.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SpotMe.R;

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView bMessage;

    public LinearLayout bContainer;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        bMessage = itemView.findViewById(R.id.message);
        bContainer = itemView.findViewById(R.id.container);

    }

    @Override
    public void onClick(View v) {

    }
}
