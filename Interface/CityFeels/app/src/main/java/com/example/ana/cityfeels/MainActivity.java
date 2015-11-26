package com.example.ana.cityfeels;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ana.cityfeels.modules.TextToSpeechModule;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements LocationEventListener {

    private static final String NULL_PONTO_INTERESSE_ERROR = "Não foi possível obter o ponto de interesse";

    private static Location[] TEST_LOCATIONS;
    private static String[] INFO_ORIENTATION;

    private CityFeels application;
    private TextToSpeechModule textToSpeech;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.application = (CityFeels)getApplication();
        this.textToSpeech = this.application.getTextToSpeechModule();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setLayerButtonsClickListeners();
        setGenerateLocationButtonListeners();
        setRepeatInstructionsButtonListener();

        View basicLayerButton = findViewById(R.id.button1);
        basicLayerButton.setPressed(true);

        final View generateLocationButton = findViewById(R.id.generate_location_button);
        generateLocationButton.setEnabled(false);

        this.textToSpeech.registerOnReady(new TextToSpeechModule.OnReadyListener() {
            @Override
            public void onReady() {
                generateLocationButton.setEnabled(true);
                LocationEventDispatcher.registerOnNewLocation(MainActivity.this);
            }
        });
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
                application.setCurrentDataLayer(DataSource.DataLayer.Basic);
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
                application.setCurrentDataLayer(DataSource.DataLayer.Local);
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
                application.setCurrentDataLayer(DataSource.DataLayer.Detailed);
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
                application.setCurrentDataLayer(DataSource.DataLayer.Another);
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
                if (poi != null) {
                    /*
                    int index = (int) (((poi.getOrientation() - orientationModule.getAzimuth() + 360) % 360) / 90);
                    String text = poi.getInformation().replace("[ori]", INFO_ORIENTATION[index]);
                    */
                    String text = poi.getInformation();
                    textToSpeech.speak(text);
                }
            }
        });
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

    /*
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
    */

    @Override
    public void onNewLocation(final Location location) {
        new AsyncTask<Void, Void, IPontoInteresse>() {

            @Override
            protected void onPreExecute() {
                findViewById(R.id.generate_location_button).setEnabled(false);
            }

            @Override
            protected IPontoInteresse doInBackground(Void... params) {
                IPontoInteresse pontoInteresse = null;

                if(location == null)
                    Log.e("LOCATION", "Received a NULL location");
                else {
                    try {
                        pontoInteresse = DataSource.getPointOfInterest(location, application.getCurrentDataLayer());
                    } catch (IOException e) {
                        Log.e("NETWORK", e.getMessage());
                    }
                }

                return pontoInteresse;
            }

            @Override
            protected void onPostExecute(IPontoInteresse pontoInteresse) {
                if(pontoInteresse == null)
                    Toast.makeText(MainActivity.this, NULL_PONTO_INTERESSE_ERROR, Toast.LENGTH_LONG).show();
                else
                {
                    /*
                    int index = (int) (((pontoInteresse.getOrientation() - orientationModule.getAzimuth() + 360) % 360) / 90);
                    String text = pontoInteresse.getInformation().replace("[ori]", INFO_ORIENTATION[index]);
                    */
                    String text = pontoInteresse.getInformation();
                    textToSpeech.speak(text);
                }

                findViewById(R.id.generate_location_button).setEnabled(true);
            }

        }.execute();
    }
}
