package com.team_red.melody;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.team_red.melody.StartActivityFragments.LoginFragment;
import com.team_red.melody.StartActivityFragments.UsersOrCompsListFragment;

public class StartActivity extends AppCompatActivity {

    private LoginFragment loginFragment;
    private UsersOrCompsListFragment usersOrCompsListFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();


    }

    public  void  initFragments(){

        loginFragment = new LoginFragment();
        usersOrCompsListFragment = new UsersOrCompsListFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.Login, loginFragment, "login");
        fragmentTransaction.add(R.id.AccountChooser, usersOrCompsListFragment, "usersCompsList");
        fragmentTransaction.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case   R.id.saveIcon:
                break;
            case   R.id.shareIcon:
                break;
            case   R.id.exportIcon:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
