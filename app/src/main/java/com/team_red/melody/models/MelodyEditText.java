package com.team_red.melody.models;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.team_red.melody.Melody;
import com.team_red.melody.melodyboard.MelodyStatics;

public class MelodyEditText extends android.support.v7.widget.AppCompatEditText {
    public static final int TEXT_SIZE = 65;

    private Rect bounds;
    private TextPaint mTextPaint;
    private int width = 0;
    private int height = 0;
    private char[] background = {(char) 161 , (char) 161 , (char) 161 ,(char) 161, (char) 161 , (char) 161 , (char) 161 ,(char) 161 , (char) 161 ,
            (char) 161 , (char) 161 ,(char) 161};

    public MelodyEditText(Context context) {
        this(context , null);
    }

    public MelodyEditText(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public MelodyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        bounds = new Rect();
        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.BLACK);
        Typeface typeface = Typeface.createFromAsset(Melody.getContext().getAssets(), MelodyStatics.FONT_NAME);
        mTextPaint.setTypeface(typeface);

        mTextPaint.setTextSize(TEXT_SIZE + (TEXT_SIZE /2));
        super.setTypeface(typeface);
        super.setTextSize(TEXT_SIZE);
        super.setMaxLines(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(width == 0){
            width = getWidth();
            height = getHeight();

            //getting and setting maximum text length for edit text

            char testChar = (char) 161;
            mTextPaint.getTextBounds(String.valueOf(testChar) , 0 , 1 , bounds);
            int maxLengthOfText = width / bounds.width();
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthOfText)});
        }
        //mTextPaint.getTextBounds(getText().toString(), 0, getText().length(), bounds);
        canvas.drawText(String.copyValueOf(background) ,0 , height / 2 + height/ 6 , mTextPaint);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }
}
