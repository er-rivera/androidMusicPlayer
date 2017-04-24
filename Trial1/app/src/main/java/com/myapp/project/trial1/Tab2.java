package com.myapp.project.trial1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by user on 12/21/16.
 */

public class Tab2 extends Fragment {
    private ArrayList<Song> songList3;
    private  static MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_layout, container, false);
        ListAdapter adapter = new ListAdapter(getActivity(),R.layout.song_item_layout,songList3);
        ListView lv = (ListView) rootView.findViewById(R.id.list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Testing INTENT", String.valueOf(position));
            }
        });

        return rootView;
    }

    void setList(ArrayList<Song> list){

        songList3 = list;
        Collections.sort(songList3, Song.SongTitleComparator);
    }


}
