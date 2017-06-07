package com.macjiji.marcus.friendschatjava.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.macjiji.marcus.friendschatjava.R;
import com.macjiji.marcus.friendschatjava.listeners.MainInterface;

/**
 *
 * @author Marcus
 * @version 1.0
 * @see MainInterface
 * @see com.macjiji.marcus.friendschatjava.MainActivity
 *
 * Classe permettant de gérer le fragment de gestion de compte.
 *
 */

public class FragmentAccount extends Fragment {

    protected View rootView;
    protected Button createNewChat, disconnectUser;
    protected MainInterface mainInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_account, container, false);
            initializeButtons();
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
     * Méthode d'initialisation des éléments du Fragments. On fait appel à rootView !!!
     */
    private void initializeButtons(){
        createNewChat = (Button)rootView.findViewById(R.id.button_create_chat);
        disconnectUser = (Button)rootView.findViewById(R.id.button_disconnect);

        createNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainInterface.goOnCreateChatActivity();
            }
        });

        disconnectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainInterface.onDisconnect();
            }
        });

    }

}
