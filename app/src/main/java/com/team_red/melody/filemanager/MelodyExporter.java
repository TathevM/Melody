package com.team_red.melody.filemanager;

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

    private ArrayList<Integer> sound1;
    public static final String TEMPORARY_MP3_FILE_NAME = "tempmp3";

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
        File mergedFile = new File(COMPOSITION_JSON_DIR + "asd.mp3");
        ArrayList<File> mp3Files = prepareExport();

        FileInputStream fisToFinal = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mergedFile);
            fisToFinal = new FileInputStream(mergedFile);
            for(File mp3File:mp3Files){
                if(!mp3File.exists())
                    continue;
                FileInputStream fisSong = new FileInputStream(mp3File);
                SequenceInputStream sis = new SequenceInputStream(fisToFinal, fisSong);
                byte[] buf = new byte[1024];
                try {
                    for (int readNum; (readNum = fisSong.read(buf)) != -1;) {
//                        if(buf[890] == 85)
//                            break;
                        fos.write(buf, 0, readNum);
                    }
                } finally {
                    fisSong.close();
                    sis.close();
                    mp3File.delete();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
