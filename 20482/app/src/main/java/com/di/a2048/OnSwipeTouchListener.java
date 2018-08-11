package com.di.a2048;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import java.util.logging.Logger;

/**
 * Detects left and right swipes across a view.
 */
public class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;


    public OnSwipeTouchListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void onSwipeLeft() {


    }

    public void onSwipeRight() {

//        Log.d("Swipe", "swipe right listener");
    }
    public void onSwipeDown() {
//        Log.d("Swipe", "swipe down listener");
    }

    public void onSwipeUp() {

//        Log.d("Swipe", "swipe up listener");
    }

    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 50;
        private static final int SWIPE_VELOCITY_THRESHOLD = 50;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();

            String numberAsString = Integer.toString((int) distanceX);
            String numberAsStringY = Integer.toString((int) distanceY);

            Log.d("Coordinates X", String.valueOf(distanceX));
            Log.d("Coordinates Y", String.valueOf(distanceY));
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0 )
                    onSwipeRight();
                else if (distanceX < 0)
                    onSwipeLeft();
                return true;
            }
            else if (Math.abs(distanceY) > Math.abs(distanceX) && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(distanceY) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceY > 0 )
                    onSwipeDown();
                else if (distanceY < 0)
                    onSwipeUp();
            }
                return false;
        }
    }
}