package com.example.ana.cityfeels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.ana.cityfeels.models.Percurso;
import com.example.ana.cityfeels.models.PontoInteresse;
import com.example.ana.cityfeels.modules.OrientationModule;
import com.example.ana.cityfeels.modules.TextToSpeechModule;

import java.io.IOException;
import java.util.Locale;

public class CityFeels extends android.app.Application {

    private static final Locale DEFAULT_LOCALE = new Locale("pt", "PT");

    private DataSource.DataLayer currentDataLayer = DataSource.DataLayer.Basic;
    private Percurso currentPercurso = null;
    private PontoInteresse lastPontoInteresse = null;
    private String lastInstructions = null;
    private Location lastLocation = null;

    private OrientationModule orientationModule;
    private TextToSpeechModule textToSpeechModule;
    private BroadcastManager broadcastManager;
    private NetworkReceiver networkReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.broadcastManager = new BroadcastManager(this);
        this.networkReceiver = new NetworkReceiver();
        this.textToSpeechModule = new TextToSpeechModule(this, DEFAULT_LOCALE);
        this.orientationModule = new OrientationModule();

        this.orientationModule.OrientationInit(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                (SensorManager) getSystemService(SENSOR_SERVICE));

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(this.networkReceiver, intentFilter);
    }

    public void setLocation(final Location location) {
        if(location == null)
            return;

        this.lastLocation = location;
        try {
            this.lastPontoInteresse = DataSource.getPontoInteresse(location, this.currentDataLayer);

            Float azimuth = this.orientationModule.getAzimuth();
            Instructions directions = new Instructions(this.lastPontoInteresse.getInformacao(), this.lastPontoInteresse.getOrientacao());

            this.lastInstructions = directions.applyOrientation(azimuth);
            this.textToSpeechModule.speak(this.lastInstructions);

            this.broadcastManager.broadcastNewLocation();
            this.broadcastManager.broadcastNewPontoInteresse();
            this.broadcastManager.broadcastNewInstructions();
        } catch (IOException e) {
            Log.e("NETWORK", e.getMessage());
        }
    }

    public void setCurrentDataLayer(DataSource.DataLayer dataLayer) {
        this.currentDataLayer = dataLayer;
    }

    public void setCurrentPercurso(Percurso percurso) {
        this.currentPercurso = percurso;
    }

    public Location getLastLocation() {
        return this.lastLocation;
    }

    public DataSource.DataLayer getCurrentDataLayer() {
        return this.currentDataLayer;
    }

    public Percurso getCurrentPercurso() {
        return this.currentPercurso;
    }

    public String getLastInstructions() {
        return this.lastInstructions;
    }

    public PontoInteresse getLastPontoInteresse() { return this.lastPontoInteresse; }

    public TextToSpeechModule getTextToSpeechModule() {
        return this.textToSpeechModule;
    }

    public OrientationModule getOrientationModule() { return this.orientationModule; }

    public BroadcastManager getBroadcastManager() { return this.broadcastManager; }

    public boolean isPercursoSelected() {
        return this.currentPercurso != null;
    }

    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager conn =  (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conn.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected())
                broadcastManager.broadcastConnectivityActive();
            else
                broadcastManager.broadcastConnectivityInactive();
        }
    }

}
