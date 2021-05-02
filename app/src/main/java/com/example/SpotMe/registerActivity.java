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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class registerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button regBut;
    private EditText fEmail, fPass, fName;
    private RadioGroup rGroup;
    private Spinner fSpinner;
    String SEL_VALUE;
    private TextView textview;

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


        fSpinner = findViewById(R.id.item_age);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.age, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fSpinner.setAdapter(adapter);
        fSpinner.setOnItemSelectedListener(this);
         SEL_VALUE = "";

        textview = findViewById(R.id.textView2);


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
                final String age = textview.getText().toString();
                fbAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(registerActivity.this, new OnCompleteListener<AuthResult>() { // check if user creation was success
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String encPass = computerMD5Hash(password);
                        if(!task.isSuccessful()){
                            Toast.makeText(registerActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }else{
                            String userId = fbAuth.getCurrentUser().getUid();

                            DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                            Map userInfo = new HashMap<>();
                            userInfo.put("name",name);
                            userInfo.put("age",age);
                            userInfo.put("sex", radioButton.getText().toString());
                            userInfo.put("password",encPass);
                            userInfo.put("profileImageUrl","default");


                            currentUserDB.updateChildren(userInfo);

                        }



                    }
                });
            }
        });
    }

    public String computerMD5Hash(String password){     //Hashing algorithm
        try{
            //create HASH
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();


            StringBuffer MD5Hash = new StringBuffer();
            for(int i = 0; i<messageDigest.length;i++){

                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while(h.length() <2)
                    h = "0" +h;
                MD5Hash.append(h);
            }


            }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return password;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sel_val = fSpinner.getSelectedItem().toString();
                SEL_VALUE = sel_val;
                textview.setText(sel_val);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}