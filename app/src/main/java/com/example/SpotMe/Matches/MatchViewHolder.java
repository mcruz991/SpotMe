package com.example.SpotMe.Matches;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    }
}
