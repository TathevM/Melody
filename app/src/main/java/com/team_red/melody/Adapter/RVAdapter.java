package com.team_red.melody.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.team_red.melody.R;
import com.team_red.melody.models.Composition;
import com.team_red.melody.models.User;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{

    public boolean  IS_USER_CHOSEN = false;
    private ArrayList<User> usersList = new ArrayList<>();
    private ArrayList<Composition> compositionsList;

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
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!IS_USER_CHOSEN) {
            holder.mUserOrCompName.setText(usersList.get(position).getUserName());
        }
        else
            holder.mUserOrCompName.setText(compositionsList.get(position).getCompositionName());
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

        ViewHolder(View itemView) {
            super(itemView);
            mUserOrCompName = (TextView) itemView.findViewById(R.id.userOrCompName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(onListItemClickListener != null) {
                if (!IS_USER_CHOSEN) {
                    onListItemClickListener.onItemClick(usersList.get(getAdapterPosition()).getID());
                }
                else
                    onListItemClickListener.onItemClick(compositionsList.get(getAdapterPosition()).getCompositionID());
            }
        }
    }

    public interface OnListItemClickListener{
        void onItemClick(int ID);
    }
}
