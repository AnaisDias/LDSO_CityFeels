package com.example.ana.cityfeels.modules;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

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

	private GpsModule gpsModule;
	private Float azimuth = 0.0f;
	private SensorManager mSensorManager;
	private Sensor accelerometer;
	private Sensor magnetometer;

	public int OrientationInit(LocationManager locationManager, SensorManager sensorManager)
	{
		this.gpsModule = new GpsModule(locationManager);
		this.mSensorManager = sensorManager;
		if(this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
			this.accelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		else
			return 1;
		if(this.mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null)
			this.magnetometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		else
			return 2;

		this.resume();
		return 0;
	}

	float[] mGravity;
	float[] mGeomagnetic;

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		if(this.gpsModule.getCurrentBestLocation() != null)
		{
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
				this.mGravity = lowPass(event.values.clone(), mGravity);
			if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
				this.mGeomagnetic = lowPass(event.values.clone(), this.mGeomagnetic);
			if(this.mGravity != null && this.mGeomagnetic != null)
			{
				float R[] = new float[9];
				float I[] = new float[9];
				boolean success = SensorManager.getRotationMatrix(R, I, this.mGravity, this.mGeomagnetic);
				if(success)
				{
					float orientation[] = new float[3];
					SensorManager.getOrientation(R, orientation);
					this.azimuth = orientation[0]; // orientation contains: azimuth, pitch and roll
				}
			}
			this.azimuth = (float) Math.toDegrees(this.azimuth);
			GeomagneticField geoField = new GeomagneticField(
					(float) this.gpsModule.getCurrentBestLocation().getLatitude(),
					(float) this.gpsModule.getCurrentBestLocation().getLongitude(),
					(float) this.gpsModule.getCurrentBestLocation().getAltitude(),
					System.currentTimeMillis());
			this.azimuth += geoField.getDeclination();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{

	}

	public void resume()
	{
		this.gpsModule.resume();
		this.mSensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_UI);
		this.mSensorManager.registerListener(this, this.magnetometer, SensorManager.SENSOR_DELAY_UI);
	}

	public void pause()
	{
		this.gpsModule.pause();
		this.mSensorManager.unregisterListener(this, this.accelerometer);
		this.mSensorManager.unregisterListener(this, this.magnetometer);
	}

	public boolean isActivated() {
		return this.gpsModule.isActivated();
	}

	public void askForActivation(Activity activity, FragmentManager fragmentManager) {
		this.gpsModule.askForActivation(activity, fragmentManager);
	}

	protected float[] lowPass(float[] newVals, float[] oldVals)
	{
		if(oldVals == null) return newVals;

		for(int i = 0; i < newVals.length; i++)
		{
			oldVals[i] = oldVals[i] + ALPHA * (newVals[i] - oldVals[i]);
		}
		return oldVals;
	}

	public Float getAzimuth()
	{
		if(this.gpsModule.getCurrentBestLocation() == null)
			return null;
		else
			return this.azimuth;
	}
}
