package com.example.jaseem.tandem.SwipePager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jaseem.tandem.MainActivity;
import com.example.jaseem.tandem.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Jaseem on 5/8/17.
 */

public class FreeTimesFragment extends Fragment implements View.OnClickListener{

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Button bSignout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_homepage, container, false);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bSignout = (Button) rootView.findViewById(R.id.bSignout);
        bSignout.setOnClickListener(this);

        return rootView;
    }

    public void SignOut() {
        firebaseAuth.signOut();
    }

    public void onClick(View v) {
        if (v == bSignout) {
            SignOut();
            startActivity(new Intent(getContext(), MainActivity.class));
        }


    }


}
