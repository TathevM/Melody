package com.team_red.melody.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.team_red.melody.DBs.DbManager;

import com.team_red.melody.app.MelodyApplication;
import com.team_red.melody.R;
import com.team_red.melody.models.MelodyStatics;
import com.team_red.melody.models.Composition;
import com.team_red.melody.models.User;

import java.util.ArrayList;

import static com.team_red.melody.models.MelodyStatics.MAIN_FONT_NAME;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{

    public boolean  IS_USER_CHOSEN = false;
    private ArrayList<User> usersList = new ArrayList<>();
    private ArrayList<Composition> compositionsList;
    private DbManager dbManager = new DbManager(MelodyApplication.getContext());
    private Context context;

    public RVAdapter(Context context) {
        this.context = context;
    }

    private OnListItemClickListener onListItemClickListener;
    private OnListItemLongClickListener onListItemLongClickListener;

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    public void setOnListItemLongClickListener(OnListItemLongClickListener onListItemLongClickListener) {
        this.onListItemLongClickListener = onListItemLongClickListener;
    }

    public void setUsersList(ArrayList<User> usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    public void setCompositionsList(ArrayList<Composition> compositionsList) {
        this.compositionsList = compositionsList;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,int position) {
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
                    onDeleteClick(holder.getAdapterPosition());
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


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener{

        private TextView mUserOrCompName;
        private ImageButton mDeleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            mUserOrCompName = (TextView) itemView.findViewById(R.id.userOrCompName);
            Typeface typeface = Typeface.createFromAsset(MelodyApplication.getContext().getAssets(), MelodyStatics.MAIN_FONT_NAME);
            mUserOrCompName.setTypeface(typeface);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
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

        @Override
        public boolean onLongClick(View view) {
            if (onListItemLongClickListener!=null){
                if (!IS_USER_CHOSEN) {
                    onListItemLongClickListener.onItemLongClick(usersList.get(getAdapterPosition()).getID(), view);
                }
                else
                    onListItemLongClickListener.onItemLongClick(getAdapterPosition(), view);
            }
            return false;
        }
    }

    public interface OnListItemClickListener{
        void onItemClick(int ID, View view);
    }

    public interface OnListItemLongClickListener{
        void onItemLongClick(int ID, View view);
    }

    public void renameCompOrUser(final int position){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_rename_dialog);
        dialog.setTitle(R.string.dialog_rename);
        final EditText renameText = (EditText) dialog.findViewById(R.id.dialog_rename_text);
        Typeface typeface = Typeface.createFromAsset(getAssets(), MAIN_FONT_NAME);
        renameText.setTypeface(typeface);

        if (!IS_USER_CHOSEN){
            renameText.setText( usersList.get(position).getUserName());
        }
        else {
            renameText.setText( compositionsList.get(position).getCompositionName());
        }

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        Button dialogOK = (Button) dialog.findViewById(R.id.button_dialog_accept);
        dialogOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String renamed = renameText.getText().toString();
                if (renamed.equals("")){
                    if(!IS_USER_CHOSEN){
                        Toast.makeText(context, R.string.new_user_hint, Toast.LENGTH_SHORT) .show();
                    }
                    else{
                        Toast.makeText( context, R.string.no_title, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (IS_USER_CHOSEN) {
                        compositionsList.get(position).setCompositionName(renamed);
                        notifyDataSetChanged();
                        int compID = compositionsList.get(position).getCompositionID();
                        dbManager.renameComposition(compID, renamed);
                    }
                    else {
                        usersList.get(position).setUserName(renamed);
                        notifyDataSetChanged();
                        int userID = usersList.get(position).getID();
                        dbManager.renameUser(userID ,renamed);
                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.findViewById(R.id.button_dialog_decline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void onDeleteClick(final int position){

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
