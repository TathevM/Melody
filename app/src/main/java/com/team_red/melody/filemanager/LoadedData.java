package com.team_red.melody.filemanager;

import com.team_red.melody.models.Note;

import java.util.ArrayList;

import static com.team_red.melody.melodyboard.MelodyStatics.SHEET_TYPE_ONE_HANDED;
import static com.team_red.melody.melodyboard.MelodyStatics.SHEET_TYPE_TWO_HANDED;


public class LoadedData {

    private ArrayList<Note> comp1;
    private ArrayList<Note> comp2;

    LoadedData() {
    }

    public int getType(){
        return comp2 == null ? SHEET_TYPE_ONE_HANDED : SHEET_TYPE_TWO_HANDED;
    }

    public ArrayList<Note> getComp1() {
        return comp1;
    }

    void setComp1(ArrayList<Note> comp1) {
        this.comp1 = comp1;
    }

    public ArrayList<Note> getComp2() {
        return comp2;
    }

    void setComp2(ArrayList<Note> comp2) {
        this.comp2 = comp2;
    }
}
