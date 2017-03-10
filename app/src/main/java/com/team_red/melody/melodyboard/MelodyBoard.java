package com.team_red.melody.melodyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.team_red.melody.Melody;
import com.team_red.melody.R;


public class MelodyBoard {
    public static final String FONT_NAME = "fonts/Melody.ttf";
    private static final int CODE_BACKSPACE = -5;
    private static final int CODE_CANCEL = -3;
    private static final int CODE_SYMBOLS = -10;
    private static final int CODE_MAIN = -11;
    private static final int CODE_SHARP_TOGGLE = -12;
    private static final int CODE_DOUBLE_SHARP_TOGGLE = -13;
    private static final int CODE_FLAT_TOGGLE = -14;
    private static final int CODE_DOUBLE_FLAT_TOGGLE = -15;
    private static final int CODE_CANCEL_B_MAJOR_TOGGLE = -16;
    private static final int CODE_DOT = -17;

    private Context context;
    private MelodyKeyboardView mMelodyKeyboardView;


    public MelodyBoard(Context context) {
        this.context = context;
        mMelodyKeyboardView = (MelodyKeyboardView) ((Activity) context).findViewById(R.id.keyboard_view);
        MelodyKeyboard melodyKeyboard = new MelodyKeyboard(this.context , R.xml.keyboard_main);
        mMelodyKeyboardView.setKeyboard(melodyKeyboard);
        mMelodyKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        mMelodyKeyboardView.setPreviewEnabled(false);

        mMelodyKeyboardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN && mMelodyKeyboardView.isLongPressed()) {
                    mMelodyKeyboardView.closing(); // Close popup keyboard if it's showing
                    //TODO check islongpressed and popup
                }
                return false;
            }
        });
    }

    //Helper function for KeyboardView ContextWrapperFix
    public static void setOnEditMode(boolean mode){
        MelodyKeyboardView.inEditMode = mode;
    }

    public void showMelodyBoard(View v){
        mMelodyKeyboardView.setVisibility(View.VISIBLE);
        mMelodyKeyboardView.setEnabled(true);
        if( v != null )
            ((InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromInputMethod(v.getWindowToken() , 0);
    }

    public void hideMelodyBoard(){
        mMelodyKeyboardView.setVisibility(View.GONE);
        mMelodyKeyboardView.setEnabled(false);
    }

    public boolean isMelodyBoardVisible(){
        return mMelodyKeyboardView.getVisibility() == View.VISIBLE;
    }

    //registering edit text to receive custom keyboard events
    public void registerEditText(EditText editText){
        editText.setTextSize(65);
        editText.setTypeface(Typeface.createFromAsset(Melody.getContext().getAssets() , FONT_NAME));
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showMelodyBoard(v);
                else
                    hideMelodyBoard();
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMelodyBoardVisible())
                    showMelodyBoard(v);
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //disabling and re-enabling input_type to prevent default keyboard popping up
                EditText editText1 = (EditText) v;
                int clickPosition = editText1.getOffsetForPosition(event.getX(), event.getY());
                int inType = editText1.getInputType();
                editText1.setInputType(InputType.TYPE_NULL);
                editText1.onTouchEvent(event);
                editText1.setInputType(inType);
                if (clickPosition >= 0) {
                    editText1.setSelection(clickPosition);
                }
                return true;
            }
        });
        //disable spell checking
        editText.setInputType( editText.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS );
    }

    private KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if (mMelodyKeyboardView.isLongPressed()) {
                mMelodyKeyboardView.setLongPressed(false);
               // return;
            }
            View focusCurrent = ((Activity) context).getWindow().getCurrentFocus();
            //if(focusCurrent == null || focusCurrent.getClass()!=EditText.class ) return;
            if (focusCurrent == null) return;
            EditText editText = (EditText) focusCurrent;
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();

            switch (primaryCode){
                case CODE_CANCEL:
                    hideMelodyBoard();
                    break;
                case CODE_BACKSPACE:
                    if(start>0 && editable != null)
                        editable.delete(start - 1 , start);
                    break;
                case CODE_SYMBOLS:
                    MelodyKeyboard symbols = new MelodyKeyboard(context , R.xml.keyboard_symbols);
                    mMelodyKeyboardView.setKeyboard(symbols);
                    break;
                case CODE_MAIN:
                    MelodyKeyboard main = new MelodyKeyboard(context , R.xml.keyboard_main);
                    mMelodyKeyboardView.setKeyboard(main);
                    break;
                case CODE_SHARP_TOGGLE:

                    break;
                default:
                    editable.insert(start, Character.toString((char) primaryCode));
                    break;
            }
        }

        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

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
