package com.team_red.melody.models;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.team_red.melody.R;
import com.team_red.melody.melodyboard.MelodyBoard;

import java.util.ArrayList;


public class MelodyAdapter extends RecyclerView.Adapter<MelodyAdapter.MelodyViewHolder> {

    private ArrayList<String> melodyStringList;
    private MelodyBoard mMelodyBoard;

    public MelodyAdapter(ArrayList<String> melodyStringList, MelodyBoard melodyBoard) {
        this.melodyStringList = melodyStringList;
        this.mMelodyBoard = melodyBoard;

        this.melodyStringList = new ArrayList<>();//TODO called for test purposes
        this.melodyStringList.add("");
        this.melodyStringList.add("");
        this.melodyStringList.add("");
        this.melodyStringList.add("");
    }

    public ArrayList<String> getMelodyStringList() {
        return melodyStringList;
    }

    public void setMelodyStringList(ArrayList<String> melodyStringList) {
        this.melodyStringList = melodyStringList;
    }

    @Override
    public MelodyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_composition_line , parent , false);
        return new MelodyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MelodyViewHolder holder, final int position) {
        holder.mEditText.setText(melodyStringList.get(position));
        holder.mEditText.addTextChangedListener(new TextWatcher() {
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
                    }
                    else
                        mMelodyBoard.setClefType(181);
                melodyStringList.set(position, s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return melodyStringList.size();
    }

    class MelodyViewHolder extends RecyclerView.ViewHolder{

        EditText mEditText;

        MelodyViewHolder(View itemView) {
            super(itemView);
            mEditText = (EditText) itemView.findViewById(R.id.composition_line_edit_text);
            //mEditText.setTag(getAdapterPosition());
            mMelodyBoard.registerEditText(mEditText);
        }
    }
}
