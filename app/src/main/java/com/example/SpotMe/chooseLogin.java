package com.example.SpotMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class chooseLogin extends AppCompatActivity {

    private Button CL_button;
    private Button R_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login);

        R_button = findViewById(R.id.register_btn);
        CL_button = findViewById(R.id.login_btn);

        R_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openRA();

            }
        });

        CL_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLA();
            }
        });
    }


    public void openRA(){
        Intent intent = new Intent(this, registerActivity.class);
        startActivity(intent);
    }

    public void openLA(){
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
    }

}