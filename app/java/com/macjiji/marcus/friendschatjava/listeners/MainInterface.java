package com.macjiji.marcus.friendschatjava.listeners;

import com.macjiji.marcus.friendschatjava.objects.ChatRoom;

/**
 *
 * @author Marcus
 * @version 1.0
 * @see com.macjiji.marcus.friendschatjava.MainActivity
 * @see com.macjiji.marcus.friendschatjava.fragments.FragmentChat
 * @see com.macjiji.marcus.friendschatjava.fragments.FragmentAccount
 *
 * Interface qui va permettre la communication entre MainActivity et ses Fragments. Ici, on prendra deux comportements :
 *      -> onDisconnect() : permettra de savoir lorsqu'un utilisateur clic sur le bouton de déconnexion dans le fragment de gestion de compte ;
 *      -> goIntoChatRoom(ChatRoom chatRoom) : permettra de lancer la bonne conversation au clic sur un des items présents dans le fragment des listes de conversations.
 *
 */

public interface MainInterface {

    void onDisconnect();
    void goIntoChatRoom(ChatRoom chatRoom);
    void goOnCreateChatActivity();

}
