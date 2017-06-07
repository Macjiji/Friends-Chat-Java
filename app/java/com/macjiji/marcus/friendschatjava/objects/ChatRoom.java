package com.macjiji.marcus.friendschatjava.objects;

import java.text.DateFormat;
import java.util.Locale;

/**
 *
 * @author Marcus
 * @version 1.0
 * @see com.macjiji.marcus.friendschatjava.MainActivity
 *
 * Classe permettant de représenter une conversation à afficher dans l'activité d'accueil.
 *
 */

public class ChatRoom {

    private String chatName;
    private long createAt;

    /**
     * Constructeur par défaut
     */
    public ChatRoom(){ }

    /**
     * Constructeur prenant en paramètres tous les attributs d'une conversation à afficher
     * @param chatName Le nom de la conversation
     * @param createAt La date de création de la conversation
     */
    public ChatRoom(String chatName, long createAt){
        this.chatName = chatName;
        this.createAt = createAt;
    }

    // GETTERS
    public String getChatName() { return chatName; }
    public long getCreateAt() { return createAt; }

    // SETTERS
    public void setChatName(String chatName) { this.chatName = chatName; }
    public void setCreateAt(long createAt) { this.createAt = createAt; }

    /**
     * Méthode permettant de récupérer la date de création d'une conversation à partir de la valeur sous forme de Long présente sur le serveur
     * @return La date sous forme de chaine de caractères
     */
    public String getDate(){ return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(createAt * 1000L); }

    // toString()
    @Override
    public String toString() {
        return "ChatRoom{" +
                "chatName='" + chatName + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
