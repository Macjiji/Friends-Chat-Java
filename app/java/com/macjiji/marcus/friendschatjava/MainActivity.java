package com.macjiji.marcus.friendschatjava;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.macjiji.marcus.friendschatjava.adapters.OwnViewPagerAdapter;
import com.macjiji.marcus.friendschatjava.fragments.FragmentAccount;
import com.macjiji.marcus.friendschatjava.fragments.FragmentChat;
import com.macjiji.marcus.friendschatjava.listeners.MainInterface;
import com.macjiji.marcus.friendschatjava.objects.ChatRoom;

/**
 *
 * @author Marcus
 * @version 1.0
 *
 * Classe permettant de gérer l'activité d'accueil de l'application. Cette activité contient deux Fragments :
 *      -> Le fragment contenant la liste des conversations ;
 *      -> Le fragment de gestion du compte (Déconnexion par exemple).
 *
 */

public class MainActivity extends AppCompatActivity implements MainInterface {

    protected FragmentChat fragmentChat;
    protected FragmentAccount fragmentAccount;

    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected LinearLayout.LayoutParams layoutParamsSelected, layoutParamsDefault;
    protected View viewChat, viewAccount;


    private int[] tabIcons = {
            R.drawable.custom_tab_icon_chat,
            R.drawable.custom_tab_icon_account
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseLayoutParams();
        initialiseTabLayout();

    }

    public void onDisconnect(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void goIntoChatRoom(ChatRoom chatRoom){
        Intent chatRoomIntent = new Intent(MainActivity.this, ChatActivity.class);
        chatRoomIntent.putExtra("chatName", chatRoom.getChatName());
        startActivity(chatRoomIntent);
    }

    public void goOnCreateChatActivity(){
        startActivity(new Intent(MainActivity.this, CreateChatActivity.class));
    }

    /**
     * Méthode d'initialisation des icônes de TabLayout
     */
    @SuppressWarnings("ConstantConditions")
    private void setupTabIcons() {

        // Etape 1 : On génère les vues de nos différents composants de TabLayout, à partir du fichier custom_tab_icon
        viewChat = getLayoutInflater().inflate(R.layout.custom_tab_icon, tabLayout, false);
        viewChat.findViewById(R.id.icon).setBackgroundResource(tabIcons[0]);
        viewChat.setLayoutParams(layoutParamsSelected);

        viewAccount = getLayoutInflater().inflate(R.layout.custom_tab_icon, tabLayout, false);
        viewAccount.findViewById(R.id.icon).setBackgroundResource(tabIcons[1]);
        viewAccount.setLayoutParams(layoutParamsDefault);


        // Etape 2 : On ajoute les différentes vues créées ci-dessus dans notre TabLayout
        if (tabLayout != null) {
            tabLayout.getTabAt(0).setCustomView(viewChat);
            tabLayout.getTabAt(1).setCustomView(viewAccount);
        }

        // Etape 3 : On crée le listener pour gérer les interactions avec les utilisateurs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabSelected(tab.getCustomView());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabDefault(tab.getCustomView());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * Méthode de création du ViewPager
     * @param viewPager
     */
    private void createViewPager(ViewPager viewPager) {
        // Etape 1 : On crée le ViewPagerAdapter
        OwnViewPagerAdapter adapter = new OwnViewPagerAdapter(getSupportFragmentManager());

        // Etape 2 : On génère les différents Fragments
        fragmentChat = new FragmentChat();
        fragmentAccount = new FragmentAccount();

        // Etape 3 : On ajoute les fragments à l'aide de la méthode addFrag()
        adapter.addFrag(fragmentChat);
        adapter.addFrag(fragmentAccount);

        // Etape 4 : On attache l'adaptateur à notre ViewPager, et on prend le premier item par défaut sélectionné
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        // Etape 5 : On crée le lien entre le TabLayout et le ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Etape 6 : On appel la méthode d'initialisation des icônes
        setupTabIcons();

    }


    /**
     * Méthode permettant d'initialiser le ViewPager et la TabLayout.
     */
    private void initialiseTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        viewPager.setDrawingCacheEnabled(true);
        createViewPager(viewPager);
    }

    /**
     * Méthode permettant d'initialiser les paramètres de taille pour les icones.
     */
    private void initialiseLayoutParams() {
        layoutParamsSelected = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.value_50dp), getResources().getDimensionPixelSize(R.dimen.value_50dp));
        layoutParamsDefault = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.value_30dp), getResources().getDimensionPixelSize(R.dimen.value_30dp));
    }


    /**
     * Méthode permettant de changer la vue présent dans la barre de navigation. Lorsqu'elle est sélectionnée, elle s'agrandit.
     *
     * @param view l'icone à agrandir.
     */
    private void changeTabSelected(View view) {
        view.setLayoutParams(layoutParamsSelected);
    }

    /**
     * Méthode permettant de réduire la taille d'une icone lorsqu'elle n'est plus sélectionnée.
     *
     * @param view l'icone à réduire.
     */
    private void changeTabDefault(View view) {
        view.setLayoutParams(layoutParamsDefault);
    }


}
