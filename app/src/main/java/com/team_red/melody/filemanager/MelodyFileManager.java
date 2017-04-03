package com.team_red.melody.filemanager;



import com.team_red.melody.DBs.DbManager;
import com.team_red.melody.app.MelodyApplication;
import com.team_red.melody.R;
import com.team_red.melody.models.Composition;
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

import static com.team_red.melody.models.MelodyStatics.CODE_BASS_CLEF;
import static com.team_red.melody.models.MelodyStatics.CODE_SOL_CLEF;
import static com.team_red.melody.models.MelodyStatics.FLAG_PLAY;
import static com.team_red.melody.models.MelodyStatics.FLAG_SAVE;
import static com.team_red.melody.models.MelodyStatics.SHEET_TYPE_ONE_HANDED;
import static com.team_red.melody.models.MelodyStatics.SHEET_TYPE_TWO_HANDED;

public class MelodyFileManager {
    public static final String COMPOSER_JSON_TAG = "composer_name";
    public static final String COMPOSITION_NAME_JSON_TAG = "composition_name";
    public static final String COMPOSITION_ARRAY1_JSON_TAG = "composition1";
    public static final String COMPOSITION_ARRAY2_JSON_TAG = "composition2";
    public static final String COMPOSITION_ID_TAG = "_id";
    public static final String MAX_CHARACTERS_TAG = "max_chars_per_line";
    public static String COMPOSITION_JSON_DIR = MelodyApplication.getContext().getFilesDir()  + File.separator;
    public static String COMPOSITION_TYPE_TAG = "hand_type";
    public static String EXPORTED_FILE_DIRECTORY = "/Melody";

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

    public void createEmptyJson(Composition comp, DbManager manager){
        if (comp.getType() == SHEET_TYPE_ONE_HANDED)
            saveOneHandedComposition(new ArrayList<Note>(), manager.getUserByID(comp.getCompositorID()).getUserName(), comp.getCompositionName(), comp.getJsonFileName()
            , comp.getCompositionID());
        else
            saveTwoHandedComposition(new ArrayList<Note>(), new ArrayList<Note>(), manager.getUserByID(comp.getCompositorID()).getUserName(),
                    comp.getCompositionName(), comp.getJsonFileName(), comp.getCompositionID());
    }

    public ArrayList<Integer> getResIDOfMusic(ArrayList<Note> input){
        ArrayList<Integer> result = new ArrayList<>();
        for(int i = 0; i < input.size(); i++)
        {
            if(220 <= input.get(i).getValue() && input.get(i).getValue() < 290 ) {
                String tempName = input.get(i).toString() ;
                try {
                    int id = R.raw.class.getField(tempName).getInt(null);
                    result.add(id);
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public void saveOneHandedComposition(ArrayList<Note> composition, String composerName, String compositionName, String fileName, int _ID){
        JSONObject jsonComposition = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        OutputStream os;
        try{
            jsonComposition.put(COMPOSER_JSON_TAG, composerName);
            jsonComposition.put(COMPOSITION_NAME_JSON_TAG, compositionName);
            jsonComposition.put(COMPOSITION_TYPE_TAG , SHEET_TYPE_ONE_HANDED);
            jsonComposition.put(MAX_CHARACTERS_TAG , currentMaxCharacters);
            jsonComposition.put(COMPOSITION_ID_TAG , _ID);
            for (int i = 0; i < composition.size() ; i++)
            {
                jsonArray.put(i , composition.get(i).getJSONObject());
            }
            jsonComposition.put(COMPOSITION_ARRAY1_JSON_TAG, jsonArray);
        }catch (JSONException ex){
            ex.printStackTrace();
        }

        try{
            File f = new File(COMPOSITION_JSON_DIR + fileName);
            f.createNewFile();
            os = new FileOutputStream(f);
            os.write(jsonComposition.toString().getBytes());
            os.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void saveTwoHandedComposition(ArrayList<Note> comp1, ArrayList<Note> comp2, String composerName, String compositionName, String fileName,  int _ID){
        JSONObject jsonComposition = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        OutputStream os;
        try{
            jsonComposition.put(COMPOSER_JSON_TAG, composerName);
            jsonComposition.put(COMPOSITION_NAME_JSON_TAG, compositionName);
            jsonComposition.put(COMPOSITION_TYPE_TAG , SHEET_TYPE_TWO_HANDED);
            jsonComposition.put(MAX_CHARACTERS_TAG , currentMaxCharacters);
            jsonComposition.put(COMPOSITION_ID_TAG , _ID);
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
            File f = new File(COMPOSITION_JSON_DIR + fileName);
            f.createNewFile();
            os = new FileOutputStream(f);
            os.write(jsonComposition.toString().getBytes());
            os.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    public ArrayList<Note> MakeNotesFromString(ArrayList<String> input, int flag){
        if(flag == FLAG_PLAY)
            return MakeNotesFromStringPlay(input);
        else if(flag == FLAG_SAVE)
            return MakeNotesFromStringSave(input);
        return null;
    }

    private ArrayList<Note> MakeNotesFromStringSave(ArrayList<String> input){
        ArrayList<Note> result = new ArrayList<>();
        int maxCharactersPerLine = 0;
        for(int i = 0; i < input.size(); i++){
            if(input.get(i).length() > maxCharactersPerLine)
                maxCharactersPerLine = input.get(i).length();
            if(input.get(i).isEmpty())
                continue;
            char[] temp = input.get(i).toCharArray();
            int prevSign = 0;
            int clefType = (int) temp[0];
            result.add(new Note(clefType, 0, 0));
            for(int j = 1; j < temp.length; j++){
                int code = (int) temp[j];
                int octave = 4;
                if(clefType == CODE_SOL_CLEF){
                    if (code >= 290)
                        octave = 5;
                }
                else {
                    octave = 2;
                    if (code >= 280)
                        octave = 3;
                }
                if(code >= 200 && code < 360){
                    if(code%10 < 5) {
                        result.add(new Note(code, prevSign, octave));
                        prevSign = 0;
                    }
                    else{
                        prevSign = code;
                    }
                }
                else if(code > 400)
                    prevSign = code;
            }
        }
        currentMaxCharacters = maxCharactersPerLine;
        return result;
    }

    private ArrayList<Note> MakeNotesFromStringPlay(ArrayList<String> input){
        ArrayList<Note> result = new ArrayList<>();
        int maxCharactersPerLine = 0;
        for(int i = 0; i < input.size(); i++){
            if(input.get(i).length() > maxCharactersPerLine)
                maxCharactersPerLine = input.get(i).length();
            if(input.get(i).isEmpty())
                continue;
            char[] temp = input.get(i).toCharArray();
            int prevSign = 0;
            int clefType = (int) temp[0];
            result.add(new Note(clefType, 0, 0));
            for(int j = 1; j < temp.length; j++){
                int code = (int) temp[j];
                int octave = 4;
                if(clefType == CODE_SOL_CLEF) {
                    if (code >= 290 && code < 360) {
                        code = code - 70;
                        octave = 5;
                    }
                    if (code > 400 || code % 10 >= 5) {
                        prevSign = code;
                        continue;
                    }
                    result.add(new Note(code, prevSign, octave));
                    prevSign = 0;
                }
                else {
                    if(code < 280 && code >= 200){
                        code = code + 20;
                        octave = 2;
                    }
                    else if(code >= 280 && code < 350){
                        code = code - 60;
                        octave = 3;
                    }
                    if (code > 400 || code % 10 >= 5) {
                        prevSign = code;
                        continue;
                    }
                    result.add(new Note(code, prevSign, octave));
                    prevSign = 0;
                }
            }
        }
        currentMaxCharacters = maxCharactersPerLine;
        return result;
    }

    public ArrayList<String> makeStringFromNotes(ArrayList<Note> input){
        ArrayList<String> result = new ArrayList<>();
        if (input.size() != 0) {
            StringBuilder temp = new StringBuilder();
            temp.append((char) input.get(0).getValue());
            for (int i = 1; i < input.size(); i ++) {
                if (input.get(i).getValue() == CODE_BASS_CLEF || input.get(i).getValue() == CODE_SOL_CLEF){
                    result.add(temp.toString());
                    temp.delete(0, temp.length());
                }
                if (input.get(i).getSign() == 0)
                    temp.append((char) input.get(i).getValue());
                else {
                    if (input.get(i).getSign() > 400)
                        temp.append((char) input.get(i).getValue()).append(((char) input.get(i).getSign()));
                    else
                        temp.append((char) input.get(i).getSign()).append(((char) input.get(i).getValue()));
                }
            }
            result.add(temp.toString());
        }
        return result;
    }
}
