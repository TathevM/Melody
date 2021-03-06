package com.team_red.melody.StartActivityFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.team_red.melody.Adapter.RVAdapter;
import com.team_red.melody.DBs.DbManager;
import com.team_red.melody.R;
import com.team_red.melody.activities.CompositionsActivity;
import com.team_red.melody.app.MelodyApplication;
import com.team_red.melody.models.User;

public class UsersOrCompsListFragment extends Fragment {

    private RVAdapter adapter;
    private DbManager mdbManager;
    private Intent myIntent;
    private long selectedUserID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.users_or_compositions_list_fragment, container , false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mdbManager = new DbManager(getContext());
        adapter = new RVAdapter(getActivity());
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
        adapter.setOnListItemLongClickListener(new RVAdapter.OnListItemLongClickListener() {
            @Override
            public void onItemLongClick(final int ID, View view) {
                adapter.showPopup(ID, view);
            }
        });
        adapter.setOnListItemClickListener(new RVAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(int ID, View view1) {
                if (!adapter.IS_USER_CHOSEN) {
                    adapter.IS_USER_CHOSEN = true;
                    selectedUserID = ID;
                    myIntent = new Intent(getActivity(), CompositionsActivity.class);
                    User selectedUser = mdbManager.getUserByID((int) selectedUserID);
                    MelodyApplication.setLoggedInUser(selectedUser);
                    startActivity(myIntent);
                    getActivity().finish();
                }
            }
        });
    }
}
