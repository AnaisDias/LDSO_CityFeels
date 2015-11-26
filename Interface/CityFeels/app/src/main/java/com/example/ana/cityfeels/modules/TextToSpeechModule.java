package com.example.ana.cityfeels.modules;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class TextToSpeechModule implements TextToSpeech.OnInitListener {

    private static final String TEXT_TO_SPEECH_INIT_ERROR = "Oops! Erro ao iniciar TextToSpeech!";

    private boolean isLoaded = false;
    private int status = -1;
    private Context context;
    private TextToSpeech textToSpeech;
    private Locale preferredLanguage;
    private List<OnReadyListener> readyListeners = new LinkedList<>();

    public TextToSpeechModule(Context context, Locale language) {
        this.context = context;
        this.preferredLanguage = language;

        this.textToSpeech = new TextToSpeech(this.context, this);
    }

    private void fireOnReady() {
        for(OnReadyListener listener : this.readyListeners)
            listener.onReady();
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            int result = this.textToSpeech.setLanguage(this.preferredLanguage);

            if (result == TextToSpeech.LANG_MISSING_DATA )
                Log.i("TextToSpeech", "Missing data for " + this.preferredLanguage);
            else if(result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Locale currentLanguage = this.textToSpeech.getLanguage();

                Log.e("TextToSpeech", this.preferredLanguage + " not supported. Using " + currentLanguage);
                Toast.makeText(this.context, this.preferredLanguage + " não é suportado! A usar " + currentLanguage, Toast.LENGTH_LONG).show();
            } else
                Log.i("TextToSpeech", "Successfully initiated TextToSpeech using the language " + this.preferredLanguage);
            /*
            Locale[] locales = Locale.getAvailableLocales();
            List<Locale> localeList = new ArrayList<>();
            for (Locale locale : locales) {
                int res = this.textToSpeech.isLanguageAvailable(locale);
                if (res == TextToSpeech.LANG_AVAILABLE) {
                    localeList.add(locale);
                }
            }
            */

        }
        else if (status == TextToSpeech.ERROR) {
            Log.e("TextToSpeech", "Could not initiate TextToSpeech");
            Toast.makeText(this.context, TEXT_TO_SPEECH_INIT_ERROR, Toast.LENGTH_SHORT).show();
        }

        this.status = status;
        this.isLoaded = true;
        this.fireOnReady();
    }

    public boolean isLoaded() {
        return this.isLoaded;
    }

    public int getStatus() {
        return this.status;
    }

    public void speak(String text) {
        this.textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    public void registerOnReady(OnReadyListener listener) {
        if(this.isLoaded())
            listener.onReady();
        else
            readyListeners.add(listener);
    }

    public interface OnReadyListener {
        void onReady();
    }

}
