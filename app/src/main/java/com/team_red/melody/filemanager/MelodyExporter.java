package com.team_red.melody.filemanager;

import android.content.Context;
import android.os.AsyncTask;

import com.team_red.melody.MelodyApplication;
import com.team_red.melody.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;

import static com.team_red.melody.filemanager.MelodyFileManager.COMPOSITION_JSON_DIR;

public class MelodyExporter {

    private int mFrameSize;
    private int mFramePosition;
    private ArrayList<Integer> sound1;
    private Context mContext;
    public static final String TEMPORARY_MP3_FILE_NAME = "tempmp3";

    public MelodyExporter(int frameSize, int framePosition, Context context) {
        this.mContext = context;
        mFrameSize = frameSize;
        mFramePosition = framePosition;
        mFrameSize = (144 * 160000) / (44100);
    }

    public void setSound1(ArrayList<Integer> sound1) {
        this.sound1 = sound1;
    }

    private ArrayList<File> prepareExport(){
        ArrayList<File> result = new ArrayList<>();
        for (int i = 0; i <sound1.size(); i++)
        {
            File file = new File(COMPOSITION_JSON_DIR + sound1.get(i));
            try {
                InputStream inputStream = MelodyApplication.getContext().getResources().openRawResource(sound1.get(i));
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                byte buf[]=new byte[1024];
                int len;
                while((len=inputStream.read(buf))>0) {
                    fileOutputStream.write(buf,0,len);
                }

                fileOutputStream.close();
                inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            result.add(file);
        }
        return result;
    }

    //public void mergeSongs(File mergedFile,File...mp3Files){
    public void mergeSongs(){
        SongExporterTask task = new SongExporterTask();
        task.execute();
    }

    private class SongExporterTask extends AsyncTask<Void,  Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            File mergedFile = new File(COMPOSITION_JSON_DIR + "asd.mp3");
            ArrayList<File> mp3Files = prepareExport();
            FileInputStream fisToFinal = null;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mergedFile);
                fisToFinal = new FileInputStream(mergedFile);
                for(int i = 0; i < mp3Files.size(); i++){
                    File mp3File = mp3Files.get(i);
                    if(!mp3File.exists())
                        continue;
                    FileInputStream fisSong = new FileInputStream(mp3File);
                    SequenceInputStream sis = new SequenceInputStream(fisToFinal, fisSong);
                    byte[] t = new byte[70];
                    fos.write(t, 0, fisSong.read(t));
                    byte[] buf = new byte[mFrameSize];
                    try {
                        int k=0;
                        for (int readNum; (readNum = fisSong.read(buf)) != -1; k++) {
                            if(k == 15)
                                break;
                            fos.write(buf, 0, readNum);
                        }
                    } finally {
                        fisSong.close();
                        sis.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    if(fos!=null){
                        fos.flush();
                        fos.close();
                    }
                    if(fisToFinal!=null){
                        fisToFinal.close();
                    }
                    for(File mp3file:mp3Files)
                        mp3file.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
