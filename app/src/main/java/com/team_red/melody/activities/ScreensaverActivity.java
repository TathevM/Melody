package com.team_red.melody.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.team_red.melody.R;

import java.util.Random;

public class ScreensaverActivity extends AppCompatActivity {

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screensaver);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.actionbar));
        }
        Random rand = new Random();
        int i= rand.nextInt(11);
        mHandler = new Handler();
        init(i);

    }

    public void init(int i) {
        String[] quotes = getResources().getStringArray(R.array.quotes);
        String[] authors = getResources().getStringArray(R.array.authors);

        TextView quote = (TextView) findViewById(R.id.quote);
        TextView author = (TextView) findViewById(R.id.author);

        quote.setText(quotes[i]);
        author.setText(authors[i]);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(ScreensaverActivity.this, StartActivity.class);
                startActivity(mainIntent);
                finish();

            }
        }, 2000);

    }
}
