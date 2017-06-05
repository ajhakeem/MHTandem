package com.example.jaseem.tandem;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class Homepage extends AppCompatActivity implements View.OnClickListener {

    private int statusCheckInterval = 30000;
    private Handler handlerStatusCheck = new Handler();
    private Thread checkThread;
    private FirebaseAuth.AuthStateListener authStateListener;
    String TAG = "";

    Button bSignout, bTime, bFriends;
    isAppInstalled isAppInstalled = new isAppInstalled();
    GlobalAvailability isAvailable = new GlobalAvailability();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    String userUID = firebaseAuth.getCurrentUser().getUid();
    DatabaseReference refRoot = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refFreeTimes = refRoot.child(userUID).child("FreeTimes");
    DatabaseReference refUserDetails = refRoot.child("usersInfo").child(userUID).child("UserDetails");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bSignout = (Button) findViewById(R.id.bSignout);
        bSignout.setOnClickListener(this);
        bTime = (Button) findViewById(R.id.bTime);
        bTime.setOnClickListener(this);
        bFriends = (Button) findViewById(R.id.bFriends);
        bFriends.setOnClickListener(this);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    //User is signed in
                    Log.d(TAG, "onAuthStateChanged: signed in:" + user.getUid());
                    checkInstall();
                    checkUserFreeTimes();
                }

                else {
                    //User is signed out
                    Log.d(TAG, "onAuthStateChanged: signed out");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    //Toast.makeText(getApplicationContext(), "Please sign in", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getApplicationContext(), Login.class));
                }
            }
        };

        checkThread = new Thread(statusChecker);
    }

    public void SignOut() {
        firebaseAuth.signOut();
    }

    @Override
    public void onClick(View v) {
        if (v == bSignout) {
            SignOut();
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        if (v == bTime) {
            startActivity(new Intent(getApplicationContext(), FreeTimes.class));
        }

        if (v == bFriends) {
            startActivity(new Intent(getApplicationContext(), Friends.class));
        }
    }


    public void checkInstall() {
        PackageManager packageManager = getApplicationContext().getPackageManager();
        isPackageInstalled("com.example.jaseem.tandem", packageManager);

        if (isAppInstalled.isInstalled() == true) {
            refUserDetails.child("IsInstalled").setValue(isAppInstalled.isInstalled());
        }

        else {
            refUserDetails.child("IsInstalled").setValue(isAppInstalled.isInstalled());
        }
    }

    private void isPackageInstalled (String packageName, PackageManager packageManager) {

        try {
            packageManager.getPackageInfo(packageName, 0);
            isAppInstalled.setInstalled(true);
        }

        catch (PackageManager.NameNotFoundException e) {
            isAppInstalled.setInstalled(false);
        }

    }

    public void checkUserFreeTimes() {
        refRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userUID).hasChild("FreeTimes")) {
                    checkThread.start();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean checkAvailability() {
        refFreeTimes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    FreeTimesClass dsValue = ds.getValue(FreeTimesClass.class);

                    Calendar startTimes = Calendar.getInstance();
                    startTimes.set(Calendar.HOUR_OF_DAY, dsValue.getStartHour());
                    startTimes.set(Calendar.MINUTE, dsValue.getStartMinute());
                    startTimes.clear(Calendar.SECOND);
                    Date dateStartTime = startTimes.getTime();

                    Calendar endTimes = Calendar.getInstance();
                    endTimes.set(Calendar.HOUR_OF_DAY, dsValue.getEndHour());
                    endTimes.set(Calendar.MINUTE, dsValue.getEndMinute());
                    endTimes.clear(Calendar.SECOND);
                    Date dateEndTime = endTimes.getTime();

                    Date currentTime = Calendar.getInstance().getTime();

                    if ((currentTime.after(dateStartTime)) && (currentTime.before(dateEndTime))) {
                        isAvailable.setAvailable(true);
                        refUserDetails.child("IsAvailable").setValue(isAvailable.isAvailable());
                    }

                    else {
                        isAvailable.setAvailable(false);
                        refUserDetails.child("IsAvailable").setValue(isAvailable.isAvailable());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return false;

    }

    @Override
    public void onBackPressed() {
        //
    }

    Runnable statusChecker = new Runnable() {
        @Override
        public void run() {
            try  {
                checkAvailability();
            }

            finally {
                handlerStatusCheck.postDelayed(statusChecker, statusCheckInterval);
            }
        }
    };

}
