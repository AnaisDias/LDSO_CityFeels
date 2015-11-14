package com.example.ana.cityfeels;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ana.cityfeels.sia.Etiqueta;
import com.example.ana.cityfeels.sia.Location;
import com.example.ana.cityfeels.sia.PointOfInterest;

import org.json.JSONException;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, LocationEventListener {

    private enum InformationLayer {
        Basic, Local, Detailed, Another
    }

    private final static int TEXT_TO_SPEECH_CHECK_CODE = 0;

    private InformationLayer currentInformationLayer = InformationLayer.Basic;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setLayerButtonsClickListeners();
        setGenerateLocationButtonListeners();
        setRepeatInstructionsButtonListener();
        LocationEventDispatcher.registerOnNewLocation(this);

        View basicLayerButton = findViewById(R.id.button1);
        basicLayerButton.setPressed(true);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, TEXT_TO_SPEECH_CHECK_CODE);
    }

    private void setGenerateLocationButtonListeners() {
        Button generateLocationButton = (Button) findViewById(R.id.generate_location_button);
        generateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationEventDispatcher.fireNewLocation(new Location(1, 2));
            }
        });
    }

    private void setLayerButtonsClickListeners() {
        final Button basicLayerButton = (Button) findViewById(R.id.button1);
        final Button localLayerButton = (Button) findViewById(R.id.button2);
        final Button detailedLayerButton = (Button) findViewById(R.id.button3);
        final Button anotherLayerButton = (Button) findViewById(R.id.button4);

        basicLayerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.setPressed(true);
                localLayerButton.setPressed(false);
                detailedLayerButton.setPressed(false);
                anotherLayerButton.setPressed(false);
                setInformationLayer(InformationLayer.Basic);
                return true;
            }
        });
        localLayerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setPressed(true);
                basicLayerButton.setPressed(false);
                detailedLayerButton.setPressed(false);
                anotherLayerButton.setPressed(false);
                setInformationLayer(InformationLayer.Local);
                return true;
            }
        });
        detailedLayerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setPressed(true);
                localLayerButton.setPressed(false);
                basicLayerButton.setPressed(false);
                anotherLayerButton.setPressed(false);
                setInformationLayer(InformationLayer.Detailed);
                return true;
            }
        });
        anotherLayerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setPressed(true);
                localLayerButton.setPressed(false);
                basicLayerButton.setPressed(false);
                detailedLayerButton.setPressed(false);
                setInformationLayer(InformationLayer.Another);
                return true;
            }
        });
    }

    private void setRepeatInstructionsButtonListener() {
        ImageButton repeatInstructionsButton = (ImageButton) findViewById(R.id.imageButton);
        repeatInstructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PointOfInterest poi = DataSource.getLastPointOfInterest();
                if(poi != null)
                    textToSpeech.speak(poi.about, TextToSpeech.QUEUE_ADD, null);
            }
        });
    }

    private void setInformationLayer(InformationLayer layer) {
        switch(layer) {
            case Basic: break;
            case Local: break;
            case Detailed: break;
            case Another: break;
        }

        this.currentInformationLayer = layer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up buttonprincipal, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewLocation(Location location) {
        Toast.makeText(this, location.toString(), Toast.LENGTH_LONG).show();
        PointOfInterest pointOfInterest = DataSource.getPointOfInterest(location, DataSource.DataLayer.Basic, null);
        this.textToSpeech.speak(pointOfInterest.about, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            this.textToSpeech.setLanguage(new Locale("pt", "BR"));

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
        else if (status == TextToSpeech.ERROR)
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_SHORT).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TEXT_TO_SPEECH_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                this.textToSpeech = new TextToSpeech(this, this);
            }
            else {
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

}
