package com.macjiji.marcus.friendschatjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 *
 * @author Marcus
 * @version 1.0
 *
 * Classe permettant de gérer l'activité de création d'un salon de tchat.
 *
 */

public class CreateChatActivity extends AppCompatActivity {

    protected TextView titleUserSelection, titleChatName;
    protected Spinner chatType, userToSelect;
    protected ArrayAdapter<CharSequence> adapterChatType;
    protected ArrayAdapter<String> adapterUserList;
    protected EditText chatName;
    protected Button confirm;

    private ArrayList<String> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        initializeUserList();
        initializeTextViews();
        initializeSpinner();
        initializeEditText();
        initializeButton();
        updateUI();

    }

    /**
     * Méthode d'initialisation de la liste des utilisateurs, pour la création d'un tchat "One to One"
     */
    private void initializeUserList(){
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Data", "Valeurs : " + dataSnapshot.toString());
                if(!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                    userList.add(dataSnapshot.getKey());
                    adapterUserList.notifyDataSetChanged();
                }
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

    /**
     * Méthode d'initialisation des vues Textes
     */
    private void initializeTextViews(){
        titleChatName = (TextView)findViewById(R.id.titleChatName);
        titleUserSelection = (TextView)findViewById(R.id.titleUserSelection);
    }

    /**
     * Méthode d'initialisation des Spinners
     */
    private void initializeSpinner(){
        // Etape 1 : On récupère les références
        chatType = (Spinner)findViewById(R.id.chatType);
        userToSelect = (Spinner)findViewById(R.id.chatUser);

        // Etape 2 : On crée les adaptateurs adéquats pour les spinners
        adapterChatType = ArrayAdapter.createFromResource(this, R.array.group_type_array, android.R.layout.simple_spinner_item);
        adapterChatType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chatType.setAdapter(adapterChatType);

        adapterUserList = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userList);
        userToSelect.setAdapter(adapterUserList);

        // Etape 3 : on crée les listeners
        chatType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

    }

    /**
     * Méthode d'initialisation des champs d'édition
     */
    private void initializeEditText(){
        chatName = (EditText)findViewById(R.id.chatRoomName);
    }

    /**
     * Méthode d'initialisation des boutons
     */
    private void initializeButton(){
        confirm = (Button)findViewById(R.id.button_ok);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (chatType.getSelectedItemPosition()){
                    case 0 :
                        Log.d("Chat", "Chat de groupe sélectionné");
                        FirebaseDatabase.getInstance().getReference().child("groupChat").child(chatName.getText().toString()).child("createAt").setValue(System.currentTimeMillis() / 1000L).addOnCompleteListener(CreateChatActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(CreateChatActivity.this, MainActivity.class));
                            }
                        });
                        break;
                    case 1 :
                        Log.d("Chat", "Chat One to One sélectionné");
                        FirebaseDatabase.getInstance().getReference().child("oneToOneChat").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "~" + userList.get(userToSelect.getSelectedItemPosition())).child("createAt").setValue(System.currentTimeMillis() / 1000L).addOnCompleteListener(CreateChatActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(CreateChatActivity.this, MainActivity.class));
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * Méthode permettant de mettre à jour l'interface graphique de l'activité :
     *      -> Le champ d'édition du nom de tchat collectif disparait si Tchat pour deux est sélectionné;
     *      -> Le spinner de sélection d'un utilisateur disparait si Tchat de groupe est sélectionné;
     *      -> Par défaut, tous les composants sont invisibles.
     */
    private void updateUI(){
        switch (chatType.getSelectedItemPosition()){
            case 0 :
                titleChatName.setVisibility(View.VISIBLE);
                chatName.setVisibility(View.VISIBLE);
                titleUserSelection.setVisibility(View.GONE);
                userToSelect.setVisibility(View.GONE);
                break;
            case 1 :
                titleChatName.setVisibility(View.GONE);
                chatName.setVisibility(View.GONE);
                titleUserSelection.setVisibility(View.VISIBLE);
                userToSelect.setVisibility(View.VISIBLE);
                break;
            default:
                titleChatName.setVisibility(View.GONE);
                chatName.setVisibility(View.GONE);
                titleUserSelection.setVisibility(View.GONE);
                userToSelect.setVisibility(View.GONE);
                break;
        }
    }

}
