package com.macjiji.marcus.friendschatjava.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcus
 * @version 1.0
 * @see com.macjiji.marcus.friendschatjava.MainActivity
 * @see com.macjiji.marcus.friendschatjava.fragments.FragmentChat
 * @see com.macjiji.marcus.friendschatjava.fragments.FragmentAccount
 *
 * Classe permettant de gérer le ViewPager au sein de MainActivity, pour afficher les fragments adéquats...
 *
 */

public class OwnViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public OwnViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {return null; }


}
