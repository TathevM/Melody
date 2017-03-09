package com.team_red.melody.filemanager;


import android.content.Context;

import com.team_red.melody.Melody;
import com.team_red.melody.models.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MelodyFileManager {
    public static final String COMPOSER_JSON_TAG = "composer_name";
    public static final String COMPOSITION_NAME_JSON_TAG = "composition_name";
    public static final String COMPOSITION_ARRAY_JSON_TAG = "composition";
    public static final String COMPOSITION_JSON_DIR = "compositions";

    private static MelodyFileManager melodyFileManager;

    public static MelodyFileManager getMelodyFileManager(){
        if(melodyFileManager == null){
            melodyFileManager = new MelodyFileManager();
        }
        return melodyFileManager;
    }

    private MelodyFileManager() {
    }

    public ArrayList<Note> loadComposition(String filename){
        ArrayList<Note> retValue = new ArrayList<>();
        try {
            File f = new File(Melody.getContext().getFilesDir() + "/" + filename);
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONObject jsonComposition = new JSONObject(new String(buffer, "UTF-8"));
            JSONArray jsonCompositionArray = jsonComposition.getJSONArray(COMPOSITION_ARRAY_JSON_TAG);
            for(int i=0; i<jsonCompositionArray.length() ; i++)
            {
                retValue.add(Note.getNoteFromJson(jsonCompositionArray.getJSONObject(i)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return retValue;
    }

    public void saveComposition(ArrayList<Note> composition, String composerName, String compositionName, int _ID){
        JSONObject jsonComposition = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        FileOutputStream os;
        try{
            jsonComposition.put(COMPOSER_JSON_TAG, composerName);
            jsonComposition.put(COMPOSITION_NAME_JSON_TAG, compositionName);
            for (int i = 0; i<composition.size() ; i++)
            {
                jsonArray.put(i , composition.get(i).getJSONObject());
            }
            jsonComposition.put(COMPOSITION_ARRAY_JSON_TAG, jsonArray);
        }catch (JSONException ex){
            ex.printStackTrace();
        }

        try{
            os = Melody.getContext().openFileOutput(COMPOSITION_JSON_DIR + "/" + composerName + " - " + compositionName , Context.MODE_PRIVATE);
            os.write(jsonComposition.toString().getBytes());
            os.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
