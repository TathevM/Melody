package com.team_red.melody;

import android.app.Dialog;
import android.content.Intent;

import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.team_red.melody.Adapter.RVAdapter;
import com.team_red.melody.DBs.DbManager;

import com.team_red.melody.filemanager.MelodyFileManager;
import com.team_red.melody.models.Composition;
import com.team_red.melody.models.User;

import static com.team_red.melody.StartActivityFragments.LoginFragment.COMP_ID_TAG;
import static com.team_red.melody.StartActivityFragments.LoginFragment.USER_ID_TAG;
import static com.team_red.melody.melodyboard.MelodyStatics.SHEET_TYPE_ONE_HANDED;
import static com.team_red.melody.melodyboard.MelodyStatics.SHEET_TYPE_TWO_HANDED;

public class CompositionsActivity extends AppCompatActivity {

    private RVAdapter adapter;
    private DbManager mdbManager;
    private RecyclerView rv;

    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compositions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.actionbar));
        }


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




    public  void  initData() {

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        adapter.setOnListItemClickListener(new RVAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(int ID) {
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
        dialog.setTitle("Pick page type");
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        Button dialogOK = (Button) dialog.findViewById(R.id.button_dialog_OK);
        dialogOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) dialog.findViewById(R.id.dialog_new_comp_name);
                if (editText.getText().toString().equals("")){
                    Toast.makeText(CompositionsActivity.this,"Write your name maestro", Toast.LENGTH_SHORT).show();
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
}
