package com.team_red.melody.melodyboard;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;


class MelodyKeyboard extends Keyboard {

    public MelodyKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public MelodyKeyboard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }

    @Override
    protected Key createKeyFromXml(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
        return new NoteKey(res,parent,x,y,parser);
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
