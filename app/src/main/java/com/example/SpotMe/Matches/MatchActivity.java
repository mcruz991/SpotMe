package com.example.SpotMe.Matches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.SpotMe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MatchActivity extends AppCompatActivity {

    private RecyclerView recView;

    private RecyclerView.Adapter bMatchAdapter;//points to class

    private RecyclerView.LayoutManager bMatchLayoutManager;

    private String curUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        curUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recView = findViewById(R.id.recyclerView);
        recView.setNestedScrollingEnabled(false); //scroll freely through the view
        recView.setHasFixedSize(true);
        bMatchLayoutManager = new LinearLayoutManager(MatchActivity.this);
        recView.setLayoutManager(bMatchLayoutManager);
        
        bMatchAdapter = new MatchesAdapter(getDataSetMatches(), MatchActivity.this);
        
        recView.setAdapter(bMatchAdapter);


        getUserMatchID();




    }

    private void getUserMatchID() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(curUserId).child("matches").child("connections");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot match : snapshot.getChildren()){
                        FetchMatchInformation(match.getKey());  //right match info
                                                        // getchildren will pass first match to the variable "match"
                                                        //match.getKey  then looping to the next users
                    }
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key); //access child name, pfp img, url
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userId = snapshot.getKey();
                    String name = "";
                    String profileImageUrl="";

                    if(snapshot.child("name").getValue()!=null){        //if null
                            name = snapshot.child("name").getValue().toString();

                    }
                    if(snapshot.child("profileImageUrl").getValue()!=null){        //if null

                        profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();

                    }


                        MatchObject obj = new MatchObject(userId,name, profileImageUrl);
                        resultMatch.add(obj);

                    bMatchAdapter.notifyDataSetChanged(); // recycler view can look for change

                    //pass the value of obj to recyclerView
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private ArrayList<MatchObject> resultMatch= new ArrayList<MatchObject>();

    private List<MatchObject> getDataSetMatches() {
        return resultMatch;
    }
}