package com.example.ana.cityfeels.modules;

import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;

/**
 * Created by David on 16/11/2015.
 */
public class OrientationModule implements SensorEventListener
{
    /*
     * time smoothing constant for low-pass filter
     * 0 ≤ alpha ≤ 1 ; a smaller value means more smoothing
     */
    static final float ALPHA = 0.15f;

    GpsModule gpsModule;

    Float azimuth = 0.0f;
    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;

    public int OrientationInit(LocationManager locationManager, SensorManager sensorManager)
    {
        gpsModule = new GpsModule();
        gpsModule.GpsInit(locationManager);
        mSensorManager = sensorManager;
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
            accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        else
            return 1;
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null)
            magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        else
            return 2;
        return 0;
    }

    float[] mGravity;
    float[] mGeomagnetic;
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(gpsModule.currentBestLocation != null) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = lowPass(event.values.clone(), mGravity);
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = lowPass(event.values.clone(), mGeomagnetic);
            if (mGravity != null && mGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    azimuth = orientation[0]; // orientation contains: azimuth, pitch and roll
                }
            }
            azimuth = (float) Math.toDegrees(azimuth);
            GeomagneticField geoField = new GeomagneticField(
                    (float) gpsModule.currentBestLocation.getLatitude(),
                    (float) gpsModule.currentBestLocation.getLongitude(),
                    (float) gpsModule.currentBestLocation.getAltitude(),
                    System.currentTimeMillis());
            azimuth += geoField.getDeclination();
            azimuth = (azimuth + 360) % 360;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void Resume() {
        gpsModule.Resume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void Pause() {
        gpsModule.Pause();
        mSensorManager.unregisterListener(this, accelerometer);
        mSensorManager.unregisterListener(this, magnetometer);
    }

    protected float[] lowPass( float[] newVals, float[] oldVals ) {
        if ( oldVals == null ) return newVals;

        for ( int i=0; i<newVals.length; i++ ) {
            oldVals[i] = oldVals[i] + ALPHA * (newVals[i] - oldVals[i]);
        }
        return oldVals;
    }

    public Float getAzimuth() {
        return azimuth;
    }
}
