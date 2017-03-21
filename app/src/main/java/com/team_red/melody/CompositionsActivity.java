package com.team_red.melody;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.team_red.melody.StartActivityFragments.LoginFragment;
import com.team_red.melody.StartActivityFragments.UsersOrCompsListFragment;

public class CompositionsActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    UsersOrCompsListFragment usersOrCompsListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compositions);

        initFragments();
    }

    public  void  initFragments() {

        usersOrCompsListFragment = new UsersOrCompsListFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.AccountChooser, usersOrCompsListFragment, "usersCompsList");
        fragmentTransaction.commit();
    }
}
