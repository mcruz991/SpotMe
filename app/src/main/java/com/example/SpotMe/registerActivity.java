package com.example.SpotMe;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity {

    private Button regBut;
    private EditText fEmail, fPass, fName;
    private RadioGroup rGroup;

    private FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //check if user logs in and out
        fbAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() { // must be called ot start
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(registerActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        fEmail = findViewById(R.id.email);
        fPass = findViewById(R.id.password);
        fName = findViewById(R.id.name);

        regBut = findViewById(R.id.regBtn);

        rGroup = findViewById(R.id.radioGroup);

        regBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectId = rGroup.getCheckedRadioButtonId();

                final RadioButton radioButton = findViewById(selectId);

                if(radioButton.getText() == null){

                    return;
                }


                final String email = fEmail.getText().toString();
                final String password = fPass.getText().toString();
                final String name= fName.getText().toString();
                fbAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(registerActivity.this, new OnCompleteListener<AuthResult>() { // check if user creation was success
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(registerActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }else{
                            String userId = fbAuth.getCurrentUser().getUid();

                            DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString()).child(userId);
                            Map userInfo = new HashMap<>();
                            userInfo.put("name",name);
                            userInfo.put("profileImageUrl","default");


                            currentUserDB.updateChildren(userInfo);
                        }

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fbAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}