package com.team_red.melody.filemanager;


import android.content.Context;

import com.team_red.melody.Melody;
import com.team_red.melody.models.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class MelodyFileManager {
    public static final String COMPOSER_TAG = "composer_name";
    public static final String COMPOSITION_NAME_TAG = "composition_name";
    public static final String COMPOSITION_ARRAY_TAG = "composition";
    public static final String COMPOSITION_DIR = "compositions";

    private static MelodyFileManager melodyFileManager;

    public static MelodyFileManager getMelodyFileManager(){
        if(melodyFileManager == null){
            melodyFileManager = new MelodyFileManager();
        }
        return melodyFileManager;
    }

    private MelodyFileManager() {
    }

    public ArrayList<Note> getComposition(int id){
//        try {
//            File f = new File("/data/data/" + getPackageName() + "/" + params);
//            FileInputStream is = new FileInputStream(f);
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            String mResponse = new String(buffer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String json = null;
//        try {
//
//            InputStream is = getAssets().open("file_name.json");
//
//            int size = is.available();
//
//            byte[] buffer = new byte[size];
//
//            is.read(buffer);
//
//            is.close();
//
//            json = new String(buffer, "UTF-8");
//
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
        return null;
    }

    public void saveComposition(ArrayList<Note> composition, String composerName, String compositionName){
        JSONObject jsonComposition = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        FileOutputStream os;
        try{
            jsonComposition.put(COMPOSER_TAG , composerName);
            jsonComposition.put(COMPOSITION_NAME_TAG, compositionName);
            for (int i = 0; i<composition.size() ; i++)
            {
                jsonArray.put(i , composition.get(i).getJSONObject());
            }
            jsonComposition.put(COMPOSITION_ARRAY_TAG , jsonArray);
        }catch (JSONException ex){
            ex.printStackTrace();
        }

        try{
            os = Melody.getContext().openFileOutput(COMPOSITION_DIR + "/" + composerName + " - " + compositionName , Context.MODE_PRIVATE);
            os.write(jsonComposition.toString().getBytes());
            os.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
