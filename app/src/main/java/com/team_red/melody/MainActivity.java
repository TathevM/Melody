package com.team_red.melody;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.team_red.melody.DBs.DbManager;
import com.team_red.melody.filemanager.LoadedData;
import com.team_red.melody.filemanager.MelodyExporter;
import com.team_red.melody.filemanager.MelodyFileManager;
import com.team_red.melody.melodyboard.MelodyBoard;
import com.team_red.melody.models.Composition;
import com.team_red.melody.models.MelodyAdapter;
import com.team_red.melody.models.Note;
import com.team_red.melody.models.User;
import com.team_red.melody.sound.MelodyPoolManager;

import java.util.ArrayList;

import static com.team_red.melody.StartActivityFragments.LoginFragment.COMP_ID_TAG;
import static com.team_red.melody.StartActivityFragments.LoginFragment.USER_ID_TAG;
import static com.team_red.melody.melodyboard.MelodyStatics.SHEET_TYPE_ONE_HANDED;
import static com.team_red.melody.melodyboard.MelodyStatics.SHEET_TYPE_TWO_HANDED;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private MelodyBoard mMelodyBoard;
    private MelodyAdapter melodyAdapter;
    private MenuItem playButton;
    private User currentUser;
    private Composition currentComposition;
    private DbManager mDbManager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mDbManager = new DbManager(this);

        initActivity();
        getInitData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPagePickerDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void openPagePickerDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_new_comp_dialog);
        dialog.setTitle("Pick page type");
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        Button dialogOK = (Button) dialog.findViewById(R.id.button_dialog_OK);
        dialogOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curType;
                String compName;
                RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.sheet_type_radio_group);
                EditText editText = (EditText) dialog.findViewById(R.id.dialog_new_comp_name);
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.radio_type_two_hand:
                        curType = SHEET_TYPE_TWO_HANDED;
                        break;
                    default:
                        curType = SHEET_TYPE_ONE_HANDED;
                        break;
                }
                compName = editText.getText().toString();
                String fileName = currentUser.getUserName() + compName;
                long id = mDbManager.insertComposition(compName , currentUser.getID() , fileName , curType);
                createCurrentComposition((int) id);
                dialog.dismiss();
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

    private void getInitData(){
        int userID = (int) getIntent().getLongExtra(USER_ID_TAG , -1);
        int compID = (int) getIntent().getLongExtra(COMP_ID_TAG , -1);
        if(userID != -1){
            currentUser = mDbManager.getUserByID(userID);
            ((TextView) findViewById(R.id.compositor_label)).setText(currentUser.getUserName());
        }
        if (compID != -1)
        {
            currentComposition = mDbManager.getCompByID(compID);
            fab.setVisibility(View.GONE);
            LoadedData data = MelodyFileManager.getManager().loadComposition(currentComposition.getJsonFileName());
            if (data.getType() == SHEET_TYPE_ONE_HANDED)
                melodyAdapter.setMelodyStringList1(MelodyFileManager.getManager().makeStringFromNotes(data.getComp1()));
            else {
                melodyAdapter.setMelodyStringList1(MelodyFileManager.getManager().makeStringFromNotes(data.getComp1()));
                melodyAdapter.setMelodyStringList2(MelodyFileManager.getManager().makeStringFromNotes(data.getComp2()));
            }
            melodyAdapter.notifyDataSetChanged();
        }
    }

    private void initActivity(){
        mMelodyBoard = new MelodyBoard(MainActivity.this);
        RecyclerView rv = (RecyclerView) findViewById(R.id.composition);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        melodyAdapter = new MelodyAdapter(mMelodyBoard);
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
    }

    private void createCurrentComposition(int id){
        currentComposition = mDbManager.getCompByID(id);
        MelodyFileManager.getManager().createEmptyJson(currentComposition, mDbManager);
        fab.setVisibility(View.GONE);
        if(melodyAdapter != null) {
            LoadedData data = MelodyFileManager.getManager().loadComposition(currentComposition.getJsonFileName());
            if (data.getType() == SHEET_TYPE_ONE_HANDED)
                melodyAdapter.setMelodyStringList1(MelodyFileManager.getManager().makeStringFromNotes(data.getComp1()));
            else {
                melodyAdapter.setMelodyStringList1(MelodyFileManager.getManager().makeStringFromNotes(data.getComp1()));
                melodyAdapter.setMelodyStringList2(MelodyFileManager.getManager().makeStringFromNotes(data.getComp2()));
            }
            melodyAdapter.notifyDataSetChanged();
        }
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
        playButton = menu.findItem(R.id.action_play_sound);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_save:
                if(melodyAdapter.getCompositionType() == SHEET_TYPE_ONE_HANDED) {
                    ArrayList<Note> a = MelodyFileManager.getManager().MakeNotesFromString(melodyAdapter.getMelodyStringList1());
                    MelodyFileManager.getManager().saveOneHandedComposition(a, currentUser.getUserName() , currentComposition.getCompositionName(),
                            currentComposition.getJsonFileName(), currentComposition.getCompositionID());
                }
                else {
                    ArrayList<Note> a = MelodyFileManager.getManager().MakeNotesFromString(melodyAdapter.getMelodyStringList1());
                    ArrayList<Note> b = MelodyFileManager.getManager().MakeNotesFromString(melodyAdapter.getMelodyStringList2());
                    MelodyFileManager.getManager().saveTwoHandedComposition(a, b, currentUser.getUserName() , currentComposition.getCompositionName() ,
                        currentComposition.getJsonFileName(), currentComposition.getCompositionID());
                }
                break;
            case R.id.action_settings:
                return true;
            case R.id.action_play_sound:
                togglePlayButton(false);
                ArrayList<Integer> sounds1 = MelodyFileManager.getManager().getResIDOfMusic(MelodyFileManager.getManager().MakeNotesFromString(melodyAdapter.getMelodyStringList1()));
                MelodyPoolManager.getInstance().setSounds1(sounds1);
                if (melodyAdapter.getCompositionType() == SHEET_TYPE_TWO_HANDED)
                {
                    ArrayList<Integer> sounds2 = MelodyFileManager.getManager().getResIDOfMusic(MelodyFileManager.getManager().MakeNotesFromString(melodyAdapter.getMelodyStringList2()));
                    MelodyPoolManager.getInstance().setSounds2(sounds2);
                }
                try {
                    MelodyPoolManager.getInstance().InitializeMelodyPool(this, new MelodyPoolManager.IMelodyPoolLoaded() {
                        @Override
                        public void onSuccess() {
                            MelodyPoolManager.getInstance().setPlaySound(true);
                            MelodyPoolManager.getInstance().playMelody(new MelodyPoolManager.IMelodyPoolPlaybackFinished() {
                                @Override
                                public void onFinishPlayBack() {
                                    togglePlayButton(true);
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.action_export:
                MelodyExporter melodyExporter = new MelodyExporter();
                ArrayList<Integer> sound1 = MelodyFileManager.getManager().getResIDOfMusic(MelodyFileManager.getManager().MakeNotesFromString(melodyAdapter.getMelodyStringList1()));
                melodyExporter.setSound1(sound1);
                melodyExporter.mergeSongs();
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

    private void togglePlayButton(boolean toggle){
        playButton.setEnabled(toggle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MelodyPoolManager.getInstance().clear();
    }
}
