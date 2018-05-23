package integration.google.sunder.updatesharedprefrencedatainservicewithactivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class SendPosService extends Service {
    private static final String TAG = "SendPosService";
    private LocationManager mLocationManager = null;
    private int BattLevel;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;


    private class LocationListener implements android.location.LocationListener {
        final Location mLastLocation;

        private LocationListener(String provider) {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            @SuppressLint("InlinedApi") BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
            @SuppressLint("InlinedApi") int batLevel = Objects.requireNonNull(bm).getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            Calendar current_date = Calendar.getInstance();
         //Write location update code
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private final LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_MULTI_PROCESS);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedpreferences.getInt("constant",0)==1){
                    Log.i("Service123", "Service123");
                }else{
                    Log.i("Service12345", "Service12345");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(new Intent(getApplicationContext(), SendPosService.class));
                    } else {
                        startService(new Intent(getApplicationContext(), SendPosService.class));
                    }
                }
            }
        }, 30000);
        return START_STICKY;
    }
    @Override
    public void onCreate() {
        initializeLocationManager();

        startForeground(1, new Notification());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeLocationManagerUpdate();
    }

    private void removeLocationManagerUpdate() {
        if (mLocationManager != null) {
            for (LocationListener mLocationListener : mLocationListeners) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        return;
                    mLocationManager.removeUpdates(mLocationListener);
                } catch (Exception ex) {
                    Log.i(TAG, "Failed to remove location listeners, ignore", ex);
                }
            }
        }
        mLocationManager = null;
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        int LOCATION_INTERVAL = 600000;
        try {
            Objects.requireNonNull(mLocationManager).requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, 0, mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, 0, mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
