package com.team_red.melody.melodyboard;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;

import com.team_red.melody.models.MelodyStatics;

import java.util.ArrayList;


class MelodyKeyboard extends Keyboard {

    private Key mSharpKey;
    private Key mDoubleSharpKey;
    private Key mFlatKey;
    private Key mDoubleFlatKey;
    private Key mNaturalKey;
    private Key mDoteKey;
    private Key mOctavePlusKey;
    private Key mOctaveMinusKey;

    private ArrayList<Key> mOctaveModifierKeys;

    private ArrayList<Key> mNoteModifierKeys;

    public ArrayList<Key> getNoteModifierKeys() {
        return mNoteModifierKeys;
    }

    private void setNoteModifierKeys() {
        mNoteModifierKeys.add(mSharpKey);
        mNoteModifierKeys.add(mDoubleSharpKey);
        mNoteModifierKeys.add(mFlatKey);
        mNoteModifierKeys.add(mDoubleFlatKey);
        mNoteModifierKeys.add(mNaturalKey);
        mNoteModifierKeys.add(mDoteKey);
        mOctaveModifierKeys.add(mOctavePlusKey);
        mOctaveModifierKeys.add(mOctaveMinusKey);
    }

    MelodyKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
        mNoteModifierKeys = new ArrayList<>();
        mOctaveModifierKeys = new ArrayList<>();
    }

    public MelodyKeyboard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }

    @Override
    protected Key createKeyFromXml(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
        Key key = new Key(res,parent,x,y,parser);
        if (key.codes[0] == MelodyStatics.CODE_SHARP_TOGGLE) {
            mSharpKey = key;
           // mNoteModifierKeys.add(mSharpKey);
        }
        if (key.codes[0] == MelodyStatics.CODE_DOUBLE_SHARP_TOGGLE) {
            mDoubleSharpKey = key;
          //  mNoteModifierKeys.add(mDoubleSharpKey);
        }
        if (key.codes[0] == MelodyStatics.CODE_FLAT_TOGGLE) {
            mFlatKey = key;
//            mNoteModifierKeys.add(mFlatKey);
        }
        if (key.codes[0] == MelodyStatics.CODE_DOUBLE_FLAT_TOGGLE) {
            mDoubleFlatKey = key;
           // mNoteModifierKeys.add(mDoubleFlatKey);
        }
        if (key.codes[0] == MelodyStatics.CODE_TOGGLE_NATURAL) {
            mNaturalKey = key;
           // mNoteModifierKeys.add(mNaturalKey);
        }
        if(key.codes[0] == MelodyStatics.CODE_DOT_TOGGLE){
            mDoteKey = key;
        }
        if(key.codes[0] == MelodyStatics.CODE_OCTAVE_PLUS){
            mOctavePlusKey = key;
        }
        if(key.codes[0] == MelodyStatics.CODE_OCTAVE_MINUS){
            mOctaveMinusKey = key;
        }
        return key;
    }

    void setToggled(int keyCode){
        if(mNoteModifierKeys.isEmpty())
            setNoteModifierKeys();
        for (Key k:mNoteModifierKeys) {
            //k.on = (k.codes[0] == keyCode);
            k.pressed = (k.codes[0] == keyCode);
        }
    }

    void setOctaveToggled(int keyCode){
        if (mOctaveModifierKeys.isEmpty())
            setNoteModifierKeys();
        for (Key k: mOctaveModifierKeys){
            k.pressed = (k.codes[0] == keyCode);
        }
    }

    static class NoteKey extends Keyboard.Key {

        public NoteKey(Row parent) {
            super(parent);
        }

        NoteKey(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
            super(res, parent, x, y, parser);
        }
    }
}
