package com.macjiji.marcus.friendschatjava.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.macjiji.marcus.friendschatjava.R;
import com.macjiji.marcus.friendschatjava.adapters.OwnListViewAdapter;
import com.macjiji.marcus.friendschatjava.listeners.MainInterface;
import com.macjiji.marcus.friendschatjava.objects.ChatRoom;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcus
 * @version 1.0
 * @see MainInterface
 * @see com.macjiji.marcus.friendschatjava.MainActivity
 *
 * Classe permettant de gérer le fragment contenant l'ensemble des conversations.
 *
 */

public class FragmentChat extends Fragment {

    protected View rootView;
    protected ListView groupChat, oneToOneChat;
    protected OwnListViewAdapter groupChatAdapter, oneToOneChatAdapter;
    protected ArrayList<ChatRoom> groupChatList = new ArrayList<>();
    protected ArrayList<ChatRoom> oneToOneChatList = new ArrayList<>();

    protected MainInterface mainInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_chat_rooms, container, false);
            initializeFirebase();
            initializeListViews();
        }
        return rootView;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mainInterface = (MainInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DataInscriCommunication");
        }
    }

    /**
     * Méthode d'initialisation des requêtes serveurs Firebase
     */
    private void initializeFirebase(){
        // Etape 1 : On récupère les salons de discussion de groupe
        FirebaseDatabase.getInstance().getReference().child("groupChat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("GroupChat", "Valeurs : " + dataSnapshot.toString());
                groupChatList.add(new ChatRoom(dataSnapshot.getKey(), Long.valueOf(dataSnapshot.child("createAt").getValue().toString())));
                groupChatAdapter.notifyDataSetChanged();
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

        // Etape 2 : On récupère les salons de discussions en one to one. Ici, on ne va récupèrer que les salons contenant le nom de l'utilisateur
        //              à l'aide des méthode startAt et endAt de Firebase
        FirebaseDatabase.getInstance().getReference().child("oneToOneChat")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("OneToOneChat", "Valeurs : " + dataSnapshot.toString());
                        if(dataSnapshot.getKey().contains(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                            String[] result = dataSnapshot.getKey().split("~");
                            if(dataSnapshot.getKey().endsWith("~" + FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                                oneToOneChatList.add(new ChatRoom(result[0], Long.valueOf(dataSnapshot.child("createAt").getValue().toString())));
                                oneToOneChatAdapter.notifyDataSetChanged();
                            } else {
                                oneToOneChatList.add(new ChatRoom(result[1], Long.valueOf(dataSnapshot.child("createAt").getValue().toString())));
                                oneToOneChatAdapter.notifyDataSetChanged();
                            }
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
     * Méthode d'initialisation des ListViews
     */
    private void initializeListViews(){
        // Etape 1 : On récupère les références via la classe R
        groupChat = (ListView)rootView.findViewById(R.id.group_chat);
        oneToOneChat = (ListView)rootView.findViewById(R.id.one_to_one_chat);

        // Etape 2 : On crée les adaptateurs
        groupChatAdapter = new OwnListViewAdapter(getContext(), groupChatList);
        groupChat.setAdapter(groupChatAdapter);
        groupChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainInterface.goIntoChatRoom(groupChatList.get(i)); // On envoie à l'activité la chatroom à lancer
            }
        });

        oneToOneChatAdapter = new OwnListViewAdapter(getContext(), oneToOneChatList);
        oneToOneChat.setAdapter(oneToOneChatAdapter);
        oneToOneChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainInterface.goIntoChatRoom(oneToOneChatList.get(i)); // On envoie à l'activité la chatroom à lancer
            }
        });

    }

}
