package com.oceanstyxx.pubdriver.adapter;

import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;

import com.oceanstyxx.pubdriver.fragment.MainBookingFragment;
import com.oceanstyxx.pubdriver.fragment.MainBookingListFragment;
import com.oceanstyxx.pubdriver.fragment.MainContactUsFragment;
import com.oceanstyxx.pubdriver.fragment.MainHomeFragment;


/**
 * Created by mohsin on 10/10/16.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "BOOK","HISTORY","CONTACT"};
    private Context context;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            /*case 0:
                MainHomeFragment  mianHomeFragmentTab= new MainHomeFragment();
                return mianHomeFragmentTab;*/
            case 0:
                MainBookingFragment mainBookingFragmentTab = new MainBookingFragment();
                return mainBookingFragmentTab;
            case 1:
                MainBookingListFragment mainBookingListFragmentTab = new MainBookingListFragment();
                return mainBookingListFragmentTab;
            case 2:
                MainContactUsFragment mainContactUsFragmentTab = new MainContactUsFragment();
                return mainContactUsFragmentTab;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
