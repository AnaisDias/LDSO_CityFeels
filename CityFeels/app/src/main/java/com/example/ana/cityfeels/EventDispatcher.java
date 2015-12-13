package com.example.ana.cityfeels;

import android.os.AsyncTask;

import com.example.ana.cityfeels.models.PontoInteresse;

import java.util.HashSet;

public class EventDispatcher {

    private static HashSet<OnNewLocationEventListener> LOCATION_LISTENERS = new HashSet<>();
    private static HashSet<OnNewPontoInteresseEventListener> PONTO_INTERESSE_LISTENERS = new HashSet<>();
    private static HashSet<OnNewInstructionsEventListener> INSTRUCTIONS_LISTENER = new HashSet<>();

    private EventDispatcher() {}

    public static boolean registerOnNewLocation(OnNewLocationEventListener listener) {
        return !LOCATION_LISTENERS.contains(listener) && LOCATION_LISTENERS.add(listener);
    }

    public static void fireNewLocation(final Location location) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                for(OnNewLocationEventListener listener : LOCATION_LISTENERS)
                    listener.onNewLocation(location);

                return null;
            }
        }.execute();
    }

    public static void removeOnNewLocation(OnNewLocationEventListener listener) {
        LOCATION_LISTENERS.remove(listener);
    }

    public static boolean registerOnNewPontoInteresse(OnNewPontoInteresseEventListener listener) {
        return !PONTO_INTERESSE_LISTENERS.contains(listener) && PONTO_INTERESSE_LISTENERS.add(listener);
    }

    public static void fireNewPontoInteresse(final PontoInteresse pontoInteresse) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for(OnNewPontoInteresseEventListener listener : PONTO_INTERESSE_LISTENERS)
                    listener.onNewPontoInteresse(pontoInteresse);

                return null;
            }
        }.execute();
    }

    public static void removeOnNewPontoInteresse(OnNewPontoInteresseEventListener listener) {
        PONTO_INTERESSE_LISTENERS.remove(listener);
    }

    public static boolean registerOnNewInstructions(OnNewInstructionsEventListener listener) {
        return !INSTRUCTIONS_LISTENER.contains(listener) && INSTRUCTIONS_LISTENER.add(listener);
    }

    public static void fireNewInstructions(final String instructions) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for(OnNewInstructionsEventListener listener : INSTRUCTIONS_LISTENER)
                    listener.onNewInstructions(instructions);

                return null;
            }
        }.execute();
    }

    public static void removeOnNewInstructions(OnNewInstructionsEventListener listener) {
        INSTRUCTIONS_LISTENER.remove(listener);
    }

    public interface OnNewLocationEventListener {
        void onNewLocation(Location location);
    }

    public interface OnNewPontoInteresseEventListener {
        void onNewPontoInteresse(PontoInteresse pontoInteresse);
    }

    public interface OnNewInstructionsEventListener {
        void onNewInstructions(String instructions);
    }

}
