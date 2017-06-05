package com.example.jaseem.tandem;

import android.app.Application;

/**
 * Created by Jaseem on 5/10/17.
 */

public class GlobalAvailability extends Application {

    public GlobalAvailability() { }

    private boolean isAvailable;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
