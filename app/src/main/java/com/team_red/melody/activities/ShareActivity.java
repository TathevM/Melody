package com.team_red.melody.activities;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.team_red.melody.Adapter.ShareAdapter;
import com.team_red.melody.R;
import com.team_red.melody.widget.MelodyEditText;

import static com.team_red.melody.models.MelodyStatics.MAIN_FONT_NAME;

public class ShareActivity extends AppCompatActivity{

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
}
