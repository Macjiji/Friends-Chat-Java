package com.macjiji.marcus.friendschatjava.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.macjiji.marcus.friendschatjava.R;
import com.macjiji.marcus.friendschatjava.objects.ChatBubble;

import java.util.ArrayList;

/**
 *
 * @author Marcus
 * @version 1.0
 * @see com.macjiji.marcus.friendschatjava.ChatActivity
 * @see ChatBubble
 *
 * Classe permettant de générer correctement le RecyclerView. Ici, on pourra afficher deux types spécifiques de vues dans le RecyclerView :
 *      -> ViewHolderSelf : Une vue d'un message envoyé par l'utilisateur courant ;
 *      -> ViewHolderOther : Une vue d'un message envoyé par un autre utilisateur.
 *
 */

public class OwnRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private static final int VIEW_TYPE_SELF = 0;
    private static final int VIEW_TYPE_OTHER = 1;

    private Context mContext;
    private ArrayList<ChatBubble> messageArrayList;


    /**
     * Constructeur prenant en paramètres le contexte, et la liste des messages à afficher.
     * @param mContext Le contexte
     * @param messageArrayList La liste des messages à afficher
     */
    public OwnRecyclerAdapter(Context mContext, ArrayList<ChatBubble> messageArrayList) {
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;
    }

    /**
     * Classe privée permettant de définir ViewHolderSelf : Une vue spécifique à l'utilisateur dans une conversation
     */
    private class ViewHolderSelf extends RecyclerView.ViewHolder {
        TextView message, timestamp;

        private ViewHolderSelf(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        }
    }

    /**
     * Classe privée permettant de définir ViewHolderOther : Une vue spécifique aux autres utilisateurs d'une conversation
     */
    private class ViewHolderOther extends RecyclerView.ViewHolder {
        TextView message, timestamp;

        private ViewHolderOther(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        }
    }


    /**
     * Méthode issue de RecyclerView permettant de créer le ViewHolder adéquat en fonction du type de vue
     * @see RecyclerView
     * @param parent Le groupe auquel appartient l'item
     * @param viewType Le type d'item (0 pour item de l'utilisateur, 1 sinon)
     * @return Le ViewHolder adéquat (ici ViewHolderSelf ou ViewHolderOther)
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_SELF :
                return new ViewHolderSelf(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_self, parent, false));
            case VIEW_TYPE_OTHER :
                return new ViewHolderOther(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_other, parent, false));
        }
        return null;
    }

    /**
     * Méthode issue de RecyclerView renvoyant le type d'items à afficher
     * @see RecyclerView
     * @param position La position de l'item dans la liste des messages
     * @return Le type d'items sous forme d'entier (0 si items de l'utilisateur courant, 1 sinon)
     */
    @Override
    public int getItemViewType(int position) {
        ChatBubble chatBubble = messageArrayList.get(position);
        if(chatBubble.getUserName().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
            return VIEW_TYPE_SELF;
        } else if(!chatBubble.getUserName().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
            return VIEW_TYPE_OTHER;
        }
        return position;
    }

    /**
     * Méthode issue de RecyclerView permettant de générer les items adéquats.
     * @see RecyclerView
     * @param holder
     * @param position La position de l'items dans la liste des messages
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case VIEW_TYPE_SELF:
                ChatBubble chatBubbleSelf = messageArrayList.get(position);
                ((ViewHolderSelf) holder).message.setText(chatBubbleSelf.getMessage());
                ((ViewHolderSelf) holder).timestamp.setText(chatBubbleSelf.getLongToAgo());
                break;
            case VIEW_TYPE_OTHER:
                ChatBubble chatBubbleOther = messageArrayList.get(position);
                ((ViewHolderOther) holder).message.setText(chatBubbleOther.getMessage());
                ((ViewHolderOther) holder).timestamp.setText(chatBubbleOther.getLongToAgo() + " par " + chatBubbleOther.getUserName());
                break;
        }
    }

    /**
     * Méthode issue de RecyclerView
     * @see RecyclerView
     * @return Le nombre de messages présents dans la conversation
     */
    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }




}
