package com.myapp.waveform.trial1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by user on 12/21/16.
 */

public class Tab1 extends Fragment {

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private List<Song> artistList;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    public static final String SONG_ID = "SONG_ID";
    ArtistAdapter adapter;
    private RecyclerView rv;
    LinearLayoutManager llm;
    LayoutManagerType managerType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab1_layout, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.recyclerArtist);
        rv.setHasFixedSize(true);
        llm =  new LinearLayoutManager(getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*managerType  = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            managerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }*/

        adapter = new ArtistAdapter(getActivity(),artistList);//R.layout.artist_item_layout
        rv.setAdapter(adapter);
        return rootView;
    }


    void setList(List<Song> list){
        artistList = list;
    }

}
