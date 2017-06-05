package com.example.jaseem.tandem;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jaseem on 4/25/17.
 */

public class FreeTimes extends AppCompatActivity implements View.OnClickListener {

    GlobalAvailability isAvailable = new GlobalAvailability();

    Button bSetStartTime, bSetEndTime;
    int hour, minute;
    static final int setStartTime_id = 0, setEndTime_id = 1;
    TextView tvSetTime1, tvSetTime2, tvCurrentTime;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String userUID = firebaseAuth.getCurrentUser().getUid();
    DatabaseReference refRoot = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refFreeTimes = refRoot.child(userUID).child("FreeTimes");
    DatabaseReference refUserDetails = refRoot.child(userUID).child("UserDetails");
    String pushID, timeString1, timeString2;
    int startHour, endHour;
    final FreeTimesClass timesDetails = new FreeTimesClass();
    FreeTimesHelper helper;
    FreeTimesAdapter listAdapter;
    ListView lvFreeTimesList;

    private int dataCheckInterval = 5000;
    private int statusCheckInterval = 30000;
    private Handler handlerStatusCheck, handlerDataCheck;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freetimes);

        bSetStartTime = (Button) findViewById(R.id.bSetStartTime);
        bSetStartTime.setOnClickListener(this);
        bSetEndTime = (Button) findViewById(R.id.bSetEndTime);
        bSetEndTime.setOnClickListener(this);

        tvSetTime1 = (TextView) findViewById(R.id.tvTest);
        tvSetTime2 = (TextView) findViewById(R.id.tvTest2);
        tvCurrentTime = (TextView) findViewById(R.id.tvCurrentTime);
        lvFreeTimesList = (ListView) findViewById(R.id.lvFreeTimesList);

        helper = new FreeTimesHelper(refFreeTimes);
        listAdapter = new FreeTimesAdapter(getApplicationContext(), helper.retrieve());
        lvFreeTimesList.setAdapter(listAdapter);

        handlerStatusCheck = new Handler();
        startStatusCheck();
        handlerDataCheck = new Handler();
        startDataCheck();

        //isAvailable = ((GlobalAvailability)getApplicationContext());
    }

    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case setStartTime_id:
                return new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int setHour, int setMinute) {
                        String timeSet;
                        String displayMinute = "";
                        pushID = refFreeTimes.push().getKey();

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

                        if (setMinute < 10) {
                            timeString1 = new StringBuilder().append(startHour).append(":").append("0")
                                    .append(setMinute).append(" ").append(timeSet).toString();
                        }

                        else if (setMinute == 0) {
                            timeString1 = new StringBuilder().append(startHour).append(":").append("00")
                                    .append(setMinute).append(" ").append(timeSet).toString();
                        }

                        else if (setMinute > 10) {
                            timeString1 = new StringBuilder().append(startHour).append(":")
                                    .append(setMinute).append(" ").append(timeSet).toString();
                        }

                        timesDetails.setStartHour(setHour);
                        timesDetails.setStartMinute(setMinute);
                        timesDetails.setTimeString1(timeString1);
                        tvSetTime1.setText(timesDetails.getTimeString1());
                        refFreeTimes.child(pushID).setValue(timesDetails);

                    }
                }, hour, minute, false);

            case setEndTime_id:
                return new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
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

                        if (setMinute < 10) {
                            timeString2 = new StringBuilder().append(endHour).append(":").append("0")
                                    .append(setMinute).append(" ").append(timeSet).toString();
                        }

                        else if (setMinute == 0) {
                            timeString2 = new StringBuilder().append(endHour).append(":").append("00")
                                    .append(setMinute).append(" ").append(timeSet).toString();
                        }

                        else if (setMinute > 10) {
                            timeString2 = new StringBuilder().append(endHour).append(":")
                                    .append(setMinute).append(" ").append(timeSet).toString();
                        }

                        Calendar compareStart = Calendar.getInstance();
                        compareStart.set(Calendar.HOUR_OF_DAY, timesDetails.getStartHour());
                        compareStart.set(Calendar.MINUTE, timesDetails.getStartMinute());
                        Date compareStartDate = compareStart.getTime();

                        Calendar compareEnd = Calendar.getInstance();
                        compareEnd.set(Calendar.HOUR_OF_DAY, setHour);
                        compareEnd.set(Calendar.MINUTE, setMinute);
                        Date compareEndDate = compareEnd.getTime();

                        if (compareEndDate.before(compareStartDate)) {
                            Toast.makeText(getApplicationContext(), "Please select a time after your set start time", Toast.LENGTH_SHORT).show();
                        }

                        else if (compareEndDate.after(compareStartDate)) {
                            timesDetails.setEndHour(setHour);
                            timesDetails.setEndMinute(setMinute);
                            timesDetails.setTimeString2(timeString2);
                            tvSetTime2.setText(timesDetails.getTimeString2());
                            refFreeTimes.child(pushID).setValue(timesDetails);
                        }

                    }
                }, hour, minute, false);

        }

        return null;
    }


    @Override
    public void onClick(View v) {
        if (v == bSetStartTime) {
            showDialog(setStartTime_id);
        }

        if (v == bSetEndTime) {
            showDialog(setEndTime_id);
        }

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
                        //refUserDetails.child("IsAvailable").setValue(true);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return false;

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

    Runnable dataChecker = new Runnable() {
        @Override
        public void run() {
            try  {
                notifyList();
            }

            finally {
                handlerDataCheck.postDelayed(dataChecker, dataCheckInterval);
            }
        }
    };

    public void startStatusCheck() {
        statusChecker.run();
    }

    public void stopStatusCheck() {
        handlerStatusCheck.removeCallbacks(statusChecker);
    }

    public void startDataCheck() { dataChecker.run();  }

    public void stopDataCheck() { handlerDataCheck.removeCallbacks(dataChecker); }

    public void notifyList() {
        listAdapter.notifyDataSetChanged();
    }

}



