package com.team_red.melody.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.team_red.melody.DBs.DbManager;
import com.team_red.melody.R;
import com.team_red.melody.app.MelodyApplication;
import com.team_red.melody.filemanager.LoadedData;
import com.team_red.melody.filemanager.MelodyExporter;
import com.team_red.melody.filemanager.MelodyFileManager;
import com.team_red.melody.filemanager.MelodyPDF;
import com.team_red.melody.melodyboard.MelodyBoard;
import com.team_red.melody.models.Composition;
import com.team_red.melody.models.MelodyAdapter;
import com.team_red.melody.models.Note;
import com.team_red.melody.models.User;
import com.team_red.melody.sound.MelodyPoolManager;

import java.util.ArrayList;

import static com.team_red.melody.StartActivityFragments.LoginFragment.COMP_ID_TAG;
import static com.team_red.melody.models.MelodyStatics.FLAG_EXPORT;
import static com.team_red.melody.models.MelodyStatics.FLAG_PLAY;
import static com.team_red.melody.models.MelodyStatics.FLAG_SAVE;
import static com.team_red.melody.models.MelodyStatics.MAIN_FONT_NAME;
import static com.team_red.melody.models.MelodyStatics.SHEET_TYPE_ONE_HANDED;
import static com.team_red.melody.models.MelodyStatics.SHEET_TYPE_TWO_HANDED;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_WRITE_STORAGE = 112;


    private MelodyBoard mMelodyBoard;
    private MelodyAdapter melodyAdapter;
    private MenuItem playButton;
    private MenuItem exportPdfButton;
    private User currentUser;
    private Composition currentComposition;
    private DbManager mDbManager;
    private boolean hasPermission;
    private boolean isPdfExporting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDbManager = new DbManager(this);

        initActivity();
        getInitData();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navigationUsername = (TextView) headerView.findViewById(R.id.compositor);
        Typeface typeface = Typeface.createFromAsset(getAssets(), MAIN_FONT_NAME);
        navigationUsername.setTypeface(typeface);
        navigationUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        currentUser = MelodyApplication.getLoggedInUser();
        navigationUsername.setText(currentUser.getUserName());

        hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void getInitData() {
        int compID = (int) getIntent().getLongExtra(COMP_ID_TAG, -1);
        if (compID != -1) {
            currentComposition = mDbManager.getCompByID(compID);
            LoadedData data = MelodyFileManager.getManager().loadComposition(currentComposition.getJsonFileName());
            if (data.getType() == SHEET_TYPE_ONE_HANDED)
                melodyAdapter.setMelodyStringList1(MelodyFileManager.getManager().makeStringFromNotes(data.getComp1()));
            else {
                melodyAdapter.setMelodyStringList1(MelodyFileManager.getManager().makeStringFromNotes(data.getComp1()));
                melodyAdapter.setMelodyStringList2(MelodyFileManager.getManager().makeStringFromNotes(data.getComp2()));
            }
            melodyAdapter.notifyDataSetChanged();

            TextView compView = (TextView) findViewById(R.id.composition_label);
            compView.setText(currentComposition.getCompositionName());
            Typeface typeface = Typeface.createFromAsset(getAssets(), MAIN_FONT_NAME);
            compView.setTypeface(typeface);
        }
    }

    private void initActivity() {
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
                if (!recyclerView.canScrollVertically(1))
                    ((MelodyAdapter) recyclerView.getAdapter()).addNewLinesToList();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else  if (mMelodyBoard.isMelodyBoardVisible()) {
            mMelodyBoard.hideMelodyBoard();
        }else {
            alertSaveData(R.id.nav_compositions);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        playButton = menu.findItem(R.id.action_play_sound);
        exportPdfButton = menu.findItem(R.id.action_export_pdf);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                break;
            case R.id.action_play_sound:
                play();
                break;
            case R.id.action_export_mp3:
                exportToMP3();
                break;
            case R.id.action_export_pdf:
                exportToPdf();
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

        if (id == R.id.nav_change_theme) {
        } else if (id == R.id.nav_change_user) {
            alertSaveData(id);
        } else if (id == R.id.nav_compositions) {
            alertSaveData(id);
        }  else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void togglePlayButton(boolean toggle) {
        playButton.setEnabled(toggle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MelodyPoolManager.getInstance().clear();
    }

    private void save() {
        if (melodyAdapter.getCompositionType() == SHEET_TYPE_ONE_HANDED) {
            ArrayList<Note> a = MelodyFileManager.getManager().MakeNotesFromString(melodyAdapter.getMelodyStringList1(), FLAG_SAVE);
            MelodyFileManager.getManager().saveOneHandedComposition(a, currentUser.getUserName(), currentComposition.getCompositionName(),
                    currentComposition.getJsonFileName(), currentComposition.getCompositionID());
        } else {
            ArrayList<Note> a = MelodyFileManager.getManager().MakeNotesFromString(melodyAdapter.getMelodyStringList1(), FLAG_SAVE);
            ArrayList<Note> b = MelodyFileManager.getManager().MakeNotesFromString(melodyAdapter.getMelodyStringList2(), FLAG_SAVE);
            MelodyFileManager.getManager().saveTwoHandedComposition(a, b, currentUser.getUserName(), currentComposition.getCompositionName(),
                    currentComposition.getJsonFileName(), currentComposition.getCompositionID());
        }
        Toast.makeText(this , R.string.string_save_ok , Toast.LENGTH_SHORT).show();
    }

    private void play() {
        togglePlayButton(false);
        ArrayList<Integer> sounds1 = MelodyFileManager.getManager().getResIDOfMusic(MelodyFileManager.getManager().MakeNotesFromString(melodyAdapter.getMelodyStringList1(), FLAG_PLAY));
        MelodyPoolManager.getInstance().setSounds1(sounds1);
        if (melodyAdapter.getCompositionType() == SHEET_TYPE_TWO_HANDED) {
            ArrayList<Integer> sounds2 = MelodyFileManager.getManager().getResIDOfMusic(MelodyFileManager.getManager().MakeNotesFromString(melodyAdapter.getMelodyStringList2(), FLAG_PLAY));
            MelodyPoolManager.getInstance().setSounds2(sounds2);
        }
        try {
            MelodyPoolManager.getInstance().InitializeMelodyPool(new MelodyPoolManager.IMelodyPoolLoaded() {
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
    }

    private void alertSaveData(final int id) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_leave_comp)
                .setMessage(R.string.alert_save_comp)
                .setPositiveButton(R.string.toolbar_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        save();
                        if(id == R.id.nav_compositions)
                            exitToCompositions();
                        else
                            exitToStart();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(id == R.id.nav_compositions)
                            exitToCompositions();
                        else
                            exitToStart();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void exportToMP3(){
        if(hasPermission) {
                MelodyExporter melodyExporter = new MelodyExporter(this);
                ArrayList<Integer> sound1 = MelodyFileManager.getManager().getResIDOfMusic(MelodyFileManager.getManager().MakeNotesFromString(melodyAdapter.getMelodyStringList1(), FLAG_EXPORT));
                melodyExporter.setSound1(sound1);
                melodyExporter.mergeSongs(currentComposition);
        }
        else {
            requestPermission();
            exportToMP3();
        }
    }

    private void exportToPdf(){
        if(hasPermission) {
            toggleExportPdf(false);
            isPdfExporting = true;
            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                   if(isPdfExporting)
                       h.postDelayed(this, 1000);
                    else
                        toggleExportPdf(true);
                }
            }, 1000);
            RecyclerView rv = (RecyclerView) findViewById(R.id.composition);
            TextView compView = (TextView) findViewById(R.id.composition_label);
            MelodyPDF melodyPDF = new MelodyPDF();
            melodyPDF.export(compView, rv, currentComposition, new MelodyPDF.IOnPDFExportFinishedListener() {
                @Override
                public void onPdfExportFinished() {
                    Toast.makeText(getApplicationContext() , R.string.pdf_export_complete , Toast.LENGTH_SHORT).show();
                    isPdfExporting = false;
                }
            });
        }
        else {
            requestPermission();
            exportToPdf();
        }
    }

    private void toggleExportPdf(boolean state){
        exportPdfButton.setEnabled(state);
        if (state)
            exportPdfButton.setTitle(R.string.toolbar_export_PDF);
        else
            exportPdfButton.setTitle(R.string.pdf_exporting_in_process);
    }

    private void exitToCompositions(){
        Intent intent = new Intent(MainActivity.this, CompositionsActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void exitToStart(){
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, R.string.permission_text, Toast.LENGTH_LONG).show();

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_WRITE_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.

                }
            }
        }
    }
}
