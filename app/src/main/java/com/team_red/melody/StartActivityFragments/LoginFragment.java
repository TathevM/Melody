package com.team_red.melody.StartActivityFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.team_red.melody.DBs.DbManager;
import com.team_red.melody.R;
import com.team_red.melody.activities.CompositionsActivity;
import com.team_red.melody.app.MelodyApplication;
import com.team_red.melody.models.User;


public class LoginFragment extends Fragment {

    public static final String COMP_ID_TAG = "comp_tag";
    private boolean newUserState;

    private EditText loginInput;
    private DbManager dbManager;
    private ImageView startButton;

    public boolean isNewUserState() {
        return newUserState;
    }

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
        startButton = (ImageView) view.findViewById(R.id.addButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!newUserState) {
                    Animation addButtonAnimation = AnimationUtils.loadAnimation(getContext() , R.anim.add_button_click);
                    startButton.setAnimation(addButtonAnimation);
                    startButton.setImageResource(R.drawable.ic_arrow_forward);
                    Animation moveInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.move_right);
                    loginInput.startAnimation(moveInAnimation);
                    loginInput.setVisibility(View.VISIBLE);
                    newUserState = true;
                } else {
                    String userName = loginInput.getText().toString();
                    if (!userName.equalsIgnoreCase("")) {
                        long id = dbManager.insertUser(userName);
                        Intent mIntent = new Intent(getActivity(), CompositionsActivity.class);
                        MelodyApplication.setLoggedInUser(new User(userName, (int) id));
                        startActivity(mIntent);
                        newUserState = false;
                        getActivity().finish();
                    } else
                        Toast.makeText(getContext(), R.string.toast_no_name, Toast.LENGTH_SHORT).show();
                }
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

    public void handleBackPressed(){
        Animation moveAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.move_left);
        loginInput.setAnimation(moveAnimation);
        loginInput.setText("");
        loginInput.setVisibility(View.INVISIBLE);
        newUserState = false;
        Animation addButtonAnimation = AnimationUtils.loadAnimation(getContext() , R.anim.add_button_back);
        startButton.setAnimation(addButtonAnimation);
        startButton.setImageResource(R.drawable.add_button);
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
