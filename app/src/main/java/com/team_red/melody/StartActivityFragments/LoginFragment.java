package com.team_red.melody.StartActivityFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team_red.melody.DBs.DbManager;
import com.team_red.melody.MainActivity;
import com.team_red.melody.R;


public class LoginFragment extends Fragment {

    Button startButton;
    EditText loginInput;
    DbManager dbManager;

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
        startButton = (Button) view.findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = loginInput.getText().toString();
                if (!userName.equalsIgnoreCase("")){
                    dbManager.insertUser(userName);
                    Intent mIntent = new Intent(getActivity(), MainActivity.class);
                    mIntent.putExtra("id", -1);
                    startActivity(mIntent);

                }
                else

                    Toast.makeText(getContext(),"Write your name maestro", Toast.LENGTH_SHORT).show();
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
}
