package com.myapp.project.trial1;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/21/16.
 */

public class Tab3  extends Fragment{

    private ArrayList<Album> albumList;
    ArrayList<Song> songList;
    public static AlertDialog.Builder builder;
    public static AlertDialog dialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AlbumAdapter adapter;
        RecyclerView recyclerView;
        int numOfColumns = 2;
        final StaggeredGridLayoutManager layoutManager;
        View rootView = inflater.inflate(R.layout.tab3_layout, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerAlbumList);
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AlbumAdapter(getActivity(),albumList);
        recyclerView.setAdapter(adapter);


        return rootView;
    }

    void setList(ArrayList<Song> songList){
        albumList = new ArrayList<Album>();
        this.songList = songList;
        if(albumList.size() == 0){
            albumList.add(new Album(songList.get(0).getImage(),songList.get(0).getAlbum(),songList.get(0)));
        }
        for(int i = 1; i < songList.size(); i++){
            int j =0;
            boolean found = false;
            while(j < albumList.size()  && !found){
                if(albumList.get(j).getAlbumName().equals(songList.get(i).getAlbum())){
                    albumList.get(j).add(songList.get(i));
                    Log.i("ALBUM ADDED:" ,songList.get(i).getAlbum());
                    found = true;
                }
                Log.i("ALBUM NOT ADDED:" ,songList.get(i).getAlbum() + " " + albumList.get(j).getAlbumName());
                j++;
            }
            if(j >= albumList.size()){
                albumList.add(new Album(songList.get(i).getImage(),songList.get(i).getAlbum(),songList.get(i)));
            }
        }
    }

}
