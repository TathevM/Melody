package com.team_red.melody.melodyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.team_red.melody.MainActivity;
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

    private void showMelodyBoard(View v){
        mMelodyKeyboardView.setVisibility(View.VISIBLE);
        mMelodyKeyboardView.setEnabled(true);
        if( v!=null ) ((InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void hideMelodyBoard(){
        mMelodyKeyboardView.setVisibility(View.GONE);
        mMelodyKeyboardView.setEnabled(false);
    }

    private boolean isMelodyBoardVisible(){
        return mMelodyKeyboardView.getVisibility() == View.VISIBLE;
    }

    //registering edit text to receive custom keyboard events
    public void registerEditText(EditText editText){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) showMelodyBoard(v);
                else hideMelodyBoard();
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMelodyBoard(v);
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EditText editText1 = (EditText) v;
                int inType = editText1.getInputType();
                editText1.setInputType(InputType.TYPE_NULL);
                editText1.onTouchEvent(event);
                editText1.setInputType(inType);
                return true;
            }
        });
        //disable spell checking
        editText.setInputType( editText.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS );
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
            if (mMelodyKeyboardView.isLongPressed()) {
                mMelodyKeyboardView.setLongPressed(false);
            }
            View focusCurrent = ((Activity) context).getWindow().getCurrentFocus();
            //if(focusCurrent == null || focusCurrent.getClass()!=EditText.class ) return;
            if (focusCurrent == null) return;
            EditText editText = (EditText) focusCurrent;
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            editable.insert(start, Character.toString((char) primaryCode));

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
