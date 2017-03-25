package com.team_red.melody.StartActivityFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team_red.melody.DBs.DbManager;
import com.team_red.melody.MainActivity;
import com.team_red.melody.MelodyApplication;
import com.team_red.melody.R;
import com.team_red.melody.models.User;


public class LoginFragment extends Fragment {

    public static final String USER_ID_TAG = "user_id";
    public static final String COMP_ID_TAG = "comp_tag";

    private EditText loginInput;
    private DbManager dbManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container , false);
        dbManager = new DbManager(getContext());
        initButtonClick(view);
        return view;
    }


    void initButtonClick(View view){
        loginInput = (EditText) view.findViewById(R.id.login_input);
        Button startButton = (Button) view.findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = loginInput.getText().toString();
                if (!userName.equalsIgnoreCase("")){
                    long id = dbManager.insertUser(userName);
                    Intent mIntent = new Intent(getActivity(), MainActivity.class);
                    MelodyApplication.setLoggedInUser(new User(userName , (int) id));
                    startActivity(mIntent);
                }
                else
                    Toast.makeText(getContext(),"Write your name maestro", Toast.LENGTH_SHORT).show();
            }
        });

        loginInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void hideKeyboard(View view)
    {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
