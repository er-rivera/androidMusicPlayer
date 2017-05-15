package com.myapp.project.trial1;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/21/16.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Song> songList;
    private ArrayList<Bitmap> bmList; //REMOVE THIS

    public SectionsPagerAdapter(FragmentManager fm, ArrayList<Song> list, ArrayList<Bitmap> bmList) {
        super(fm);
        songList = list;
        this.bmList = bmList;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Tab1 tab1 = new Tab1();
                tab1.setList(songList);
                return tab1;
            case 1:
                Tab2 tab2 =  new Tab2();
                tab2.setList(songList);
                return tab2;
            case 2:
                Tab3 tab3 = new Tab3();
                tab3.setList(songList);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Artists";
            case 1:
                return "Songs";
            case 2:
                return "Albums";
        }
        return null;
    }
}
