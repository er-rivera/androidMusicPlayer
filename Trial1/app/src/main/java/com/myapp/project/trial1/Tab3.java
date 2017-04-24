package com.myapp.project.trial1;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by user on 12/21/16.
 */

public class Tab3  extends Fragment implements AlbumAdapter.ItemClickListener{

    private ArrayList<Bitmap> bmList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AlbumAdapter adapter;
        RecyclerView recyclerView;
        int numOfColumns = 2;
        final StaggeredGridLayoutManager layoutManager;
        View rootView = inflater.inflate(R.layout.tab3_layout, container, false);


        for(int i = 0; i < bmList.size(); i++){
            Log.i("BITMAP LIST", String.valueOf(bmList.get(i)));
        }
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerAlbumList);
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AlbumAdapter(getActivity(),bmList);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    void setList(ArrayList<Bitmap> list){

        bmList = list;
        //Collections.sort(songList3, Song.SongTitleComparator);
    }


    @Override
    public void onItemClick(View view, int position) {

    }
}
