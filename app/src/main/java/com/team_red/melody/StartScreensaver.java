package com.team_red.melody;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class StartScreensaver extends AppCompatActivity {

    ImageView appLogo ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screensaver);

        init();

    }



    void init() {

        appLogo = (ImageView) findViewById(R.id.app_logo);
        appLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent myIntent = new Intent(StartScreensaver.this, StartActivity.class);
                startActivity(myIntent);
                finish();

            }
        });



    }

}
