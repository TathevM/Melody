package com.team_red.melody.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.util.AttributeSet;

import com.team_red.melody.app.MelodyApplication;
import com.team_red.melody.models.MelodyStatics;

public class MelodyEditText extends android.support.v7.widget.AppCompatEditText {
    public static final int TEXT_SIZE = 65;

    private Paint mPaint;

    char testChar = (char) 161;
    private InputFilter[] mInputFilter;
    private InputSetter mInputSetter;

    private float mTextHeight;
    private int mTextWidth;
    private int width = 0;
    private int height = 0;

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
        Rect bounds = new Rect();
        mPaint = new Paint();
        mInputSetter = new InputSetter();
        mPaint.setStrokeWidth(2f);
        mPaint.setColor(Color.BLACK);
        Typeface typeface = Typeface.createFromAsset(MelodyApplication.getContext().getAssets(), MelodyStatics.FONT_NAME);

        super.setTypeface(typeface);
        super.setTextSize(TEXT_SIZE);
        super.setMaxLines(1);
        super.getPaint().getTextBounds(String.valueOf(testChar) , 0 , 1 , bounds);
        mTextHeight = bounds.height();
        mTextWidth = bounds.width();
        mInputFilter = new InputFilter[1];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0 , getHeight()/2 , getWidth() , getHeight()/2 , mPaint);
        canvas.drawLine(0 , getHeight()/2 + mTextHeight /4 , getWidth() , getHeight()/2 + mTextHeight /4 , mPaint);
        canvas.drawLine(0 , getHeight()/2 - mTextHeight /4 , getWidth() , getHeight()/2 - mTextHeight /4 , mPaint);
        canvas.drawLine(0 , getHeight()/2 + mTextHeight /2 , getWidth() , getHeight()/2 + mTextHeight /2 , mPaint);
        canvas.drawLine(0 , getHeight()/2 - mTextHeight /2 , getWidth() , getHeight()/2 - mTextHeight /2 , mPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = widthMeasureSpec;
        height = heightMeasureSpec;
        postDelayed(mInputSetter , 10);
        super.onMeasure(width , height);
    }

    private class InputSetter implements Runnable{
        @Override
        public void run() {
            mInputFilter[0] = new InputFilter.LengthFilter(getWidth() / mTextWidth);
            setFilters(mInputFilter);
        }
    }
}
