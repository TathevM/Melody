package com.team_red.melody.models;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Note implements Parcelable {
    public static final String NOTE_VALUE_JSON_TAG = "value";
    public static final String NOTE_SIGN_JSON_TAG = "sign";
    public static final String NOTE_OCTAVE_JSON_TAG = "octave";

    private String value;
    private String sign;
    private int octave;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(value);
        out.writeString(sign);
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
        value = in.readString();
        sign = in.readString();
        octave = in.readInt();
    }

    public JSONObject getJSONObject(){
        JSONObject object = new JSONObject();
        try {
            object.put(NOTE_VALUE_JSON_TAG , value);
            if(!sign.isEmpty())
                object.put(NOTE_SIGN_JSON_TAG , sign);
            else
                object.put(NOTE_SIGN_JSON_TAG , JSONObject.NULL);
            object.put(NOTE_OCTAVE_JSON_TAG , octave);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return object;
    }
}
