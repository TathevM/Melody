package com.team_red.melody.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;

import com.team_red.melody.R;
import com.team_red.melody.StartActivityFragments.LoginFragment;
import com.team_red.melody.StartActivityFragments.UsersOrCompsListFragment;
import com.team_red.melody.widget.MelodyEditText;

import static com.team_red.melody.models.MelodyStatics.MAIN_FONT_NAME;

public class StartActivity extends AppCompatActivity {

    public static final String LOGIN_FRAGMENT_TAG = "login";
    public static final String LIST_FRAGMENT_TAG = "usersCompsList";


    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MelodyEditText pageLabel = (MelodyEditText) findViewById(R.id.page_label);
        Typeface typeface = Typeface.createFromAsset(getAssets(), MAIN_FONT_NAME);
        pageLabel.setTypeface(typeface);
        pageLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        initFragments();
    }

    public  void  initFragments(){
        loginFragment = new LoginFragment();
        UsersOrCompsListFragment usersOrCompsListFragment = new UsersOrCompsListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.Login, loginFragment, LOGIN_FRAGMENT_TAG);
        fragmentTransaction.add(R.id.AccountChooser, usersOrCompsListFragment, LIST_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(loginFragment.isNewUserState()) {
            loginFragment.handleBackPressed();
        }
        else {
            super.onBackPressed();
        }
    }
}
