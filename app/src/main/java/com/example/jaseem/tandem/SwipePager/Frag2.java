package com.example.jaseem.tandem.SwipePager;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jaseem.tandem.FreeTimesAdapter;
import com.example.jaseem.tandem.FreeTimesClass;
import com.example.jaseem.tandem.FreeTimesHelper;
import com.example.jaseem.tandem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Jaseem on 5/8/17.
 */

public class Frag2 extends DialogFragment implements View.OnClickListener {

    Button bSetStartTime, bSetEndTime;
    int hour, minute;
    static final int setStartTime_id = 0, setEndTime_id = 1;
    TextView tvTest, tvTest2;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userUID = firebaseAuth.getCurrentUser().getUid();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference freeTimesRef = rootRef.child(userUID).child("FreeTimes");
    String pushID, timeString1, timeString2;
    int startHour, endHour;
    final FreeTimesClass timesDetails = new FreeTimesClass();
    FreeTimesHelper helper;
    FreeTimesAdapter adapter;
    ArrayAdapter<String> arrayAdapter;
    ListView lvFreeTimesList;
    ArrayList<String> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_freetimes, container, false);

        bSetStartTime = (Button) rootView.findViewById(R.id.bSetStartTime);
        bSetStartTime.setOnClickListener(this);
        bSetEndTime = (Button) rootView.findViewById(R.id.bSetEndTime);
        bSetEndTime.setOnClickListener(this);

        tvTest = (TextView) rootView.findViewById(R.id.tvTest);
        tvTest2 = (TextView) rootView.findViewById(R.id.tvTest2);

        return rootView;
    }

    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case setStartTime_id:
                return new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int setHour, int setMinute) {
                        String timeSet;
                        pushID = freeTimesRef.push().getKey();

                        startHour = setHour;

                        if (setHour > 12) {
                            startHour -= 12;
                            timeSet = "PM";
                        } else if (setHour == 0) {
                            startHour += 12;
                            timeSet = "AM";
                        } else if (setHour == 12) {
                            timeSet = "PM";
                        } else {
                            timeSet = "AM";
                        }

                        timesDetails.setStartHour(startHour);
                        timesDetails.setStartMinute(setMinute);
                        freeTimesRef.child(pushID).setValue(timesDetails);

                        timeString1 = new StringBuilder().append(startHour).append(":")
                                .append(setMinute).append(" ").append(timeSet).toString();
                        timesDetails.setTimeString1(timeString1);
                        tvTest.setText(timesDetails.getTimeString1());

                    }
                }, hour, minute, false);

            case setEndTime_id:
                return new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int setHour, int setMinute) {
                        String timeSet;

                        endHour = setHour;

                        if (setHour > 12) {
                            endHour -= 12;
                            timeSet = "PM";
                        } else if (setHour == 0) {
                            endHour += 12;
                            timeSet = "AM";
                        } else if (setHour == 12) {
                            timeSet = "PM";
                        }
                        else {
                            timeSet = "AM";
                        }

                        timesDetails.setEndHour(endHour);
                        timesDetails.setEndMinute(setMinute);
                        timesDetails.setTimeString2(timeString2);
                        freeTimesRef.child(pushID).setValue(timesDetails);

                        timeString2 = new StringBuilder().append(endHour).append(":")
                                .append(setMinute).append(" ").append(timeSet).toString();
                        tvTest2.setText(timesDetails.getTimeString2());

                    }
                }, hour, minute, false);

        }

        return null;
    }


    @Override
    public void onClick(View v) {
        if (v == bSetStartTime) {
            Toast.makeText(getContext(), "TEST 1", Toast.LENGTH_SHORT).show();
            getActivity().showDialog(setStartTime_id);
        }

        if (v == bSetEndTime) {
            getActivity().showDialog(setEndTime_id);
        }

    }
}
