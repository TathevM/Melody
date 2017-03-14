package com.team_red.melody.models;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.team_red.melody.R;
import com.team_red.melody.melodyboard.MelodyBoard;

import java.util.ArrayList;


public class MelodyAdapter extends RecyclerView.Adapter<MelodyAdapter.MelodyViewHolder> {

    private ArrayList<String> melodyStringList;
    private MelodyBoard mMelodyBoard;
//    private static final int VIEW_TYPE_LINES = 1;
//    private static final int VIEW_TYPE_BOTTOM = 2;

    public MelodyAdapter(ArrayList<String> melodyStringList, MelodyBoard melodyBoard) {
        this.melodyStringList = melodyStringList;
        this.mMelodyBoard = melodyBoard;

        this.melodyStringList = new ArrayList<>();//TODO called for test purposes
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

    public void addNewLinesToList(){
        melodyStringList.add("");
        melodyStringList.add("");
        melodyStringList.add("");
        melodyStringList.add("");
        melodyStringList.add("");
        notifyDataSetChanged();
    }

    @Override
    public MelodyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        RecyclerView.ViewHolder vh;
//        View v;
//        switch (viewType) {
//            case VIEW_TYPE_BOTTOM:
//                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_line , parent, false);
//                vh = new ButtonViewHolder(v);
//                break;
//            default:
//                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_composition_line, parent, false);
//                vh = new MelodyViewHolder(v);
//                break;
//        }
//        return vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_composition_line, parent, false);
        return new MelodyViewHolder(v);
    }

//    @Override
//    public int getItemViewType(int position) {
//        return (position == melodyStringList.size()) ? VIEW_TYPE_BOTTOM :VIEW_TYPE_LINES;
//    }

    @Override
    public void onBindViewHolder(final MelodyViewHolder holder,int position) {
//        if (holder instanceof MelodyViewHolder) {
            mMelodyBoard.registerEditText(holder.mEditText);
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
                        } else
                            mMelodyBoard.setClefType(181);
                    melodyStringList.set(holder.getAdapterPosition(), s.toString());
                }
            });
//        }
//        else {
//            ((ButtonViewHolder) holder).mButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    melodyStringList.add("");
//                    notifyDataSetChanged();
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return melodyStringList.size();
    }


    static class MelodyViewHolder extends RecyclerView.ViewHolder{

        MelodyEditText mEditText;

        MelodyViewHolder(View itemView) {
            super(itemView);
            mEditText = (MelodyEditText) itemView.findViewById(R.id.composition_line_edit_text);
            //mEditText.setTag(getAdapterPosition());
        }
    }
//    private static class ButtonViewHolder extends RecyclerView.ViewHolder{
//        Button mButton;
//        ButtonViewHolder(View itemView) {
//            super(itemView);
//            mButton = (Button) itemView.findViewById(R.id.add_line_button);
//        }
//    }
}
