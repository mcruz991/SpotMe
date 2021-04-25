package com.example.SpotMe.Matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.SpotMe.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchViewHolder> {

    private List<MatchObject> matchList;

    private Context context;

    public MatchesAdapter(List<MatchObject> matchList, Context context){
        this.matchList = matchList; //passed from main activity
        this.context = context;
    }
    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.object_match,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //everything fits well in the recycler
        layoutView.setLayoutParams(lp);


        MatchViewHolder rcv = new MatchViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        holder.MatchId.setText(matchList.get(position).getUserId());
        holder.MatchName.setText(matchList.get(position).getName());

        if(!matchList.get(position).getProfileImageUrl().equals("default")){    //if equal to default, dont change anything
            /*Picasso.get()
                    .load(matchList.get(position).getProfileImageUrl())
                    .into(holder.MatchImg);*/

                Glide.with(context).load(matchList.get(position).getProfileImageUrl()).into(holder.MatchImg);

        }

    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }
}
