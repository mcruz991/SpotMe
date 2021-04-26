 package com.example.SpotMe.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.SpotMe.Matches.MatchActivity;
import com.example.SpotMe.Matches.MatchObject;
import com.example.SpotMe.Matches.MatchesAdapter;
import com.example.SpotMe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 public class ChatActivity extends AppCompatActivity {

     private RecyclerView recView;

     private RecyclerView.Adapter bChatAdapter;//points to class

     private RecyclerView.LayoutManager bChatLayoutManager;
     private String curUserId, matchId,chatId;

     private EditText sendText;

     private Button sendButton;

     DatabaseReference dbUser,dbChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchId = getIntent().getExtras().getString("matchId");
        curUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dbUser = FirebaseDatabase.getInstance().getReference().child("Users").child(curUserId).child("matches").child("connections").child(matchId).child("ChatId"); // points to connections child
        dbChat = FirebaseDatabase.getInstance().getReference().child("Chat");



        getChatId();


        recView = findViewById(R.id.recyclerView);
        recView.setNestedScrollingEnabled(false); //scroll freely through the view
        recView.setHasFixedSize(false);
        bChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        recView.setLayoutManager(bChatLayoutManager);

        bChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);

        recView.setAdapter(bChatAdapter);


        sendText = findViewById(R.id.msg);

        sendButton = findViewById(R.id.send);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();

            }


        });
    }

     private void sendMessage() {
        //get Chat Id, get text, get userId
         //figure the ID in the connections child

         String msgTxt = sendText.getText().toString();

         if(!msgTxt.isEmpty()){     //cant send empty msg
                            //id for every text
                 DatabaseReference newMsgDb = dbChat.push();          //distinction between which user sends
                                                                    //push creates new child inside dbChat
                Map newMsg = new HashMap();

             newMsg.put("createdByUser",curUserId);
             newMsg.put("text",msgTxt);


             newMsgDb.setValue(newMsg);
         }

         sendText.setText(null);

     }

     private void getChatId(){
         dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(snapshot.exists()){
                     chatId = snapshot.getValue().toString();
                     dbChat = dbChat.child(chatId);
                     getChatMsg();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
     }

     private void getChatMsg() {
        dbChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    String message = "";
                    String createdByUser = "";

                    if(snapshot.child("text").getValue() != null){
                        message = snapshot.child("text").getValue().toString();
                    }
                    if(snapshot.child("createdByUser").getValue() != null){     //if its user or the other change color and txt pos
                        createdByUser = snapshot.child("createdByUser").getValue().toString();
                    }

                    if (message !=null && createdByUser !=null){
                        Boolean currentUserBoolean =false;
                        //check if msg is made by us or  other person
                        if(createdByUser.equals(curUserId)){
                            currentUserBoolean = true;
                        }
                        ChatObject newMessage = new ChatObject(message,currentUserBoolean);
                        resultChat.add(newMessage);

                        bChatAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
     }

     private ArrayList<ChatObject> resultChat= new ArrayList<ChatObject>();

     private List<ChatObject> getDataSetChat() {
         return resultChat;
     }
}