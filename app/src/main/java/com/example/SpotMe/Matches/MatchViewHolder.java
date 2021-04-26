package com.example.SpotMe.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SpotMe.Chat.ChatActivity;
import com.example.SpotMe.R;

public class MatchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView MatchId, MatchName;
    public ImageView MatchImg;

    public MatchViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        MatchId = itemView.findViewById(R.id.MatchID);
        MatchName = itemView.findViewById(R.id.MatchName);
        MatchImg = itemView.findViewById(R.id.MatchImg);

    }

    @Override
    public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ChatActivity.class);
            Bundle b = new Bundle();
            b.putString("matchId", MatchId.getText().toString());

            intent.putExtras(b);

            System.out.println(MatchId);
            v.getContext().startActivity(intent);
    }
}
