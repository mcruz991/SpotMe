package com.example.SpotMe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SpotMe.Cards.CardStackAdapter;
import com.example.SpotMe.Cards.CardStackCallback;
import com.example.SpotMe.Cards.ItemModel;
import com.example.SpotMe.Matches.MatchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;



    private FirebaseAuth fbAuth;
     private String userSex;
    private String otherSex;
    private String currentUid;

    private String userId = "Tx8ibFr7EHTbfE25u4eH7I3khaJ3";

    private DatabaseReference  oUsersDB;

    List<ItemModel> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        oUsersDB = FirebaseDatabase.getInstance().getReference().child("Users");

        fbAuth = FirebaseAuth.getInstance();
        final FirebaseUser fbUser = fbAuth.getCurrentUser();

        if(fbUser != null){

            currentUid = fbUser.getUid();
        };



        checkSex();





        CardStackView cardStackView = findViewById(R.id.card_stack_view);

        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            ItemModel item = new ItemModel(); //instantiate card object

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);

                if (direction == Direction.Left){ //  remove



                    oUsersDB.child(userId).child("matches").child("no").child(fbUser.getUid()).setValue(true);

                    System.out.println(currentUid);

                    Toast.makeText(MainActivity.this, "Direction Left", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Right){


                    oUsersDB.child(userId).child("matches").child("yes").child(currentUid).setValue(true);

                    ConnectionMatched(userId);

                    Toast.makeText(MainActivity.this, "Direction Right", Toast.LENGTH_SHORT).show();

                }
                if (direction == Direction.Top){
                    Toast.makeText(MainActivity.this, "Direction Top", Toast.LENGTH_SHORT).show();
                }

                if (direction == Direction.Bottom){
                    Toast.makeText(MainActivity.this, "Direction Bottom", Toast.LENGTH_SHORT).show();
                }

                // Paginating
                if (manager.getTopPosition() == adapter.getItemCount() - 5){
                    paginate();
                }

            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }
        });
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());


        adapter = new CardStackAdapter(addList());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

    }

    private void paginate() {
        List<ItemModel> old = adapter.getItems();
        List<ItemModel> baru = new ArrayList<>(addList());
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }


    private List<ItemModel> addList() {

        return items;
    }



    private void ConnectionMatched(String userId){
            DatabaseReference curUserConnectionDb = oUsersDB.child(currentUid).child("matches").child("yes").child(userId);
            curUserConnectionDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Toast.makeText(MainActivity.this, "new Connection", Toast.LENGTH_LONG).show();

                        String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                        oUsersDB.child(snapshot.getKey()).child("matches").child("connections").child(currentUid).child("ChatId").setValue(key);

                        oUsersDB.child(currentUid).child("matches").child("connections").child(snapshot.getKey()).child("ChatId").setValue(key);



                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }
    public void checkSex(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDb = oUsersDB.child(user.getUid());

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {             //CHECK the database, called everytime there are changes
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                               //know if male and female
                        if(snapshot.exists()){
                            if(snapshot.child("sex").getValue() != null){
                                userSex = snapshot.child("sex").getValue().toString()   ;
                                switch(userSex){
                                    case "Male":
                                        otherSex = "Female";
                                        break;
                                    case "Female":
                                        otherSex = "Male";
                                        break;
                                }
                                oppSexUsers();
                            }
                        }



            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void oppSexUsers(){

        oUsersDB.addChildEventListener(new ChildEventListener() {             //CHECK the database, called everytime there are changes
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.child("sex").getValue() != null) {
                    if (snapshot.exists() && !snapshot.child("matches").child("no").hasChild(currentUid) && !snapshot.child("matches").child("yes").hasChild(currentUid) && snapshot.child("sex").getValue().toString().equals(otherSex)) {

                        String profileImageUrl = "default";
                        // ItemModel item = new ItemModel(R.drawable.monke, snapshot.child("name").getValue().toString(),snapshot.getKey());

                        if (!snapshot.child("profileImageUrl").getValue().toString().equals("default")) {
                            profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                        }

                        items.add(new ItemModel(snapshot.getKey(), snapshot.child("name").getValue().toString(),snapshot.child("age").getValue().toString(), profileImageUrl));

                        //print the key
                        adapter.notifyDataSetChanged();
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

    public void logOutUser(View view) {
            fbAuth.signOut();
            Intent intent = new Intent(this, chooseLogin.class);
            startActivity(intent);
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void goToMatches(View view) {

        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
    }

    public void goToBio(View view){
        Intent intent = new Intent(this, BioActivity.class);
        startActivity(intent);

    }
}