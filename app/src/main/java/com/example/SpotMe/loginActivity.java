package com.example.SpotMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {

    private Button logBut;
    private EditText fEmail, fPass;
    private FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //check if user logs in and out
        fbAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() { // must be called ot start
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(loginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };

        fEmail = findViewById(R.id.email);
        fPass = findViewById(R.id.password);

        logBut = findViewById(R.id.loginBtnTv);

        logBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = fEmail.getText().toString();
                final String password = fPass.getText().toString();

                fbAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() { // check if user creation was success
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(loginActivity.this, "sign in error", Toast.LENGTH_SHORT).show();
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