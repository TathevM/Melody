package com.team_red.melody.filemanager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.team_red.melody.R;
import com.team_red.melody.app.MelodyApplication;
import com.team_red.melody.models.Composition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;

import static com.team_red.melody.filemanager.MelodyFileManager.COMPOSITION_JSON_DIR;
import static com.team_red.melody.filemanager.MelodyFileManager.EXPORTED_FILE_DIRECTORY;

public class MelodyExporter {

    private static final int FRAME_SIZE = (144 * 160000) / (44100);

    private int mFrameSize;
    private int mHeaderPosition;
    private ArrayList<Integer> sound1;
    private Context mContext;
    private Composition mComposition;
    private MPEG mpeg;

    public MelodyExporter( Context context) {
        this.mContext = context;
        mFrameSize = FRAME_SIZE;
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
    public void mergeSongs(Composition composition){
        this.mComposition = composition;
        mpeg = new MPEG();
        SongExporterTask task = new SongExporterTask();
        task.execute();
    }

    private class SongExporterTask extends AsyncTask<Void,  Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            if (isExternalStorageWritable()) {
                File root = android.os.Environment.getExternalStorageDirectory();
                String username = MelodyApplication.getLoggedInUser().getUserName();
                String compName = mComposition.getCompositionName();
                File dir = new File(root.getAbsolutePath() + EXPORTED_FILE_DIRECTORY);
                dir.mkdirs();
                File mergedFile = new File(dir, username + " " + compName + ".mp3");
                ArrayList<File> mp3Files = prepareExport();
                FileInputStream fisToFinal = null;
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mergedFile);
                    fisToFinal = new FileInputStream(mergedFile);
                    for (int i = 0; i < mp3Files.size(); i++) {
                        File mp3File = mp3Files.get(i);
                        mpeg.read(mp3File);
                        mHeaderPosition = mpeg.getHeaderPosition();
                        if (!mp3File.exists())
                            continue;
                        FileInputStream fisSong = new FileInputStream(mp3File);
                        SequenceInputStream sis = new SequenceInputStream(fisToFinal, fisSong);
                        byte[] t = new byte[mHeaderPosition];
                        fos.write(t, 0, fisSong.read(t));
                        byte[] buf = new byte[mFrameSize];
                        try {
                            int k = 0;
                            for (int readNum; (readNum = fisSong.read(buf)) != -1; k++) {
                                if (k == 12)
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
                } finally {
                    try {
                        if (fos != null) {
                            fos.flush();
                            fos.close();
                        }
                        if (fisToFinal != null) {
                            fisToFinal.close();
                        }
                        for (File mp3file : mp3Files)
                            mp3file.delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(mContext , mContext.getString(R.string.export_dir_help) + EXPORTED_FILE_DIRECTORY, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
