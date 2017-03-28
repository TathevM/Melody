package com.team_red.melody.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.team_red.melody.R;

import java.util.Random;

public class ScreensaverActivity extends AppCompatActivity {

    private final String QUOTE = "quote";
    private final String AUTHOR = "author";

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screensaver);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.actionbar));
        }
        Random rand = new Random();
        int i= rand.nextInt(11);
        mHandler = new Handler();
        init(i);

    }

    public void init(int i){

        try {
            TextView quote = (TextView) findViewById(R.id.quote);
            TextView author = (TextView) findViewById(R.id.author);

            int quoteID = R.string.class.getField(QUOTE + i).getInt(null);
            int authorID = R.string.class.getField(AUTHOR + i).getInt(null);

            quote.setText(quoteID);
            author.setText(authorID);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(ScreensaverActivity.this, StartActivity.class);
                startActivity(mainIntent);
                finish();

            }
        }, 1000);
    }


}
