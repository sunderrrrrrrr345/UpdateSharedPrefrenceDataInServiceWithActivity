package integration.google.sunder.updatesharedprefrencedatainservicewithactivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_MULTI_PROCESS);
        editor = sharedpreferences.edit();
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
                    return;
                }
                //"Service is not running"
              //  if ("Service is not running") {
                        editor.putInt("constant", 0);
                        editor.commit();
                        Intent locationStreamServiceIntent = new Intent(getApplicationContext(), SendPosService.class);
                        getApplicationContext().startService(locationStreamServiceIntent);

                  //  }

            } else {
                //"Service is not running"
                //  if ("Service is not running") {

                        editor.putInt("constant", 0);
                        editor.commit();
                        Intent locationStreamServiceIntent = new Intent(getApplicationContext(), SendPosService.class);
                        getApplicationContext().startService(locationStreamServiceIntent);

                  //  }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
