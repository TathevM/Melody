package com.team_red.melody;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import com.team_red.melody.filemanager.MelodyFileManager;
import com.team_red.melody.melodyboard.MelodyBoard;
import com.team_red.melody.models.MelodyAdapter;
import com.team_red.melody.models.Note;
import com.team_red.melody.sound.SoundPoolManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private MelodyBoard mMelodyBoard;
    MelodyFileManager mMelodyFileManager;
    MelodyAdapter melodyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mMelodyFileManager = MelodyFileManager.getMelodyFileManager();

        mMelodyBoard = new MelodyBoard(MainActivity.this);
        RecyclerView rv = (RecyclerView) findViewById(R.id.composition);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        melodyAdapter = new MelodyAdapter(null, mMelodyBoard);
        rv.setAdapter(melodyAdapter);
        rv.setHasFixedSize(true);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1))
                    ((MelodyAdapter)recyclerView.getAdapter()).addNewLinesToList();
            }
        });

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(mMelodyBoard.isMelodyBoardVisible())
            mMelodyBoard.hideMelodyBoard();
        else
            if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_save:
                ArrayList<Note> a = mMelodyFileManager.MakeNotesFromString(melodyAdapter.getMelodyStringList());
                mMelodyFileManager.saveComposition(a , "asd" , "asd" , 1);
                break;
            case R.id.action_settings:
                return true;
            case R.id.action_play_sound:
                ArrayList<Integer> sounds = mMelodyFileManager.getResIDOfMusic(mMelodyFileManager.MakeNotesFromString(melodyAdapter.getMelodyStringList()));
                SoundPoolManager.getInstance().setSounds(sounds);
                try {
                    SoundPoolManager.getInstance().InitializeSoundPool(this, new SoundPoolManager.ISoundPoolLoaded() {
                        @Override
                        public void onSuccess() {
                            SoundPoolManager.getInstance().setPlaySound(true);
                            SoundPoolManager.getInstance().playSound(SoundPoolManager.getInstance().getSounds().get(0));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
