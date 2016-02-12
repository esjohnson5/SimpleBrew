package com.esjohnson.simplebrew;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Eric's Desktop on 2/11/2016.
 * Swipe detector for deleting listview items
 */
public class OnSwipeTouchListener implements View.OnTouchListener {

    private ListView brewList;
    private GestureDetector gestureDetector;
    private Context context;
    private Button btnDelete;

    public OnSwipeTouchListener(Context context,ListView brewList) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        this.brewList = brewList;
        this.context = context;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        brewList = (ListView) v.findViewById(R.id.brewList);
        if(gestureDetector.onTouchEvent(event)){
            return true;
        }else{
            return false;
        }
    }

    public void onSwipeRight(int pos) {
        Log.d("swipe", "swiped right: " + pos);
    }

    public void onSwipeLeft(int pos) {
        Log.d("swipe", "swiped left: " + pos);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        private int getPosition(MotionEvent e1) {
            return brewList.pointToPosition((int) e1.getX(), (int) e1.getY());
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight(getPosition(e1));
                else
                    onSwipeLeft(getPosition(e1));
                return true;
            }
            return false;
        }
    }
}
