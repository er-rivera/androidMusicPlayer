package com.myapp.waveform.trial1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;

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
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getContext(), "IN ON CLICK", Toast.LENGTH_SHORT).show();
//            }
//        });
        return rootView;
    }

    void setList(ArrayList<Song> list){

        songList3 = list;
        Collections.sort(songList3, Song.SongTitleComparator);
    }


}
