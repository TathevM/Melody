package com.team_red.melody.StartActivityFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team_red.melody.Adapter.RVAdapter;
import com.team_red.melody.DBs.DbManager;
import com.team_red.melody.MainActivity;
import com.team_red.melody.R;



public class UsersOrCompsListFragment extends Fragment {

    RVAdapter adapter;
    DbManager mdbManager;
    Intent myIntent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users_or_compositions_list_fragment, container , false);
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mdbManager = new DbManager(getContext());
        adapter = new RVAdapter();
        adapter.setUsersList(mdbManager.getUsers());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.usersOrCompositionsList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
        adapter.setOnListItemClickListener(new RVAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(int ID) {
                if (!adapter.IS_USER_CHOSEN) {

                    adapter.setCompositionsList(mdbManager.getCompositions(ID));

                    adapter.IS_USER_CHOSEN = true;
                    adapter.notifyDataSetChanged();
                }
                else
                    myIntent = new Intent(getActivity(), MainActivity.class);
                    myIntent.putExtra("id",ID);
                    startActivity(myIntent);

            }
        });
    }
}
