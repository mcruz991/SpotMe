package com.example.SpotMe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameField, phoneField;

    private Button bConfirm, bBack;

    private ImageView pfpImage;

    private FirebaseAuth fbAuth;
    private DatabaseReference pfpDb;

    private String userId, name, phone, profileImageUrl,userSex;
    private Uri resultUri;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nameField = findViewById(R.id.name);
        phoneField = findViewById(R.id.phone);

        pfpImage = findViewById(R.id.pfpImg);


        bConfirm = findViewById(R.id.confirm);
        bBack = findViewById(R.id.back);

        fbAuth = FirebaseAuth.getInstance();
        userId = fbAuth.getCurrentUser().getUid();
        pfpDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Profile").child(userId);


        getUserInfo();
        pfpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1); //
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });
    }


    private void getUserInfo() {

        pfpDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() >0){
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if(map.get("name") != null){
                        name = map.get("name").toString();
                        nameField.setText(name);
                    }
                    if(map.get("phone") != null){
                        phone = map.get("phone").toString();
                        phoneField.setText(phone);

                        if(map.get("sex")!=null){
                            userSex = map.get("sex").toString();
                        }

                        Glide.with(getApplication()).clear(pfpImage);

                        if(map.get("profileImageUrl")!=null) {
                            profileImageUrl = map.get("profileImageUrl").toString();
                            switch (profileImageUrl) {
                                case "default":
                                    Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(pfpImage);
                                    break;
                                default:
                                    Glide.with(getApplication()).load(profileImageUrl).into(pfpImage);
                                    break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveUserInfo() {
        name = nameField.getText().toString();
        phone = phoneField.getText().toString();


        Map userInfo = new HashMap();
        userInfo.put("name",name);
        userInfo.put("phone",phone);
        pfpDb.updateChildren(userInfo);
        if(resultUri != null){
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImage").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            final UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map newImage = new HashMap();
                            newImage.put("profileImageUrl", uri.toString());
                            pfpDb.updateChildren(newImage);

                            finish();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            finish();
                            return;
                        }
                    });
                }
            });
        }
        else{
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imgUri = data.getData();
            resultUri = imgUri;
            pfpImage.setImageURI(resultUri);
        }
    }
}