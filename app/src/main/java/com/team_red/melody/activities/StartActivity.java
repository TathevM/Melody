package com.team_red.melody.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.team_red.melody.R;
import com.team_red.melody.StartActivityFragments.LoginFragment;
import com.team_red.melody.StartActivityFragments.UsersOrCompsListFragment;

public class StartActivity extends AppCompatActivity
        //implements NavigationView.OnNavigationItemSelectedListener
{

    private LoginFragment loginFragment;
    private UsersOrCompsListFragment usersOrCompsListFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFragments();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    public  void  initFragments(){
        loginFragment = new LoginFragment();
        usersOrCompsListFragment = new UsersOrCompsListFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.Login, loginFragment, "login");
        fragmentTransaction.add(R.id.AccountChooser, usersOrCompsListFragment, "usersCompsList");
        fragmentTransaction.commit();
    }
}
