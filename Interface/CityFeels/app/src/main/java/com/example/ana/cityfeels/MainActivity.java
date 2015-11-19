package com.example.ana.cityfeels;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.LocationManager;
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

import com.example.ana.cityfeels.navigation.OrientationModule;
import com.example.ana.cityfeels.sia.PontoInteresse;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, LocationEventListener {

    private final static int TEXT_TO_SPEECH_CHECK_CODE = 0;
    private static Location[] TEST_LOCATIONS;
    private static String[] INFO_ORIENTATION;
    private OrientationModule orientationModule;

    static {
        INFO_ORIENTATION = new String[4];
        INFO_ORIENTATION[0] = "À sua frente ";
        INFO_ORIENTATION[1] = "À sua direita ";
        INFO_ORIENTATION[2] = "Atrás de si ";
        INFO_ORIENTATION[3] = "À sua esquerda ";
        TEST_LOCATIONS = new Location[3];
        TEST_LOCATIONS[0] = new Location(41.1654249, -8.6082677);
        TEST_LOCATIONS[1] = new Location(41.1654034, -8.6085272);
        TEST_LOCATIONS[2] = new Location(41.1778791, -8.6001047);
    }

    private DataSource.DataLayer currentInformationLayer = DataSource.DataLayer.Basic;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orientationModule = new OrientationModule();

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

        orientationModule.OrientationInit((LocationManager) getSystemService(Context.LOCATION_SERVICE),
                (SensorManager) getSystemService(SENSOR_SERVICE));
    }

    private void setGenerateLocationButtonListeners() {
        Button generateLocationButton = (Button) findViewById(R.id.generate_location_button);
        generateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int index = random.nextInt(TEST_LOCATIONS.length);

                LocationEventDispatcher.fireNewLocation(TEST_LOCATIONS[index]);
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
                setInformationLayer(DataSource.DataLayer.Basic);
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
                setInformationLayer(DataSource.DataLayer.Local);
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
                setInformationLayer(DataSource.DataLayer.Detailed);
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
                setInformationLayer(DataSource.DataLayer.Another);
                return true;
            }
        });
    }

    private void setRepeatInstructionsButtonListener() {
        ImageButton repeatInstructionsButton = (ImageButton) findViewById(R.id.imageButton);
        repeatInstructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IPontoInteresse poi = DataSource.getLastPointOfInterest();
                if (poi != null)
                    textToSpeech.speak(poi.getInformation(), TextToSpeech.QUEUE_ADD, null);
            }
        });
    }

    private void setInformationLayer(DataSource.DataLayer layer) {
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
    protected void onResume() {
        super.onResume();
        orientationModule.Resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationModule.Pause();
    }

    @Override
    public void onNewLocation(Location location) {
        Toast.makeText(this, location.toString(), Toast.LENGTH_LONG).show();
        IPontoInteresse pontoInteresse = null;
        try {
            pontoInteresse = DataSource.getPointOfInterest(location, this.currentInformationLayer);
            if(pontoInteresse != null)
            {
                int index = (int) (((pontoInteresse.getOrientation() - orientationModule.getAzimuth() + 360) % 360) / 90);
                String text = pontoInteresse.getInformation().replace("[ori]", INFO_ORIENTATION[index]);
                this.textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao conectar!", Toast.LENGTH_LONG).show();
            System.out.println(e.getMessage());
        }
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
