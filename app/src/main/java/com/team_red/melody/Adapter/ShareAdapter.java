package com.team_red.melody.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.team_red.melody.R;
import com.team_red.melody.app.MelodyApplication;
import com.team_red.melody.models.MelodyStatics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.team_red.melody.filemanager.MelodyFileManager.MP3_SHARE_PATH;
import static com.team_red.melody.filemanager.MelodyFileManager.PDF_SHARE_PATH;


public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {

    private Context context;
    private File[] files1;
    private File[] files2;
    private List<String> mFiles = new ArrayList<>();

    private OnListItemClickListener onListItemClickListener;

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }


    public ShareAdapter(Context context) {
        getFiles();
        this.context = context;
    }

    private void getFiles(){

        File dirMP3 = new File(MP3_SHARE_PATH);
        files1 = dirMP3.listFiles();
        File dirPDF = new File(PDF_SHARE_PATH);
        files2 = dirPDF.listFiles();

        for(File file : files1) {
            mFiles.add(file.getName());
        }
        for(File file : files2) {
            mFiles.add(file.getName());
        }
    }

    @Override
    public int getItemCount() {
        return  mFiles.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_share_file, parent, false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mShareItem.setText(mFiles.get(position));
        if(mFiles.get(holder.getAdapterPosition()).endsWith("mp3")) {
            holder.mTypeImage.setImageResource(R.drawable.icon_mp3);
        }

//        holder.mTypeImage.setImageDrawable();

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mShareItem;
        private ImageView mTypeImage;

        ViewHolder(View itemView) {
            super(itemView);
            mShareItem = (TextView) itemView.findViewById(R.id.share_item);
            Typeface typeface = Typeface.createFromAsset(MelodyApplication.getContext().getAssets(), MelodyStatics.MAIN_FONT_NAME);
            mShareItem.setTypeface(typeface);
            mShareItem.setTextSize(30);
            mTypeImage = (ImageView) itemView.findViewById(R.id.type_image);
            // if substring = mp3, type_image1, else - type_image


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onListItemClickListener.onItemClick(getAdapterPosition(), view);
        }
    }

        String openPath;
        String melody = mFiles.get(position);

        Intent open = new Intent(Intent.ACTION_VIEW);
        if (melody.endsWith("mp3")){
            openPath = MP3_SHARE_PATH + "/" + melody;
            open.setDataAndType(Uri.fromFile(new File(openPath)), "audio/mp3");
        }
        else{
            openPath = PDF_SHARE_PATH + "/" + melody;
            open.setDataAndType(Uri.fromFile(new File(openPath)), "application/pdf");
        }

        // context.startActivity(Intent.createChooser(open, "Choose application to preview"));
        // OR
        context.startActivity(open);
    }

    public void share(int position){
        String sharePath;
        String melody = mFiles.get(position);

        Intent share = new Intent(Intent.ACTION_SEND);
        if (melody.endsWith("mp3")){
            sharePath = MP3_SHARE_PATH + "/" + melody;
            share.setType("audio/mp3");
        }
        else{
            sharePath = PDF_SHARE_PATH + "/" + melody;
            share.setType("application/pdf");
        }
        File file = new File(sharePath);
        Uri uri = Uri.fromFile(file);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(share, "Sharing: " + melody));
    }

    public void showPopup(final int position , View view){

        PopupMenu popup = new PopupMenu(context, view.findViewById(R.id.share_item));
        popup.getMenuInflater().inflate(R.menu.popup_menu_share, popup.getMenu());
        popup.setGravity(Gravity.END);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_open:
                    {
                        break;
                    }
                    case R.id.action_share:
                    {
                        share(position);
                        break;
                    }
                }
                return true;
            }
        });
        popup.show();
    }


    public interface OnListItemClickListener{
        void onItemClick(int position, View view);
    }

}
