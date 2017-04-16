package com.team_red.melody.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team_red.melody.R;
import com.team_red.melody.melodyboard.MelodyBoard;
import com.team_red.melody.widget.MelodyEditText;

import java.util.ArrayList;

import static com.team_red.melody.models.MelodyStatics.CODE_BASS_CLEF;
import static com.team_red.melody.models.MelodyStatics.CODE_SOL_CLEF;
import static com.team_red.melody.models.MelodyStatics.SHEET_TYPE_ONE_HANDED;
import static com.team_red.melody.models.MelodyStatics.SHEET_TYPE_TWO_HANDED;


public class MelodyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> melodyStringList1;
    private ArrayList<String> melodyStringList2;

    private MelodyBoard mMelodyBoard;
    private static final int VIEW_TYPE_ONE_LINE = 1;
    private static final int VIEW_TYPE_TWO_LINE = 2;

    private static final String EDIT_TEXT_1_TAG = "1";
    private static final String EDIT_TEXT_2_TAG = "2";

    public MelodyAdapter(MelodyBoard melodyBoard) {
        this.mMelodyBoard = melodyBoard;
    }

    public ArrayList<String> getMelodyStringList1() {
        return melodyStringList1;
    }

    public void setMelodyStringList1(ArrayList<String> melodyStringList1) {
        this.melodyStringList1 = melodyStringList1;
        for (int i = 0; i < 10; i++)
            this.melodyStringList1.add("");
    }

    public ArrayList<String> getMelodyStringList2() {
        return melodyStringList2;
    }

    public void setMelodyStringList2(ArrayList<String> melodyStringList2) {
        this.melodyStringList2 = melodyStringList2;
        for (int i = 0; i < 10; i++)
            this.melodyStringList2.add("");
    }

    public void addNewLinesToList() {
        if(melodyStringList2 == null && melodyStringList1.get(getItemCount() -1).isEmpty())
            return;
        else if(melodyStringList2 != null){
            if(melodyStringList1.get(getItemCount() - 1).isEmpty() && melodyStringList2.get(getItemCount() - 1).isEmpty())
                return;
        }

        if (melodyStringList1 != null) {
            for (int i = 0; i < 10; i++)
                melodyStringList1.add("");
            if (melodyStringList2 != null) {
                for (int i = 0; i < 10; i++)
                    melodyStringList2.add("");
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v;
        switch (viewType) {
            case VIEW_TYPE_ONE_LINE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_composition_line , parent, false);
                vh = new MelodyOneLineViewHolder(v);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_composition_two_lined, parent, false);
                vh = new MelodyTwoLineViewHolder(v);
                break;
        }
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        return melodyStringList2 == null ? VIEW_TYPE_ONE_LINE : VIEW_TYPE_TWO_LINE;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_ONE_LINE) {
            mMelodyBoard.registerEditText(((MelodyOneLineViewHolder) holder).mEditText);
            ((MelodyOneLineViewHolder) holder).mEditText.setText(melodyStringList1.get(position));
            setTextChange(((MelodyOneLineViewHolder) holder).mEditText , holder);
        }
        else {
            mMelodyBoard.registerEditText(((MelodyTwoLineViewHolder) holder).mEditText1);
            mMelodyBoard.registerEditText(((MelodyTwoLineViewHolder) holder).mEditText2);
            ((MelodyTwoLineViewHolder) holder).mEditText1.setText(melodyStringList1.get(position));
            ((MelodyTwoLineViewHolder) holder).mEditText2.setText(melodyStringList2.get(position));
            setTextChange(((MelodyTwoLineViewHolder) holder).mEditText1 , holder);
            setTextChange(((MelodyTwoLineViewHolder) holder).mEditText2 , holder);
        }
    }

    private void setTextChange(MelodyEditText editText , final RecyclerView.ViewHolder holder){
        if (editText.getTag().equals(EDIT_TEXT_1_TAG))
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (s.charAt(0) != (char) CODE_BASS_CLEF) {
                        if (s.charAt(0) != (char) CODE_SOL_CLEF) {
                            s.insert(0, String.valueOf((char) CODE_SOL_CLEF));
                            mMelodyBoard.setClefType(CODE_SOL_CLEF);
                        }
                    } else
                        mMelodyBoard.setClefType(CODE_BASS_CLEF);
                    melodyStringList1.set(holder.getAdapterPosition(), s.toString());
                }
            }
        });
        else
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        if (s.charAt(0) != (char) CODE_BASS_CLEF) {
                            if (s.charAt(0) != (char) CODE_SOL_CLEF) {
                                s.insert(0, String.valueOf((char) CODE_SOL_CLEF));
                                mMelodyBoard.setClefType(CODE_SOL_CLEF);
                            }
                        } else
                            mMelodyBoard.setClefType(CODE_BASS_CLEF);
                        melodyStringList2.set(holder.getAdapterPosition(), s.toString());
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return melodyStringList2 == null ? (melodyStringList1 == null ? 0 : melodyStringList1.size()) : Math.max(melodyStringList1.size() , melodyStringList2.size());
    }

    public int getCompositionType(){
        return melodyStringList2 == null ? SHEET_TYPE_ONE_HANDED : SHEET_TYPE_TWO_HANDED;
    }


    private static class MelodyOneLineViewHolder extends RecyclerView.ViewHolder{

        MelodyEditText mEditText;

        MelodyOneLineViewHolder(View itemView) {
            super(itemView);
            mEditText = (MelodyEditText) itemView.findViewById(R.id.composition_line_edit_text);
            mEditText.setTag(EDIT_TEXT_1_TAG);
        }
    }

    private static class MelodyTwoLineViewHolder extends RecyclerView.ViewHolder{

        MelodyEditText mEditText1;
        MelodyEditText mEditText2;

        MelodyTwoLineViewHolder(View itemView) {
            super(itemView);
            mEditText1 = (MelodyEditText) itemView.findViewById(R.id.composition_twoline_edit_text1);
            mEditText1.setTag(EDIT_TEXT_1_TAG);
            mEditText2 = (MelodyEditText) itemView.findViewById(R.id.composition_twoline_edit_text2);
            mEditText2.setTag(EDIT_TEXT_2_TAG);
        }
    }
}
