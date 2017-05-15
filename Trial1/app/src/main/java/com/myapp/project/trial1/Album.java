package com.myapp.project.trial1;

import android.graphics.Bitmap;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 4/11/17.
 */

public class Album implements Parent<Song> {
    private byte[] albumArt;
    private String albumName;
    private ArrayList<Song> albumList;

    public Album(byte[] albumArt, String albumName) {
        this.albumArt = albumArt;
        this.albumName = albumName;
        this.albumList = new ArrayList<>();
    }

    public Album(byte[] albumArt, String albumName, Song song) {
        this.albumArt = albumArt;
        this.albumName = albumName;
        this.albumList = new ArrayList<>();
        add(song);
    }

    public Album(Album album){
        this.albumArt = album.getAlbumArt();
        this.albumName = album.getAlbumName();
        this.albumList = album.getChildList();
    }

    public byte[] getAlbumArt() {
        return albumArt;
    }

    public String getAlbumName() {
        return albumName;
    }

    public int getSize() {
        return albumList.size();
    }

    public void add(Song song){
        albumList.add(song);
    }

    @Override
    public ArrayList<Song> getChildList() {
        return albumList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
