package com.macjiji.marcus.friendschatjava.objects;

import com.google.firebase.database.Exclude;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Marcus
 * @version 1.0
 * @see com.macjiji.marcus.friendschatjava.ChatActivity
 *
 * Classe représentant une bulle de tchat dans une conversation.
 *
 */

public class ChatBubble {

    private String userName;
    private String message;
    private long dateTime;

    /**
     * Constructeur par défaut
     */
    public ChatBubble(){ }

    public ChatBubble(String userName, String message, long dateTime){
        this.userName = userName;
        this.message = message;
        this.dateTime = dateTime;
    }

    // GETTERS
    public String getUserName() { return userName; }
    public String getMessage() { return message; }
    public long getDateTime() { return dateTime; }

    // SETTERS
    public void setUserName(String userName) { this.userName = userName; }
    public void setMessage(String message) { this.message = message; }
    public void setDateTime(long dateTime) { this.dateTime = dateTime; }

    /**
     * Méthode permettant permettant de renvoyer une chaine de caractères simple de la date d'envoi d'un message.
     *      -> NB : On ajoute ici la balise Firebase Excluse (Evite d'intégrer la valeurs sur la base de données !!
     * @return La date d'envoi d'un message, sous forme de chaine de caractères
     */
    @Exclude
    public String getDateCurrentTimeZone() {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(dateTime * 1000L);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            Date currenTimeZone = calendar.getTime();
            return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(currenTimeZone);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Méthode permettant de convertir la valeur de la date en fonction de la date d'envoi d'un message
     * @return La valeur de la date, sous forme de chaine de caractères
     */
    public String getLongToAgo() {
        final DateFormat dateFormatter;
        long diff = System.currentTimeMillis() - dateTime * 1000L;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String time = null;
        if (diffDays > 0) {
            if (diffDays == 1) {
                dateFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
                time =  "Hier" + ", " + dateFormatter.format(new Date(dateTime * 1000L));
            } else {
                dateFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
                time = dateFormatter.format(new Date(dateTime * 1000L));
            }
        } else {
            if (diffHours > 2) {
                if (diffHours == 1) {
                    dateFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
                    time =  "Aujourd'hui" + ", " + dateFormatter.format(new Date(dateTime * 1000L));
                }
            } else {
                if (diffMinutes > 5) {
                    time =  "Il y a quelques minutes";
                } else {
                    if (diffSeconds > 0) {
                        time =  "Il y a quelques secondes";
                    }
                }

            }

        }

        return time;
    }


    // toString()
    @Override
    public String toString() {
        return "ChatBubble{" +
                "userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }

}
