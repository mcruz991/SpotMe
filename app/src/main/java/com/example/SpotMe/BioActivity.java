package com.example.SpotMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class BioActivity extends AppCompatActivity {

    EditText bioField;
    private FirebaseAuth fbAuth;

    DatabaseReference bioDb;

    private String userId, bioText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        fbAuth = FirebaseAuth.getInstance();
         userId = "Tx8ibFr7EHTbfE25u4eH7I3khaJ3";
        bioDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        bioField = findViewById(R.id.bioMulti);


        bioDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null) {
                    bioText = snapshot.child("bio").getValue().toString();
                    bioField.setText(bioText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }





}