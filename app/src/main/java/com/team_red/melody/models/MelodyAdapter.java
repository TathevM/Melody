package com.team_red.melody.models;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team_red.melody.R;
import com.team_red.melody.melodyboard.MelodyBoard;

import java.util.ArrayList;


public class MelodyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> melodyStringList1;
    private ArrayList<String> melodyStringList2;

    private MelodyBoard mMelodyBoard;
    private static final int VIEW_TYPE_ONE_LINE = 1;
    private static final int VIEW_TYPE_TWO_LINE = 2;

    public MelodyAdapter(MelodyBoard melodyBoard) {
        this.mMelodyBoard = melodyBoard;
    }

    public ArrayList<String> getMelodyStringList1() {
        return melodyStringList1;
    }

    public void setMelodyStringList1(ArrayList<String> melodyStringList1) {
        this.melodyStringList1 = melodyStringList1;
        this.melodyStringList1.add("");
    }

    public ArrayList<String> getMelodyStringList2() {
        return melodyStringList2;
    }

    public void setMelodyStringList2(ArrayList<String> melodyStringList2) {
        this.melodyStringList2 = melodyStringList2;
        this.melodyStringList2.add("");
    }

    public void addNewLinesToList(){
        melodyStringList1.add("");
        melodyStringList1.add("");
        melodyStringList1.add("");
        melodyStringList1.add("");
        if (melodyStringList2 != null){
            melodyStringList2.add("");
            melodyStringList2.add("");
            melodyStringList2.add("");
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
            ((MelodyTwoLineViewHolder) holder).mEditText2.setText(melodyStringList1.get(position));
            setTextChange(((MelodyTwoLineViewHolder) holder).mEditText1 , holder);
            setTextChange(((MelodyTwoLineViewHolder) holder).mEditText2 , holder);
        }
    }

    private void setTextChange(MelodyEditText editText ,final RecyclerView.ViewHolder holder){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                        if (s.charAt(0) != (char) 181) {
                            if (s.charAt(0) != (char) 180) {
                                s.insert(0, String.valueOf((char) 180));
                                mMelodyBoard.setClefType(180);
                            }
                        } else
                            mMelodyBoard.setClefType(181);
                    melodyStringList1.set(holder.getAdapterPosition(), s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return melodyStringList2 == null ? melodyStringList1.size() : Math.max(melodyStringList1.size() , melodyStringList2.size());
    }


    private static class MelodyOneLineViewHolder extends RecyclerView.ViewHolder{

        MelodyEditText mEditText;

        MelodyOneLineViewHolder(View itemView) {
            super(itemView);
            mEditText = (MelodyEditText) itemView.findViewById(R.id.composition_line_edit_text);
        }
    }

    private static class MelodyTwoLineViewHolder extends RecyclerView.ViewHolder{

        MelodyEditText mEditText1;
        MelodyEditText mEditText2;

        MelodyTwoLineViewHolder(View itemView) {
            super(itemView);
            mEditText1 = (MelodyEditText) itemView.findViewById(R.id.composition_twoline_edit_text1);
            mEditText2 = (MelodyEditText) itemView.findViewById(R.id.composition_twoline_edit_text2);
        }
    }
}
