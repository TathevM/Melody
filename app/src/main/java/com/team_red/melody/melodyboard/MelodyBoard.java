package com.team_red.melody.melodyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.team_red.melody.R;


public class MelodyBoard {
    private Context context;
    private MelodyKeyboardView mMelodyKeyboardView;

    public MelodyBoard(Context context) {
        this.context = context;
        mMelodyKeyboardView = (MelodyKeyboardView) ((Activity) context).findViewById(R.id.keyboard_view);
        MelodyKeyboard melodyKeyboard = new MelodyKeyboard(context , R.xml.keyboard_main);
        mMelodyKeyboardView.setKeyboard(melodyKeyboard);
        mMelodyKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        mMelodyKeyboardView.setPreviewEnabled(false);
        mMelodyKeyboardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mMelodyKeyboardView.closing(); // Close popup keyboard if it's showing
                }
                return false;
            }
        });
    }

    //Helper function for KeyboardView ContextWrapperFix
    public static void setOnEditMode(boolean mode){
        MelodyKeyboardView.inEditMode = mode;
    }

    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if(mMelodyKeyboardView.isLongPressed()){
                mMelodyKeyboardView.setLongPressed(false);
            }
            else {
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };
}
