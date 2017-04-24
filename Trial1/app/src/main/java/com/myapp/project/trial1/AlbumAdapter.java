package com.myapp.project.trial1;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Bitmap> bitmapsList;

    public AlbumAdapter(Context c, ArrayList<Bitmap> bitmapArrayList){
        inflater = LayoutInflater.from(c);
        bitmapsList = bitmapArrayList;
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.album_item_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(bitmapsList.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmapsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.album_item_recycler);
        }

        @Override
        public void onClick(View view) {
            Log.i("onClick", "SUCCESS");
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
