package com.team_red.melody.melodyboard;

import android.annotation.TargetApi;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.util.AttributeSet;


class MelodyKeyboardView extends KeyboardView {
    static boolean inEditMode = true;

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
}
