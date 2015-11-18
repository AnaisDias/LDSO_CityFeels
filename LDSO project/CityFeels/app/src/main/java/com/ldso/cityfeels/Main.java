package com.ldso.cityfeels;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Main extends AppCompatActivity {

    TextView coords;
    TextView orientation;
    //GpsModule gpsModule;
    OrientationModule orientationModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //gpsModule = new GpsModule();
        orientationModule = new OrientationModule();
        setContentView(R.layout.activity_gpslistner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coords = (TextView) findViewById(R.id.coords);
        orientation = (TextView) findViewById(R.id.orientation);


        try {
            //Usar GPS sozinho ou Orientation(GPS + Bussola)
            //gpsModule.GpsInit((LocationManager) getSystemService(Context.LOCATION_SERVICE), coords);

            int returnVal = orientationModule.OrientationInit((LocationManager) getSystemService(Context.LOCATION_SERVICE), coords,
                    (SensorManager) getSystemService(SENSOR_SERVICE), orientation);
            if(returnVal == 0)
               orientation.setText("Getting orientation...");
            else if(returnVal == 1)
                orientation.setText("No acelerometer sensor available");
            else if(returnVal == 2)
                orientation.setText("No magnetic fields sensor available");
            coords.setText("Getting coords...");
        }
        catch(SecurityException e) {
            coords.setText("No authorisation provided.\nPlease enable GPS location and restart the application.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gpslistner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        orientationModule.Resume();
        //gpsModule.Resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationModule.Pause();
        //gpsModule.Pause();
    }
}
