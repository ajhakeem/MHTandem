package com.example.jaseem.tandem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Jaseem on 4/27/17.
 */

public class FreeTimesHelper {
    DatabaseReference db;
    ArrayList<FreeTimesClass> freeTimesDetails = new ArrayList<>();

    public FreeTimesHelper(DatabaseReference db) {
        this.db = db;
    }

    public ArrayList<FreeTimesClass> retrieve() {

       db.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               freeTimesDetails.clear();
               fetchData(dataSnapshot);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

        return freeTimesDetails;
    }

    private void fetchData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren())  {
            FreeTimesClass fetchedDetails = ds.getValue(FreeTimesClass.class);
            freeTimesDetails.add(fetchedDetails);
        }
    }

}
