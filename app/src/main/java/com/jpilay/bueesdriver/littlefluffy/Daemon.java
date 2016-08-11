package com.jpilay.bueesdriver.littlefluffy;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

import android.app.Application;
import android.util.Log;

public class Daemon extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        Log.d("BueesDriver", "Application onCreate()");

        // Output debug to LogCat with tag LittleFluffyLocationLibrary
        LocationLibrary.showDebugOutput(true);

        try {
            //Get Location each 5 seconds and force location each 5 seconds.
            LocationLibrary.initialiseLibrary(getBaseContext(), 3 * 1000, 5 * 1000, "com.uees.buees.MainActivity");
        }
        catch (UnsupportedOperationException ex) {
            Log.d("BueesDriver", "UnsupportedOperationException thrown - The device doesn't have any location providers");
        }
    }
}
