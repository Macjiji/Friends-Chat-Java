package com.macjiji.marcus.friendschatjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.macjiji.marcus.friendschatjava.adapters.OwnRecyclerAdapter;
import com.macjiji.marcus.friendschatjava.objects.ChatBubble;

import java.util.ArrayList;

/**
 *
 * @author Marcus
 * @version 1.0
 *
 * Classe permettant de gérer l'activité de Tchat au sein de l'application. Deux possibilités :
 *      -> Permet d'afficher un Tchat de groupe ;
 *      -> Permet d'afficher un Tchat entre deux utilisateurs.
 *
 */

public class ChatActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected OwnRecyclerAdapter mAdapter;
    protected ArrayList<ChatBubble> chatBubbleArrayList = new ArrayList<>();

    protected EditText inputMessage;
    protected ImageButton btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        initializeFirebase();
        initializeRecyclerView();
        initializeImageButton();
        initializeEditText();

    }

    private void initializeFirebase(){
        if(getIntent().getStringExtra("chatName").contains("~")){ // Cas n°1 : On est dans un chat entre deux utilisateurs (on ira dans "oneToOneChat")
            // final ChatBubble chatBubble = new ChatBubble(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), inputMessage.getText().toString(), System.currentTimeMillis() / 1000L);
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("oneToOneChat")
                    .child(getIntent().getStringExtra("chatName"))
                    .child("messages")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            ChatBubble chatBubble = dataSnapshot.getValue(ChatBubble.class);
                            chatBubbleArrayList.add(chatBubble);
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) { }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });
        } else { // Cas n°2 : On est dans un chat de groupe (on ira dans "groupChat")
            // final ChatBubble chatBubble = new ChatBubble(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), inputMessage.getText().toString(), System.currentTimeMillis() / 1000L);
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("groupChat")
                    .child(getIntent().getStringExtra("chatName"))
                    .child("messages")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            ChatBubble chatBubble = dataSnapshot.getValue(ChatBubble.class);
                            chatBubbleArrayList.add(chatBubble);
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) { }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });
        }
    }

    private void initializeRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new OwnRecyclerAdapter(this, chatBubbleArrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * Méthode d'initialisation du bouton d'envoi
     */
    private void initializeImageButton(){
        btnSend = (ImageButton) findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputMessage.getText().toString().trim().isEmpty()) { // On teste dans un premier temps si l'utilisateur a écrit un message
                    inputMessage.setError(null);
                    if(getIntent().getStringExtra("chatName").contains("~")){ // Cas n°1 : On est dans un chat entre deux utilisateurs (on ira dans "oneToOneChat")
                        // final ChatBubble chatBubble = new ChatBubble(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), inputMessage.getText().toString(), System.currentTimeMillis() / 1000L);
                        FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child("oneToOneChat")
                                .child(getIntent().getStringExtra("chatName"))
                                .child("messages")
                                .child(String.valueOf(System.currentTimeMillis() / 1000L))
                                .setValue(new ChatBubble(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), inputMessage.getText().toString(), System.currentTimeMillis() / 1000L));
                    } else { // Cas n°2 : On est dans un chat de groupe (on ira dans "groupChat")
                        // final ChatBubble chatBubble = new ChatBubble(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), inputMessage.getText().toString(), System.currentTimeMillis() / 1000L);
                        FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child("groupChat")
                                .child(getIntent().getStringExtra("chatName"))
                                .child("messages")
                                .child(String.valueOf(System.currentTimeMillis() / 1000L))
                                .setValue(new ChatBubble(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), inputMessage.getText().toString(), System.currentTimeMillis() / 1000L));
                    }

                    inputMessage.setText("");

                } else {
                    inputMessage.setError("Vous devez renseigner un message !");
                }

            }
        });
    }

    private void initializeEditText(){
        inputMessage = (EditText) findViewById(R.id.message);
    }


}
