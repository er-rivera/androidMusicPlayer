package com.myapp.waveform.trial1;


import android.util.Log;

import java.util.ArrayList;

/* Class LinkedHashEntry */
class LinkedHashEntry
{
    String key;
    Song value;
    LinkedHashEntry next;

    /* Constructor */
    LinkedHashEntry(String key, Song value)
    {
        this.key = key;
        this.value = value;
        this.next = null;
    }
}

/* Class HashTable */
class HashTable
{
    private int TABLE_SIZE;
    private int size;
    private LinkedHashEntry[] table;

    /* Constructor */
    public HashTable(int ts)
    {
        size = 0;
        TABLE_SIZE = ts;
        table = new LinkedHashEntry[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++)
            table[i] = null;
    }
    /* Function to get number of key-value pairs */
    public int getSize()
    {
        return size;
    }


    /* Function to clear hash table */
    public void makeEmpty()
    {
        for (int i = 0; i < TABLE_SIZE; i++)
            table[i] = null;
    }
    /* Function to get value of a key */
    public Song get(String key, String title)
    {
        int hash = (myhash( key ) % TABLE_SIZE);
        if (table[hash] == null)
            return null;
        else
        {
            LinkedHashEntry entry = table[hash];
            while (entry != null && !entry.value.getTitle().equals(title))
                entry = entry.next;
            if (entry == null)
                return null;
            else
                return entry.value;
        }
    }

    /* Function to insert a key value pair */
    public void insert(String key, Song value)
    {
       int hash = (myhash( key ) % TABLE_SIZE);
        if (table[hash] == null)
            table[hash] = new LinkedHashEntry(key, value);
        else
        {
            LinkedHashEntry entry = table[hash];
            while(entry.next != null){
                entry = entry.next;
            }
            entry.next = new LinkedHashEntry(key, value);

        }
        size++;
    }

    public boolean keyIsPresent(String key){
        int hash = (myhash( key ) % TABLE_SIZE);
        if(table[hash] != null){
            return true;
        }
        return false;
    }

    public void remove(String key)
    {
        int hash = (myhash( key ) % TABLE_SIZE);
        if (table[hash] != null)
        {
            LinkedHashEntry prevEntry = null;
            LinkedHashEntry entry = table[hash];
            while (entry.next != null && entry.key.equals(key))
            {
                prevEntry = entry;
                entry = entry.next;
            }
            if (entry.key.equals(key))
            {
                if (prevEntry == null)
                    table[hash] = entry.next;
                else
                    prevEntry.next = entry.next;
                size--;
            }
        }
    }
    /* Function myhash which gives a hash value for a given string */
    private int myhash(String x )
    {
        int hashVal = x.hashCode( );
        hashVal %= TABLE_SIZE;
        return Math.abs(hashVal);
    }

    /* Function to print hash table */
    public void printHashTable()
    {
        LinkedHashEntry entry;
        for (int i = 0; i < TABLE_SIZE; i++)
        {
            //System.out.print("\nBucket "+ (i + 1) +" : ");
            entry = table[i];
            Log.d("ARRAY NUMBER", String.valueOf(i));
            while (entry != null)
            {
                Log.i(String.valueOf(i) + " " + entry.value.getArtist(), entry.value.getTitle() +" ");
                entry = entry.next;
            }
            //Log.d("Check", "end of table entry");
        }
    }

    public ArrayList<Song> getArtistList(String key){
        int hash = (myhash( key ) % TABLE_SIZE);
        if(table[hash] == null){
            Log.e("FAILURE", "ARTIST NOT IN HASH TABLE");
            return null;
        }

        else{
            LinkedHashEntry entry;
            ArrayList<Song> arrayList = new ArrayList<>();
            entry = table[hash];
            while(entry !=  null){
                if(entry.value.getArtist().equals(key)){
                    arrayList.add(entry.value);
                }
                entry = entry.next;
            }
            return arrayList;
        }
    }

    public ArrayList<Song> getNoDuplicatesArtistList(){
        ArrayList<Song> list = new ArrayList<Song>();
        LinkedHashEntry entry;
        for(int i = 0; i < TABLE_SIZE; i++){
            entry = table[i];
            if(entry != null){
                String curArtist = entry.value.getArtist();
                list.add(entry.value);
                while(entry.next != null && entry.value.getArtist().equals(curArtist)){
                    entry = entry.next;
                }
                if(!entry.value.getArtist().equals(curArtist)){
                    list.add(entry.value);
                }
            }
        }
        return list;
    }
}
