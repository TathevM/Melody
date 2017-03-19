package com.team_red.melody.filemanager;



import com.team_red.melody.MelodyApplication;
import com.team_red.melody.R;
import com.team_red.melody.models.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.team_red.melody.melodyboard.MelodyStatics.SHEET_TYPE_ONE_HANDED;
import static com.team_red.melody.melodyboard.MelodyStatics.SHEET_TYPE_TWO_HANDED;

public class MelodyFileManager {
    public static final String COMPOSER_JSON_TAG = "composer_name";
    public static final String COMPOSITION_NAME_JSON_TAG = "composition_name";
    public static final String COMPOSITION_ARRAY1_JSON_TAG = "composition1";
    public static final String COMPOSITION_ARRAY2_JSON_TAG = "composition2";
    public static final String MAX_CHARACTERS_TAG = "max_chars_per_line";
    public static String COMPOSITION_JSON_DIR = MelodyApplication.getContext().getFilesDir()  + File.separator;
    public static String SOUND_FILE_PREFIX = "s";
    public static String COMPOSITION_TYPE_TAG = "hand_type";

    private static MelodyFileManager melodyFileManager;
    private int currentMaxCharacters;

    public static MelodyFileManager getManager(){
        if(melodyFileManager == null){
            melodyFileManager = new MelodyFileManager();
        }
        return melodyFileManager;
    }

    private MelodyFileManager() {
    }

    public LoadedData loadComposition(String filename){
        LoadedData retValue = new LoadedData();
        try {
            File f = new File(COMPOSITION_JSON_DIR +  filename);
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONObject jsonComposition = new JSONObject(new String(buffer, "UTF-8"));
            currentMaxCharacters = jsonComposition.getInt(MAX_CHARACTERS_TAG);
            JSONArray jsonCompositionArray1= jsonComposition.getJSONArray(COMPOSITION_ARRAY1_JSON_TAG);
            ArrayList<Note> array1 = new ArrayList<>();
            for(int i=0; i<jsonCompositionArray1.length() ; i++)
            {
                array1.add(Note.getNoteFromJson(jsonCompositionArray1.getJSONObject(i)));
            }
            retValue.setComp1(array1);
            if(jsonComposition.getInt(COMPOSITION_TYPE_TAG) == SHEET_TYPE_TWO_HANDED)
            {
                ArrayList<Note> array2 = new ArrayList<>();
                JSONArray jsonCompositionArray2 = jsonComposition.getJSONArray(COMPOSITION_ARRAY2_JSON_TAG);
                for(int i=0; i<jsonCompositionArray1.length() ; i++)
                {
                    array2.add(Note.getNoteFromJson(jsonCompositionArray2.getJSONObject(i)));
                }
                retValue.setComp2(array2);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return retValue;
    }

    public ArrayList<Integer> getResIDOfMusic(ArrayList<Note> input){
        ArrayList<Integer> result = new ArrayList<>();
        for(int i = 0; i < input.size(); i++)
        {
            if(220 <= input.get(i).getValue() && input.get(i).getValue() < 290 ) {
                String tempName = SOUND_FILE_PREFIX + String.valueOf(input.get(i).getValue());
                try {
                    int id = R.raw.class.getField(tempName).getInt(null);
                    result.add(id);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public void saveOneHandedComposition(ArrayList<Note> composition, String composerName, String compositionName, int _ID){
        JSONObject jsonComposition = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        OutputStream os;
        try{
            jsonComposition.put(COMPOSER_JSON_TAG, composerName);
            jsonComposition.put(COMPOSITION_NAME_JSON_TAG, compositionName);
            jsonComposition.put(COMPOSITION_TYPE_TAG , SHEET_TYPE_ONE_HANDED);
            jsonComposition.put(MAX_CHARACTERS_TAG , currentMaxCharacters);
            for (int i = 0; i < composition.size() ; i++)
            {
                jsonArray.put(i , composition.get(i).getJSONObject());
            }
            jsonComposition.put(COMPOSITION_ARRAY1_JSON_TAG, jsonArray);
        }catch (JSONException ex){
            ex.printStackTrace();
        }

        try{
            File f = new File(COMPOSITION_JSON_DIR + composerName + " - " + compositionName);
            f.createNewFile();
            os = new FileOutputStream(f);
            os.write(jsonComposition.toString().getBytes());
            os.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void saveTwoHandedComposition(ArrayList<Note> comp1, ArrayList<Note> comp2, String composerName, String compositionName, int _ID){
        JSONObject jsonComposition = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        OutputStream os;
        try{
            jsonComposition.put(COMPOSER_JSON_TAG, composerName);
            jsonComposition.put(COMPOSITION_NAME_JSON_TAG, compositionName);
            jsonComposition.put(COMPOSITION_TYPE_TAG , SHEET_TYPE_TWO_HANDED);
            jsonComposition.put(MAX_CHARACTERS_TAG , currentMaxCharacters);
            for (int i = 0; i < comp1.size() ; i++)
            {
                jsonArray.put(i , comp1.get(i).getJSONObject());
            }
            jsonComposition.put(COMPOSITION_ARRAY1_JSON_TAG, jsonArray);

            jsonArray = new JSONArray();
            for (int i = 0; i < comp2.size() ; i++)
            {
                jsonArray.put(i , comp2.get(i).getJSONObject());
            }
            jsonComposition.put(COMPOSITION_ARRAY2_JSON_TAG, jsonArray);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        try{
            File f = new File(COMPOSITION_JSON_DIR + composerName + " - " + compositionName);
            f.createNewFile();
            os = new FileOutputStream(f);
            os.write(jsonComposition.toString().getBytes());
            os.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public ArrayList<Note> MakeNotesFromString(ArrayList<String> input){
        ArrayList<Note> result = new ArrayList<>();
        int maxCharactersPerLine = 0;
        for(int i = 0; i < input.size(); i++){
            if(input.get(i).length() > maxCharactersPerLine)
                maxCharactersPerLine = input.get(i).length();
            char[] temp = input.get(i).toCharArray();
            int prevSign = 0;

            for(int j = 0; j < temp.length; j++){
                int code = (int) temp[j];

                if(code >= 200 && code < 360)
                {
                    if(code%10 < 5) {
                        result.add(new Note(code, prevSign, 0));
                        prevSign = 0;
                    }
                    else{
                        prevSign = code;
                    }
                }
                else if (code > 400)
                    prevSign = code;
            }
        }
        currentMaxCharacters = maxCharactersPerLine;
        return result;
    }

    public ArrayList<String> makeStringFromNotes(ArrayList<Note> input){
        ArrayList<String> result = new ArrayList<>();
        int offset = currentMaxCharacters;
        for(int i = 0; i < input.size(); i += offset) {
            List<Note> subList;
            if (i + 12 <= input.size())
                subList = input.subList(i , i + offset);
            else
                subList = input.subList( i , input.size());
            String temp = "";
            for (int j = 0; j < subList.size() ; j++) {
                if (subList.get(i).getSign() == 0)
                    temp = temp + ((char) subList.get(j).getValue());
                else {
                    if(subList.get(i).getSign() > 400)
                        temp = temp +  ((char) subList.get(j).getValue()) + ((char) subList.get(j).getSign());
                    else
                        temp = temp + ((char) subList.get(j).getSign()) + ((char) subList.get(j).getValue());
                }
            }
            result.add(temp);
        }
        return result;
    }
}
