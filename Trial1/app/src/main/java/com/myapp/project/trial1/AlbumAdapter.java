package com.myapp.project.trial1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Album> albumArrayList;
    Context c;

    public AlbumAdapter(Context c, ArrayList<Album> albumArrayList){
        inflater = LayoutInflater.from(c);
        this.albumArrayList = albumArrayList;
        this.c = c;
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.album_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.ViewHolder holder, int position) {
        holder.album = new Album(albumArrayList.get(position));
        BitmapResult bitmapResult = new BitmapResult();
        Bitmap bitmap = bitmapResult.resizeResult(holder.album.getAlbumArt(),200,200);
        holder.imageView.setImageBitmap(bitmap);


    }

    @Override
    public int getItemCount() {
        return albumArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public ImageView imageView;
        private Album album;
        private AlertDialog dialog;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.album_item_recycler);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder((Activity) view.getContext());
            LayoutInflater dialogInflator = ((Activity) view.getContext()).getLayoutInflater();
            final View dialogView = dialogInflator.inflate(R.layout.album_dialog, null,false);
            alertDialog.setView(dialogView);
            AlbumListDialogAdapter dialogAdapter = new AlbumListDialogAdapter(((Activity) view.getContext()),R.layout.list_item_album_dialog,album.getChildList());
            ImageButton exitButton = (ImageButton) dialogView.findViewById(R.id.dialog_close);
            ImageButton addQueue = (ImageButton) dialogView.findViewById(R.id.dialog_add_queue);
            ImageButton shuffle = (ImageButton) dialogView.findViewById(R.id.dialog_shuffle);
            ListView dialogLv = (ListView) dialogView.findViewById(R.id.dialog_list);
            ImageView dialogImageView = (ImageView) dialogView.findViewById(R.id.dialog_artwork);
            TextView albumTextView = (TextView) dialogView.findViewById(R.id.dialog_albumName);
            TextView artistYearTextView = (TextView) dialogView.findViewById(R.id.dialog_artistNameYear);
            BitmapResult bitmapResult = new BitmapResult();
            dialogImageView.setImageBitmap(bitmapResult.resizeResult(album.getAlbumArt(),100,100));
            dialogLv.setAdapter(dialogAdapter);
            dialogLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MainActivity.musicSrv.setList(album.getChildList());
                    MainActivity.musicSrv.playSong(album.getChildList().get(i));
                    MainActivity.updateSongInformation();
                    Log.i("SONG NAME:", album.getChildList().get(i).getTitle());
                }
            });
            albumTextView.setText(album.getAlbumName());
            artistYearTextView.setText(album.getChildList().get(0).getArtist() + " • " + "Year");
            alertDialog.create();
            dialog = alertDialog.show();
            shuffle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(c.getApplicationContext(), R.string.UNAVAILABLE, Toast.LENGTH_SHORT).show();
                }
            });
            addQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(c.getApplicationContext(), R.string.UNAVAILABLE, Toast.LENGTH_SHORT).show();
                }
            });
            exitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
            });

        }
    }

}
