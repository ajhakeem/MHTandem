package com.example.jaseem.tandem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Jaseem on 4/14/17.
 */

public class Signup extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference refRoot = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refUsernames = refRoot.child("Usernames");
    EditText etSignupName, etSignupEmail, etSignupPassword, etSignupUsername;
    Button bRegister;
    ProgressDialog progressDialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        etSignupName = (EditText) findViewById(R.id.etSignupName);
        etSignupEmail = (EditText) findViewById(R.id.etSignupEmail);
        etSignupPassword = (EditText) findViewById(R.id.etSignupPassword);
        etSignupUsername = (EditText) findViewById(R.id.etSignupUsername);
        bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

    }

    public void SignUp() {
        String name = etSignupName.getText().toString().trim();
        String email = etSignupEmail.getText().toString().trim();
        String password = etSignupPassword.getText().toString().trim();
        final String username = etSignupUsername.getText().toString().trim();

        if(TextUtils.isEmpty(name)) {
            //name is empty
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(email)) {
            //password is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(username)) {
            //username is empty
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        UsernameExistsHelper usernameExistsHelper = new UsernameExistsHelper(getApplicationContext(), refUsernames, username);

        if (usernameExistsHelper.checkUsernameAvailable() == true) {

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        String userUID = firebaseAuth.getCurrentUser().getUid();
                        refUsernames.child(username).setValue(userUID);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), Homepage.class));
                    }

                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else  {
            Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

    }

    @Override
    public void onBackPressed() {
        //
    }

    @Override
    public void onClick(View v) {
        if (v == bRegister) {
            SignUp();
        }
    }
}
