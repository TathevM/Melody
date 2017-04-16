package com.team_red.melody.activities;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import com.team_red.melody.Adapter.ShareAdapter;
import com.team_red.melody.R;
import com.team_red.melody.widget.MelodyEditText;

import static com.team_red.melody.models.MelodyStatics.MAIN_FONT_NAME;

public class ShareActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ShareAdapter adapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        MelodyEditText pageLabel = (MelodyEditText) findViewById(R.id.page_label);
        Typeface typeface = Typeface.createFromAsset(getAssets(), MAIN_FONT_NAME);
        pageLabel.setTypeface(typeface);
        pageLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_share);

        rv = (RecyclerView) findViewById(R.id.shareList);
        adapter = new ShareAdapter(this);
        init();
    }

    public  void  init() {

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        adapter.setOnListItemClickListener(new ShareAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                adapter.share(position);
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(ShareActivity.this, CompositionsActivity.class);
            startActivity(i);
            this.finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent i;
        switch (id){
            case R.id.nav_compositions:
                i = new Intent(ShareActivity.this, CompositionsActivity.class);
                startActivity(i);
                this.finish();
                break;
            case R.id.nav_change_user:
                i = new Intent(ShareActivity.this, StartActivity.class);
                startActivity(i);
                this.finish();
                break;
            default:
                return true;
        }
        return true;
    }
}
