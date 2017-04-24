package com.myapp.project.trial1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener listener;
    GestureDetector gestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener onItemClickListener){
        listener = onItemClickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) { return false; }

            @Override
            public void onShowPress(MotionEvent motionEvent) { }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) { return false; }

            @Override
            public boolean onScroll(MotionEvent motionEvent,
                                    MotionEvent motionEvent1, float v, float v1) { return false; }

            @Override
            public void onLongPress(MotionEvent motionEvent) { }

            @Override
            public boolean onFling(MotionEvent motionEvent,
                                   MotionEvent motionEvent1, float v, float v1) { return false; }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View view = rv.findChildViewUnder(e.getX(), e.getY());
        if (view != null && listener != null && gestureDetector.onTouchEvent(e)) {
            listener.onItemClick(view, rv.getChildAdapterPosition(view));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
}
