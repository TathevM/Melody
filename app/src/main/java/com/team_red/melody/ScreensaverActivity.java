package com.team_red.melody;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.team_red.melody.DBs.DbManager;
import com.team_red.melody.models.Quote;

import java.util.Random;

public class ScreensaverActivity extends AppCompatActivity {

    TextView mQuote;
    TextView mAuthor;
    Quote myQuote;
    Random rand;

    DbManager myDbManager;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screensaver);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.actionbar));
        }
        rand = new Random();
        int i=rand.nextInt(11);
        init(i);

    }

    public void init(int i){
        mQuote = (TextView) findViewById(R.id.quote);
        mAuthor = (TextView) findViewById(R.id.author);
   switch (i){
       case 0:
           mQuote.setText(R.string.quote0);
           mAuthor.setText(R.string.author0);
           break;
       case 1:
           mQuote.setText(R.string.quote1);
           mAuthor.setText(R.string.author1);
           break;
       case 2:
           mQuote.setText(R.string.quote2);
           mAuthor.setText(R.string.author2);
           break;
       case 3:
           mQuote.setText(R.string.quote3);
           mAuthor.setText(R.string.author3);
           break;
       case 4:
           mQuote.setText(R.string.quote4);
           mAuthor.setText(R.string.author4);
           break;
       case 5:
           mQuote.setText(R.string.quote5);
           mAuthor.setText(R.string.author5);
           break;
       case 6:
           mQuote.setText(R.string.quote6);
           mAuthor.setText(R.string.author6);
           break;
       case 7:
           mQuote.setText(R.string.quote7);
           mAuthor.setText(R.string.author7);
           break;
       case 8:
           mQuote.setText(R.string.quote8);
           mAuthor.setText(R.string.author8);
           break;
       case 9:
           mQuote.setText(R.string.quote9);
           mAuthor.setText(R.string.author9);
           break;
       case 10:
           mQuote.setText(R.string.quote10);
           mAuthor.setText(R.string.author10);
           break;


   }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(ScreensaverActivity.this, StartActivity.class);
                startActivity(mainIntent);
                finish();

            }
        }, 3500);
    }


}
