package com.example.jaseem.tandem;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jaseem on 5/10/17.
 */

public class Friends extends AppCompatActivity{

    Cursor cursor;
    boolean isAppInstalled;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        ContentResolver resolver = getContentResolver();
        cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        /*while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            Log.i("MY INFO", id + " = " + name);

            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);

            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            }
        }*/

        checkInstall();

    }

    public void checkInstall() {
        PackageManager packageManager = getApplicationContext().getPackageManager();
        isAppInstalled = isPackageInstalled("com.example.jaseem.tandem", packageManager);

        if (isAppInstalled == true) {
            Toast.makeText(getApplicationContext(), "App is installed", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(getApplicationContext(), "Negative", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPackageInstalled (String packageName, PackageManager packageManager) {

        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        }

        catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }


}
