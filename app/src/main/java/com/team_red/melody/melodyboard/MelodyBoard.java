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


public class MelodyBoard implements KeyboardView.OnKeyboardActionListener {

    private Context context;
    private MelodyKeyboardView mMelodyKeyboardView;
    private boolean signToggled = false;
    private int toggledSignType;

    private void setToggledSignType(int toggledSignType) {
        this.toggledSignType = toggledSignType;
    }

    public MelodyBoard(Context context) {
        this.context = context;
        mMelodyKeyboardView = (MelodyKeyboardView) ((Activity) context).findViewById(R.id.keyboard_view);
        MelodyKeyboard melodyKeyboard = new MelodyKeyboard(this.context , R.xml.keyboard_main);
        mMelodyKeyboardView.setKeyboard(melodyKeyboard);
        mMelodyKeyboardView.setOnKeyboardActionListener(this);
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
        editText.setTypeface(Typeface.createFromAsset(Melody.getContext().getAssets() , MelodyStatics.FONT_NAME));
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
            String input = "";
            String dotInput="";
            int start = editText.getSelectionStart();

            switch (primaryCode) {
                case MelodyStatics.CODE_CANCEL:
                    hideMelodyBoard();
                    break;
                case MelodyStatics.CODE_BACKSPACE:
                    if (start > 0 && editable != null)
                        editable.delete(start - 1, start);
                    break;
//                case MelodyStatics.CODE_SYMBOLS:
//                    MelodyKeyboard symbols = new MelodyKeyboard(context, R.xml.keyboard_symbols);
//                    mMelodyKeyboardView.setKeyboard(symbols);
//                    break;
//                case MelodyStatics.CODE_MAIN:
//                    MelodyKeyboard main = new MelodyKeyboard(context, R.xml.keyboard_main);
//                    mMelodyKeyboardView.setKeyboard(main);
//                    mMelodyKeyboardView.setToggled(-1000);
//                    setToggledSignType(0);
//                    signToggled = false;
//                    break;
                case MelodyStatics.CODE_SHARP_TOGGLE:
                    if (!signToggled)
                        toggleSign();
                    if (toggledSignType == MelodyStatics.SHARP_DIVISOR) {
                        setToggledSignType(0);
                        toggleSign();
                        mMelodyKeyboardView.setToggled(-1000);
                        return;
                    }
                    setToggledSignType(MelodyStatics.SHARP_DIVISOR);
                    mMelodyKeyboardView.setToggled(primaryCode);
                    break;
                case MelodyStatics.CODE_DOUBLE_SHARP_TOGGLE:
                    if (!signToggled)
                        toggleSign();
                    if (toggledSignType == MelodyStatics.DOUBLE_SHARP_DIVISOR) {
                        setToggledSignType(0);
                        toggleSign();
                        mMelodyKeyboardView.setToggled(-1000);
                        return;
                    }
                    setToggledSignType(MelodyStatics.DOUBLE_SHARP_DIVISOR);
                    mMelodyKeyboardView.setToggled(primaryCode);
                    break;
                case MelodyStatics.CODE_FLAT_TOGGLE:
                    if (!signToggled)
                        toggleSign();
                    if (toggledSignType == MelodyStatics.FLAT_DIVISOR) {
                        setToggledSignType(0);
                        toggleSign();
                        mMelodyKeyboardView.setToggled(-1000);
                        return;
                    }
                    setToggledSignType(MelodyStatics.FLAT_DIVISOR);
                    mMelodyKeyboardView.setToggled(primaryCode);
                    break;
                case MelodyStatics.CODE_DOUBLE_FLAT_TOGGLE:
                    if (!signToggled)
                        toggleSign();
                    if (toggledSignType == MelodyStatics.DOUBLE_FLAT_DIVISOR) {
                        setToggledSignType(0);
                        toggleSign();
                        mMelodyKeyboardView.setToggled(-1000);
                        return;
                    }
                    setToggledSignType(MelodyStatics.DOUBLE_FLAT_DIVISOR);
                    mMelodyKeyboardView.setToggled(primaryCode);
                    break;
                case MelodyStatics.CODE_TOGGLE_NATURAL:
                    if (!signToggled)
                        toggleSign();
                    if (toggledSignType == MelodyStatics.NATURAL_DIVISOR) {
                        setToggledSignType(0);
                        toggleSign();
                        mMelodyKeyboardView.setToggled(-1000);
                        return;
                    }
                    setToggledSignType(MelodyStatics.NATURAL_DIVISOR);
                    mMelodyKeyboardView.setToggled(primaryCode);
                    break;
                case MelodyStatics.CODE_DOT_TOGGLE:
                    if (!signToggled)
                        toggleSign();
                    if (toggledSignType == MelodyStatics.DOTE_DIVISOR) {
                        setToggledSignType(0);
                        toggleSign();
                        mMelodyKeyboardView.setToggled(-1000);
                        return;
                    }
                    setToggledSignType(MelodyStatics.DOTE_DIVISOR);
                    mMelodyKeyboardView.setToggled(primaryCode);
                    break;
                default:
                    if (primaryCode >= 200 && primaryCode < 360) {
                        if (signToggled && toggledSignType != 0) {
                            input = Character.toString((char) ((primaryCode / 10) * 10 + toggledSignType));
                            if(toggledSignType > 200){
                                dotInput = input;
                                input = "";
                            }
                            setToggledSignType(0);
                            toggleSign();
                            mMelodyKeyboardView.setToggled(-1000);
                        }
                    } else {
                        if(primaryCode < 180)
                        {
                            mMelodyKeyboardView.setToggled(-1000);
                            signToggled = false;
                            setToggledSignType(0);
                        }
                    }
                    editable.insert(start, input + Character.toString((char) primaryCode) + dotInput);
                    break;
            }
        }

        private void toggleSign(){
            signToggled = !signToggled;
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
}
