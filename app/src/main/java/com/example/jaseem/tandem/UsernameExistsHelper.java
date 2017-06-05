package com.example.jaseem.tandem;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Jaseem on 5/18/17.
 */

public class UsernameExistsHelper {

    DatabaseReference db;
    String username;
    static Boolean isUsernameAvailable;
    Boolean returnBool = true;
    Context context;

    public UsernameExistsHelper(Context context, DatabaseReference db, String username) {
        this.context = context;
        this.db = db;
        this.username = username;
    }

    public boolean checkUsernameAvailable() {

        //returnBool = isUsernameAvailable;

        return returnBool;
    }

    private boolean fetchUsernames(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChild(username)) {
            isUsernameAvailable = false;
        } else if (!dataSnapshot.hasChild(username)) {
            isUsernameAvailable = true;
        }

        return isUsernameAvailable;
    }

}
