package com.example.jaseem.tandem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user = firebaseAuth.getCurrentUser();
    Button bTitleFacebookSU, bTitleEmailSU;
    TextView tvLogin;
    String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tvLogin = (TextView) findViewById(R.id.tvToLogin);
        tvLogin.setOnClickListener(this);
        bTitleFacebookSU = (Button) findViewById(R.id.bTitleFacebookSU);
        bTitleFacebookSU.setOnClickListener(this);
        bTitleEmailSU = (Button) findViewById(R.id.bTitleEmailSU);
        bTitleEmailSU.setOnClickListener(this);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    //User is signed in
                    Log.d(TAG, "onAuthStateChanged: signed in:" + user.getUid());
                    startActivity(new Intent(getApplicationContext(), Homepage.class));
                }

                else {
                    //User is signed out
                    Log.d(TAG, "onAuthStateChanged: signed out");
                    //Toast.makeText(getApplicationContext(), "Please sign in", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getApplicationContext(), Login.class));
                }
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvLogin) {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        if (v == bTitleFacebookSU) {
            Toast.makeText(getApplicationContext(), "Sign up Facebook", Toast.LENGTH_SHORT).show();
        }

        if (v == bTitleEmailSU) {
            startActivity(new Intent(getApplicationContext(), Signup.class));
        }
    }
}
