package edu.mwsu.cs.se.mad.common;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class UserLocationManager {

	public static boolean locationUpdated = false;
	private LocationManager locationManager;
	private GPSUpdateListner gpsListener;
	private NetworkUpdateListner networkListener;
	private Location finalLocation;
	private static Location finalLocationShared;
	private boolean gpsEnabled;
	private boolean networkEnabled;

	public UserLocationManager() {
		
		gpsListener = new GPSUpdateListner();
		networkListener = new NetworkUpdateListner();
	}

	public Location queryUserCurrentLocation(Context context) {
		if (locationManager == null) {
			locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
		}
		// exceptions thrown if provider not enabled
		try {
			gpsEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			networkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gpsEnabled && !networkEnabled) {
			return null;

		}

		if (gpsEnabled) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
		}
		if (networkEnabled) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
		}
		getLastLocation();
		/***************************************************remove this after testing**************************************/
//		finalLocation=new Location(LocationManager.GPS_PROVIDER);
//		finalLocation.setLatitude(33.86585445407186);
//		finalLocation.setLongitude(-98.53569030761719);
		/***************************************************remove this after testing**************************************/
		setFinalLocationShared(finalLocation);
		return finalLocation;

	}

	class GPSUpdateListner implements LocationListener {

		@Override
		public void onLocationChanged(android.location.Location location) {

			consumeNewLocation(location);

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}
	}

	class NetworkUpdateListner implements LocationListener {

		@Override
		public void onLocationChanged(android.location.Location location) {

			consumeNewLocation(location);

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}
	}

	public void consumeNewLocation(android.location.Location location) {
		// assign new location
		locationUpdated = true;
		finalLocation = location;
		locationManager.removeUpdates(gpsListener);
		locationManager.removeUpdates(networkListener);
	}

	private void getLastLocation() {
		locationManager.removeUpdates(gpsListener);
		locationManager.removeUpdates(networkListener);

		Location gpsLocation = null;
		Location networkLocation = null;

		if (gpsEnabled) {
			gpsLocation = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		if (networkEnabled) {
			networkLocation = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		// if there are both values use the latest one
		if (gpsLocation != null && networkLocation != null) {
			if (gpsLocation.getTime() > networkLocation.getTime()) {
				finalLocation = gpsLocation;
			} else {
				finalLocation = networkLocation;
			}

			return;
		}

		if (gpsLocation != null) {
			finalLocation = gpsLocation;
			return;
		}

		if (networkLocation != null) {
			finalLocation = networkLocation;
			return;
		}

		finalLocation = null;
	}

	/**
	 * @param finalLocationShared the finalLocationShared to set
	 */
	public static void setFinalLocationShared(Location finalLocationShared) {
		UserLocationManager.finalLocationShared = finalLocationShared;
	}

	/**
	 * @return the finalLocationShared
	 */
	public static Location getFinalLocationShared() {
		return finalLocationShared;
	}

}
