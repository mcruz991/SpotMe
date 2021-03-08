package com.example.SpotMe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

         List<ItemModel> items;

        public CardStackAdapter(List<ItemModel> items) {

            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.setData(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView image;
            TextView name;//age, location;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.item_image);
                name = itemView.findViewById(R.id.item_name);
               // age = itemView.findViewById(R.id.item_age);
               // location = itemView.findViewById(R.id.item_city);
            }

            void setData(ItemModel data) {
                Picasso.get()
                        .load(data.getImage())
                        .fit()
                        .centerCrop()
                        .into(image);
                name.setText(data.getNama());
               // age.setText(data.getUsia());
               // location.setText(data.getKota());
            }
        }

        public List<ItemModel> getItems() {

            return items;
        }

        public void setItems(List<ItemModel> items) {

            this.items = items;
        }
    }
