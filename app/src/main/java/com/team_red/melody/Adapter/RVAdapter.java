package com.team_red.melody.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team_red.melody.R;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{

    static  final boolean  IS_USER_CHOSEN = false;



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (!IS_USER_CHOSEN) {

            holder.mUsername.setText("Alam Ashxarh");
            holder.mUserInfo.setText("8 erg ");

        }
        else
            holder.mUsername.setText("Alam Ashxarh");
        holder.mUserInfo.setText("Ashxarhums imn dun is ");
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_or_composition, parent );

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mUsername;
        private TextView mUserInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            mUsername = (TextView) itemView.findViewById(R.id.userName);
            mUserInfo = (TextView) itemView.findViewById(R.id.userOrCompInfo);
        }
    }
}
