package com.example.ana.cityfeels.modules;

import android.app.Activity;
import android.app.FragmentManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import com.example.ana.cityfeels.dialogs.ActivateGpsDialogFragment;

/**
 * Created by David on 16/11/2015.
 */
public class GpsModule implements LocationListener
{
	private static final int TWO_MINUTES_IN_MS = 1000 * 60 * 2;

	private LocationManager locationManager;
	private Location currentBestLocation;

	public GpsModule(LocationManager locationManager) {
		this.locationManager = locationManager;
		this.currentBestLocation = null;
	}

	protected void resume() throws SecurityException {
		this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}

	protected void pause() throws SecurityException {
		// Remove the listeners you previously added
		this.locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		if(isBetterLocation(location))
		{
			this.currentBestLocation = location;
			String lat = String.format("%f", location.getLatitude());
			String lon = String.format("%f", location.getLongitude());
			String coord = lat + " " + lon;
			Log.d("GpsModule", "Got a new coordinate from " + location.getProvider());
		}
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

	public boolean isActivated() {
		return this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public Location getCurrentBestLocation() {
		return this.currentBestLocation;
	}

	public void askForActivation(Activity activity, FragmentManager fragment) {
		ActivateGpsDialogFragment dialog = new ActivateGpsDialogFragment();
		dialog.setActivity(activity);
		dialog.show(fragment, "Activate_GPS");
	}

	/**
	 * Determines whether one Location reading is better than the current Location fix
	 *
	 * @param location The new Location that you want to evaluate
	 */
	protected boolean isBetterLocation(Location location) {
		if(currentBestLocation == null)
		{
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES_IN_MS;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES_IN_MS;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if(isSignificantlyNewer)
		{
			return true;
			// If the new location is more than two minutes older, it must be worse
		}
		else if(isSignificantlyOlder)
		{
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if(isMoreAccurate)
		{
			return true;
		}
		else if(isNewer && !isLessAccurate)
		{
			return true;
		}
		else if(isNewer && !isSignificantlyLessAccurate && isFromSameProvider)
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks whether two providers are the same
	 */
	private boolean isSameProvider(String provider1, String provider2) {
		if(provider1 == null)
		{
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
}
