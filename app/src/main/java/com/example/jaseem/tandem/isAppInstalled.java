package com.example.jaseem.tandem;

import android.app.Application;

/**
 * Created by Jaseem on 5/16/17.
 */

public class isAppInstalled extends Application {

    public isAppInstalled() { }

    private boolean isInstalled = false;

    public boolean isInstalled() { return isInstalled; }

    public void setInstalled(boolean installed) { isInstalled = installed; }
}
