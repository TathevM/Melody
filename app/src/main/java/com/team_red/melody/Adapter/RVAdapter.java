package com.team_red.melody.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.team_red.melody.DBs.DbManager;
import com.team_red.melody.app.MelodyApplication;
import com.team_red.melody.R;
import com.team_red.melody.models.Composition;
import com.team_red.melody.models.User;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{

    public boolean  IS_USER_CHOSEN = false;
    private ArrayList<User> usersList = new ArrayList<>();
    private ArrayList<Composition> compositionsList;
    DbManager dbManager = new DbManager(MelodyApplication.getContext());
    Context context;

    public RVAdapter(Context context) {
        this.context = context;
    }

    private OnListItemClickListener onListItemClickListener;

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    public void setUsersList(ArrayList<User> usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    public void setCompositionsList(ArrayList<Composition> compositionsList) {
        this.compositionsList = compositionsList;
//        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (!IS_USER_CHOSEN) {
            holder.mUserOrCompName.setText(usersList.get(position).getUserName());
            holder.mDeleteButton.setVisibility(View.GONE);
        }
        else {
            holder.mUserOrCompName.setText(compositionsList.get(position).getCompositionName());
            holder.mDeleteButton.setVisibility(View.VISIBLE);
            holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return  IS_USER_CHOSEN ? compositionsList.size() : usersList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_or_composition, parent, false );
        return new ViewHolder(view);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mUserOrCompName;
        private ImageButton mDeleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            mUserOrCompName = (TextView) itemView.findViewById(R.id.userOrCompName);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
//            mDeleteButton.setVisibility(View.GONE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(onListItemClickListener != null) {
                if (!IS_USER_CHOSEN) {
                    onListItemClickListener.onItemClick(usersList.get(getAdapterPosition()).getID(), view);
                }
                else
                    onListItemClickListener.onItemClick(compositionsList.get(getAdapterPosition()).getCompositionID(), view);
            }
        }
    }

    public interface OnListItemClickListener{
        void onItemClick(int ID, View view);
    }

    public void onDeleteClick(final int position){

        new AlertDialog.Builder(context)
                .setTitle(R.string.alert_delete_title)
                .setMessage(R.string.alert_delete_text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbManager.deleteCompByID(compositionsList.get(position).getCompositionID());
                        compositionsList.remove(position);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
