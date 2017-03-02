package com.team_red.melody.melodyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.KeyboardView;


public class MelodyBoard {
    private Context context;
    private MelodyKeyboardView mMelodyKeyboardView;

    public MelodyBoard(Context context, int keyboardViewResID, int keyboardxml) {
        this.context = context;
        mMelodyKeyboardView = (MelodyKeyboardView) ((Activity) context).findViewById(keyboardViewResID);
        MelodyKeyboard melodyKeyboard = new MelodyKeyboard(context , keyboardxml);
        mMelodyKeyboardView.setKeyboard(melodyKeyboard);
        mMelodyKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        mMelodyKeyboardView.setPreviewEnabled(false);
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
