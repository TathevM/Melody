package com.team_red.melody.activities;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.WindowManager;
import android.widget.TextView;

import com.team_red.melody.R;
import com.team_red.melody.StartActivityFragments.LoginFragment;
import com.team_red.melody.StartActivityFragments.UsersOrCompsListFragment;

public class StartActivity extends AppCompatActivity {

    private LoginFragment loginFragment;
    private UsersOrCompsListFragment usersOrCompsListFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_start);
        initFragments();
//        TextView appName = (TextView) findViewById(R.id.compositor_name);
//        appName.setText(getResources().getString(R.string.app_name));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.actionbar));
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main, menu);
        return true;
    }
}
