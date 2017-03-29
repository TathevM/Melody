package com.team_red.melody.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.team_red.melody.Adapter.RVAdapter;
import com.team_red.melody.DBs.DbManager;
import com.team_red.melody.R;
import com.team_red.melody.app.MelodyApplication;
import com.team_red.melody.filemanager.MelodyFileManager;
import com.team_red.melody.models.Composition;
import com.team_red.melody.models.User;

import static com.team_red.melody.StartActivityFragments.LoginFragment.COMP_ID_TAG;
import static com.team_red.melody.StartActivityFragments.LoginFragment.USER_ID_TAG;
import static com.team_red.melody.melodyboard.MelodyStatics.SHEET_TYPE_ONE_HANDED;
import static com.team_red.melody.melodyboard.MelodyStatics.SHEET_TYPE_TWO_HANDED;

public class CompositionsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RVAdapter adapter;
    private DbManager mdbManager;
    private RecyclerView rv;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compositions);
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

        mdbManager = new DbManager(this);
        mUser = MelodyApplication.getLoggedInUser();
        TextView anun = (TextView) findViewById(R.id.compositor_name);
        anun.setText(mUser.getUserName());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fa_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPagePickerDialog();
            }
        });

        rv = (RecyclerView) findViewById(R.id.compositionsList);

        adapter = new RVAdapter(this);
        adapter.IS_USER_CHOSEN = true;
        adapter.setCompositionsList(mdbManager.getCompositions(mUser.getID()));
//        adapter.setCompositionsList(mdbManager.getCompositions(4));
        initData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public  void  initData() {

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        adapter.setOnListItemClickListener(new RVAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(int ID, View view) {
                Intent myIntent = new Intent(CompositionsActivity.this, MainActivity.class);
                myIntent.putExtra(COMP_ID_TAG, (long) ID);
                myIntent.putExtra(USER_ID_TAG , mUser.getID());
                startActivity(myIntent);
            }
        });
    }

    private void openPagePickerDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_new_comp_dialog);
        dialog.setTitle(R.string.dialog_new_page_picker);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        Button dialogOK = (Button) dialog.findViewById(R.id.button_dialog_OK);
        dialogOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) dialog.findViewById(R.id.dialog_new_comp_name);
                if (editText.getText().toString().equals("")){
                    Toast.makeText(CompositionsActivity.this, R.string.toast_no_name, Toast.LENGTH_SHORT).show();
                }else {
                    int curType;
                    String compName;
                    RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.sheet_type_radio_group);
                    curType = (radioGroup.getCheckedRadioButtonId() == R.id.radio_type_one_hand ? SHEET_TYPE_ONE_HANDED : SHEET_TYPE_TWO_HANDED);

                    compName = editText.getText().toString();
                    String fileName = mUser.getUserName() + compName;
                    long id = mdbManager.insertComposition(compName, mUser.getID(), fileName, curType);
                    Composition newComp = new Composition((int) id, compName, mUser.getID(), fileName, curType);
                    MelodyFileManager.getManager().createEmptyJson(newComp, mdbManager);
                    dialog.dismiss();
                    Intent myIntent = new Intent(CompositionsActivity.this, MainActivity.class);
                    myIntent.putExtra(COMP_ID_TAG, id);
                    startActivity(myIntent);
                }
            }
        });
        dialog.findViewById(R.id.button_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.compositions, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
