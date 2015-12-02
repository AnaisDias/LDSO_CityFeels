package com.example.ana.cityfeels;

import android.os.StrictMode;

import com.example.ana.cityfeels.models.Percurso;
import com.example.ana.cityfeels.modules.OrientationModule;
import com.example.ana.cityfeels.modules.TextToSpeechModule;

import java.util.Locale;

public class CityFeels extends android.app.Application {

    private static final Locale DEFAULT_LOCALE = new Locale("pt", "PT");

    private DataSource.DataLayer currentDataLayer = DataSource.DataLayer.Basic;
    private Percurso currentPercurso = null;
    private OrientationModule orientationModule;
    private TextToSpeechModule textToSpeechModule;

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.textToSpeechModule = new TextToSpeechModule(this, DEFAULT_LOCALE);

        /*
        this.orientationModule = new OrientationModule();

        orientationModule.OrientationInit((LocationManager) getSystemService(Context.LOCATION_SERVICE),
                (SensorManager) getSystemService(SENSOR_SERVICE));
        */
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

    public boolean isRouteSelected() {
        return this.currentPercurso != null;
    }

    public Percurso getCurrentPercurso() {
        return this.currentPercurso;
    }

    public void setCurrentPercurso(Percurso percurso) {
        this.currentPercurso = percurso;
    }
}
