package com.myapp.project.trial1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;
import android.util.Log;

import java.io.Serializable;
import java.util.Comparator;


public class Song implements Parcelable{
    private long id;
    private  String title;
    private String artist;
    private String album;
    private String duration;
    private byte [] image;
    private int vibrant;
    Utilities util = new Utilities();

    public Song(long songID, String songTitle, String songArtist, String dur, byte [] img, String alb){
        id = songID;
        title = songTitle;
        artist = songArtist;
        duration = dur;
        image = img;
        album = alb;
    }

    public long getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getDurationInMilli(){
        return duration;
    }

    public byte [] getImage(){
        return image;
    }

    public String getAlbum(){ return album; }

    public String getDurationInString(){
        long dur = Long.parseLong(duration);
        return util.millisecondsToTimer(dur);
    }


    public static Comparator<Song> SongArtistComparator = new Comparator<Song>() {
        @Override
        public int compare(Song song, Song t1) {
            String nameOne = song.getArtist();
            String nameTwo = t1.getArtist();
            return nameOne.compareTo(nameTwo);
        }
    };

    public static Comparator<Song> SongTitleComparator = new Comparator<Song>() {
        @Override
        public int compare(Song song1, Song song2) {
            String name1 = song1.getTitle();
            String name2 = song2.getTitle();

            return name1.compareTo(name2);
        }
    };

    public static Comparator<Song> SongAlbumComparator = new Comparator<Song>() {
        @Override
        public int compare(Song song1, Song song2) {
            String name1 = song1.getAlbum();
            String name2 = song2.getAlbum();
            return name1.compareTo(name2);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Log.v("testing", "writeToParcel..."+ i);
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(artist);
        parcel.writeString(album);
        parcel.writeString(duration);
        parcel.writeByteArray(image);

    }

    public static final Parcelable.Creator<Song> CREATOR
            = new Parcelable.Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public Song(Parcel in) {
        id = in.readLong();
        title = in.readString();
        artist = in.readString();
        album = in.readString();
        duration = in.readString();
        image = in.createByteArray();
    }
}