package com.example.ana.cityfeels.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.DataSource;
import com.example.ana.cityfeels.Direction;
import com.example.ana.cityfeels.Item;
import com.example.ana.cityfeels.Location;
import com.example.ana.cityfeels.LocationEventDispatcher;
import com.example.ana.cityfeels.LocationEventListener;
import com.example.ana.cityfeels.R;
import com.example.ana.cityfeels.models.PontoInteresse;
import com.example.ana.cityfeels.models.Percurso;
import com.example.ana.cityfeels.modules.TextToSpeechModule;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationEventListener {

    private static final String NULL_PONTO_INTERESSE_ERROR = "Não foi possível obter o ponto de interesse";
    private static final String NULL_PERCURSO_ERROR = "Não foi possível obter o percurso";

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
        TEST_LOCATIONS = new Location[6];
        TEST_LOCATIONS[0] = new Location(41.1654249, -8.6082677);
        TEST_LOCATIONS[1] = new Location(41.1654034, -8.6085272);
        TEST_LOCATIONS[2] = new Location(41.1778791, -8.6001047);
        TEST_LOCATIONS[3] = new Location(41.1776727, -8.5969448);
        TEST_LOCATIONS[4] = new Location(41.1777009, -8.595009);
        TEST_LOCATIONS[5] = new Location(41.1776968, -8.5944819);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.application = (CityFeels) getApplication();
        this.textToSpeech = this.application.getTextToSpeechModule();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setLayerButtonsClickListeners();
        setGenerateLocationButtonListeners();
        setRepeatInstructionsButtonListener();
        populateSpinners();
        setSpinnerOnItemSelectListeners();

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

                Spinner pontos = (Spinner) findViewById(R.id.pontos);
                Item<Location, String> item = (Item<Location, String>) pontos.getSelectedItem();

                LocationEventDispatcher.fireNewLocation(item.getValue());
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
                textToSpeech.repeat();
            }
        });
    }

    private void populateSpinners() {
        Spinner percursosSpinner = (Spinner) findViewById(R.id.percursos);

        ArrayList<Item> percursosItems = new ArrayList<>();
        percursosItems.add(new Item<Integer, String>(-1, "Nenhum"));
        percursosItems.add(new Item<Integer, String>(3, "Bar-Parque"));

        ArrayAdapter<Item> percursosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, percursosItems);
        percursosSpinner.setAdapter(percursosAdapter);

        Spinner pontosSpinner = (Spinner) findViewById(R.id.pontos);

        ArrayList<Item> pontosItems = new ArrayList<>();
        pontosItems.add(new Item<Location, String>(new Location(41.1654249, -8.6082677), "Residência Jayme Rios de Sousa"));
        pontosItems.add(new Item<Location, String>(new Location(41.1654034, -8.6085272), "Escola Secundária"));
        pontosItems.add(new Item<Location, String>(new Location(41.1778791, -8.6001047), "Faculdade de Engenharia"));
        pontosItems.add(new Item<Location, String>(new Location(41.1776727, -8.5969448), "Bar de estudantes"));
        pontosItems.add(new Item<Location, String>(new Location(41.1777009, -8.595009), "Escadaria"));
        pontosItems.add(new Item<Location, String>(new Location(41.1776968, -8.5944819), "Parque de estacionamento"));

        ArrayAdapter<Item> pontosAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_spinner_dropdown_item, pontosItems);
        pontosSpinner.setAdapter(pontosAdapter);
    }

    private void setSpinnerOnItemSelectListeners() {
        Spinner percursos = (Spinner) findViewById(R.id.percursos);
        percursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, int position, long id) {
                Item<Integer, String> item = (Item) parent.getItemAtPosition(position);
                final int percursoId = item.getValue();

                if(percursoId == -1)
                    application.setCurrentPercurso(null);
                else
                {
                    new AsyncTask<Void, Void, Percurso>() {

                        @Override
                        public void onPreExecute() {
                            parent.setEnabled(false);
                        }

                        @Override
                        protected Percurso doInBackground(Void... params) {
                            Percurso percurso = null;
                            try {
                                percurso = DataSource.getPercurso(percursoId);
                            } catch (IOException e) {
                                Log.e("NETWORK", e.getMessage());
                            }

                            return percurso;
                        }

                        @Override
                        protected void onPostExecute(Percurso percurso) {
                            if(percurso == null)
                                Toast.makeText(MainActivity.this, NULL_PERCURSO_ERROR, Toast.LENGTH_LONG).show();
                            else
                                application.setCurrentPercurso(percurso);

                            parent.setEnabled(true);
                        }
                    }.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                findViewById(R.id.generate_location_button).setEnabled(false);
            }

            @Override
            protected String doInBackground(Void... params) {
                StringBuilder stringBuilder = new StringBuilder();

                if (location == null)
                    Log.e("LOCATION", "Received a NULL location");
                else {
                    try {
                        PontoInteresse pontoInteresse = DataSource.getPontoInteresse(location, application.getCurrentDataLayer());
                        if (pontoInteresse == null)
                            return null;

                        stringBuilder.append(pontoInteresse.getInformacao());

                        if(application.isRouteSelected())
                        {
                            Percurso currentPercurso = application.getCurrentPercurso();
                            Direction directions = DataSource.getPercursoDirections(currentPercurso.getId(), pontoInteresse.getId());

                            stringBuilder.append(directions.applyOrientation(0));
                        }
                    } catch (IOException e) {
                        Log.e("NETWORK", e.getMessage());
                    }
                }

                return stringBuilder.toString();
            }

            @Override
            protected void onPostExecute(String text) {
                if (text == null)
                    Toast.makeText(MainActivity.this, NULL_PONTO_INTERESSE_ERROR, Toast.LENGTH_LONG).show();
                else {
                    /*
                    int index = (int) (((pontoInteresse.getOrientacao() - orientationModule.getAzimuth() + 360) % 360) / 90);
                    String text = pontoInteresse.getInformation().replace("[ori]", INFO_ORIENTATION[index]);
                    */
                    textToSpeech.speak(text);
                }

                findViewById(R.id.generate_location_button).setEnabled(true);
            }

        }.execute();
    }

}
