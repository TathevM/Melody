package com.team_red.melody.melodyboard;

import android.annotation.TargetApi;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;


class MelodyKeyboardView extends KeyboardView {
    static boolean inEditMode = true;
    private boolean isLongPressed;

    public MelodyKeyboardView(Context context, AttributeSet attrs) {
        super(new ContextWrapperFix(context, inEditMode), attrs);
    }

    public MelodyKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(new ContextWrapperFix(context, inEditMode), attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MelodyKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(new ContextWrapperFix(context, inEditMode), attrs, defStyleAttr, defStyleRes);
    }

    public boolean isLongPressed() {
        return isLongPressed;
    }

    public void setLongPressed(boolean longPressed) {
        isLongPressed = longPressed;
    }

    @Override
    protected boolean onLongPress(Keyboard.Key popupKey) {
        setLongPressed(true);
        return super.onLongPress(popupKey);
    }
}
