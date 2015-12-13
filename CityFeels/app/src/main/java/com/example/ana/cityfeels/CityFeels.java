package com.example.ana.cityfeels;

import android.os.StrictMode;
import android.util.Log;

import com.example.ana.cityfeels.models.Percurso;
import com.example.ana.cityfeels.models.PontoInteresse;
import com.example.ana.cityfeels.modules.OrientationModule;
import com.example.ana.cityfeels.modules.TextToSpeechModule;

import java.io.IOException;
import java.util.Locale;

public class CityFeels extends android.app.Application implements EventDispatcher.OnNewLocationEventListener {

    private static final Locale DEFAULT_LOCALE = new Locale("pt", "PT");

    private DataSource.DataLayer currentDataLayer = DataSource.DataLayer.Basic;
    private Percurso currentPercurso = null;
    private PontoInteresse lastPontoInteresse = null;
    private String lastInstructions = null;
    private OrientationModule orientationModule;
    private TextToSpeechModule textToSpeechModule;

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.textToSpeechModule = new TextToSpeechModule(this, DEFAULT_LOCALE);
        this.textToSpeechModule.registerOnReady(new TextToSpeechModule.OnReadyListener() {
            @Override
            public void onReady() {
                EventDispatcher.registerOnNewLocation(CityFeels.this);
            }
        });

        /*
        this.orientationModule = new OrientationModule();

        orientationModule.OrientationInit((LocationManager) getSystemService(Context.LOCATION_SERVICE),
                (SensorManager) getSystemService(SENSOR_SERVICE));
        */
    }

    @Override
    public void onNewLocation(final Location location) {
        try {
            PontoInteresse pontoInteresse = DataSource.getPontoInteresse(location, this.currentDataLayer);

            Float azimuth = 0f;
            Instructions directions = new Instructions(pontoInteresse.getInformacao(), pontoInteresse.getOrientacao());

            this.lastInstructions = directions.applyOrientation(azimuth);
            textToSpeechModule.speak(this.lastInstructions);

            EventDispatcher.fireNewPontoInteresse(this.lastPontoInteresse = pontoInteresse);
            EventDispatcher.fireNewInstructions(this.lastInstructions);
        } catch (IOException e) {
            Log.e("NETWORK", e.getMessage());
        }
    }

    public TextToSpeechModule getTextToSpeechModule() {
        return this.textToSpeechModule;
    }

    public void setCurrentDataLayer(DataSource.DataLayer dataLayer) {
        this.currentDataLayer = dataLayer;
    }

    public DataSource.DataLayer getCurrentDataLayer() {
        return this.currentDataLayer;
    }

    public boolean isPercursoSelected() {
        return this.currentPercurso != null;
    }

    public Percurso getCurrentPercurso() {
        return this.currentPercurso;
    }

    public void setCurrentPercurso(Percurso percurso) {
        this.currentPercurso = percurso;
    }

    public PontoInteresse getLastPontoInteresse() { return this.lastPontoInteresse; }

    public String getLastInstructions() {
        return this.lastInstructions;
    }



}
