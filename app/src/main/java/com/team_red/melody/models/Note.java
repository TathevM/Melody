package com.team_red.melody.models;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import static com.team_red.melody.melodyboard.MelodyStatics.DOUBLE_FLAT_DIVISOR;
import static com.team_red.melody.melodyboard.MelodyStatics.DOUBLE_SHARP_DIVISOR;
import static com.team_red.melody.melodyboard.MelodyStatics.FLAT_DIVISOR;
import static com.team_red.melody.melodyboard.MelodyStatics.NATURAL_DIVISOR;
import static com.team_red.melody.melodyboard.MelodyStatics.SHARP_DIVISOR;

public class Note implements Parcelable {
    public static final String NOTE_VALUE_JSON_TAG = "value";
    public static final String NOTE_SIGN_JSON_TAG = "sign";
    public static final String NOTE_OCTAVE_JSON_TAG = "octave";

    private int value;
    private int sign;
    private int octave;

    public Note(int value, int sign, int octave) {
        this.value = value;
        this.sign = sign;
        this.octave = octave;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(value);
        out.writeInt(sign);
        out.writeInt(octave);
    }

    public static final Parcelable.Creator<Note> CREATOR
            = new Parcelable.Creator<Note>() {
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    private Note(Parcel in) {
        value = in.readInt();
        sign = in.readInt();
        octave = in.readInt();
    }

    public JSONObject getJSONObject(){
        JSONObject object = new JSONObject();
        try {
            object.put(NOTE_VALUE_JSON_TAG , value);
            object.put(NOTE_SIGN_JSON_TAG , sign);
            object.put(NOTE_OCTAVE_JSON_TAG , octave);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return object;
    }

    public static Note getNoteFromJson(JSONObject jsonNote){
        int value = 0;
        int sign = 0;
        int octave = 0;
        try {
            value = jsonNote.getInt(NOTE_VALUE_JSON_TAG);
            sign = jsonNote.getInt(NOTE_SIGN_JSON_TAG);
            octave = jsonNote.getInt(NOTE_OCTAVE_JSON_TAG);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return new Note(value , sign , octave);
    }

    @Override
    public String toString() {
        String temp="";
        if(sign%10==SHARP_DIVISOR){
            temp = "s";
        }
        else
            if(sign%10==DOUBLE_SHARP_DIVISOR){
                temp = "p";
            }
            else
                if(sign%10 == FLAT_DIVISOR){
                    temp = "f";
                }
                else
                    if(sign%10 == DOUBLE_FLAT_DIVISOR){
                        temp = "t";
                    }
                    else
                        if(sign%10 == NATURAL_DIVISOR || sign == 0){
                            temp = "n";
                        }
        return temp + octave + "_" + value;
    }

    public int getDuration(){
        int result=0;
        if(value%10==0){
            result = 1000;
        }
        else
            if(value%10==1){
                result = 500;
            }
            else
                if(value%10==2){
                    result = 250;
                }
                else
                    if(value%10==3){
                        result = 125;
                    }
        return result;
    }

    public int getValue() {
        return value;
    }

    public int getSign() {
        return sign;
    }

    public int getOctave() {
        return octave;
    }
}
