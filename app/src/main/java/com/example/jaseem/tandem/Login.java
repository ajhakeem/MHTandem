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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jaseem on 4/14/17.
 */

public class Login extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Button bLogin, bToFacebookLogin, bToCreateAccount;
    static EditText etLoginEmail, etLoginPassword;
    ProgressDialog progressDialog;
    Boolean fieldEmail = false;
    Boolean fieldPassword = false;
    static Boolean emailIsValid = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);
        bToFacebookLogin = (Button) findViewById(R.id.bToFacebookLogin);
        bToFacebookLogin.setOnClickListener(this);
        bToCreateAccount = (Button) findViewById(R.id.bToCreateAccount);
        bToCreateAccount.setOnClickListener(this);
        etLoginEmail = (EditText) findViewById(R.id.etLoginEmail);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        progressDialog = new ProgressDialog(this);
    }

    public void SignIn() {
        checkFields();

        if ((fieldEmail == true) && (fieldPassword == true) && (emailIsValid == true)) {

            String email = etLoginEmail.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            progressDialog.setMessage("Signing in...");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Successfully signed in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Homepage.class));
                    }

                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Sign in error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else {
            checkFields();
        }

    }

    public void checkFields() {
        String email = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            fieldEmail = false;
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            fieldPassword = false;
            return;
        }

        checkEmail();

        if (emailIsValid == false) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        }

        else {
            fieldEmail = true;
            fieldPassword = true;
            return;
        }
    }

    public static Boolean checkEmail() {
        String emailExp = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence emailSeq = etLoginEmail.getText().toString().trim();

        Pattern pattern = Pattern.compile(emailExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailSeq);

        if (matcher.matches()) {
            emailIsValid = true;
        }

        return emailIsValid;
    }

    @Override
    public void onClick(View v) {
        if (v == bLogin) {
            SignIn();
        }

        if (v == bToFacebookLogin) {

        }

        if (v == bToCreateAccount) {
            startActivity(new Intent(getApplicationContext(), Signup.class));
        }
    }
}
