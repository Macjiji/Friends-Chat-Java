package com.macjiji.marcus.friendschatjava.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.macjiji.marcus.friendschatjava.R;
import com.macjiji.marcus.friendschatjava.objects.ChatRoom;

import java.util.ArrayList;

/**
 *
 * @author Marcus
 * @version 1.0
 * @see com.macjiji.marcus.friendschatjava.MainActivity
 * @see com.macjiji.marcus.friendschatjava.fragments.FragmentChat
 *
 * Classe Adaptateur permettant de créer un liste des conversations de groupes, ou des conversations entre deux utilisateurs
 *
 */

public class OwnListViewAdapter extends ArrayAdapter<ChatRoom> {

    public OwnListViewAdapter(Context context, ArrayList<ChatRoom> eventArrayList) {
        super(context, 0, eventArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Etape 1 : On récupère une conversation
        ChatRoom chatRoom = getItem(position);

        // Etape 2 : On utilise le LayoutInflater pour inclure le layout list_item
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_room, parent, false);
        }

        // Etape 3 : On récupère la référence du champ de texte shoppingListName
        TextView chatRoomName = (TextView) convertView.findViewById(R.id.chatName);
        TextView chatRoomCreatedAt = (TextView) convertView.findViewById(R.id.chatCreatedAt) ;

        // Etape 4 : On inclus le nom de la liste et la couleur de la liste sur la vue texte
        chatRoomName.setText(chatRoom.getChatName());
        chatRoomCreatedAt.setText(chatRoom.getDate());

        // Etape 5 : On retournne la vue créée
        return convertView;
    }


}
