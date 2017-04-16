package com.team_red.melody.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

import static com.team_red.melody.filemanager.MelodyFileManager.EXPORTED_FILE_DIRECTORY;
import static com.team_red.melody.filemanager.MelodyFileManager.MELODY_DIR;
import static com.team_red.melody.filemanager.MelodyFileManager.SHEET_DIR;


public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {

    String MP3_SHARE_PATH = android.os.Environment.getExternalStorageDirectory()
            + EXPORTED_FILE_DIRECTORY + MELODY_DIR;
    String PDF_SHARE_PATH = android.os.Environment.getExternalStorageDirectory()
            + EXPORTED_FILE_DIRECTORY + SHEET_DIR;

    private Context context;
    File[] files1;
    File[] files2;
    List<String> mFiles = new ArrayList<>();

    private OnListItemClickListener onListItemClickListener;

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }


    public ShareAdapter(Context context) {
        getFiles();
        this.context = context;
    }

    void getFiles(){

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

            // if substring = mp3, type_image1, else - type_image
            mTypeImage = (ImageView) itemView.findViewById(R.id.type_image);
            mTypeImage = (ImageView) itemView.findViewById(R.id.type_image1);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onListItemClickListener.onItemClick(getAdapterPosition(), view);

        }
    }

    public void share(int position){
        String sharePath;
        String melody = mFiles.get(position);

        Intent share = new Intent(Intent.ACTION_SEND);
        if (melody.substring(melody.length()-4 , melody.length()-1).equalsIgnoreCase("mp3")){
            sharePath = "/" + MP3_SHARE_PATH + melody;
            share.setType("audio/mp3");
        }
        else{
            sharePath = "/" + PDF_SHARE_PATH + melody;
            share.setType("application/pdf");
        }


        File file = new File(sharePath);
        Uri uri = Uri.fromFile(file);

        share.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(share, "Sharing: " + melody));
    }

    public interface OnListItemClickListener{
        void onItemClick(int position, View view);
    }
}
