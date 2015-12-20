package com.example.ana.cityfeels.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.BroadcastManager;
import com.example.ana.cityfeels.Item;
import com.example.ana.cityfeels.Location;
import com.example.ana.cityfeels.R;
import com.example.ana.cityfeels.modules.OrientationModule;
import com.example.ana.cityfeels.sia.SIA;

import java.util.ArrayList;


public class FreeModeActivity extends AppCompatActivity {

    private CityFeels application;
    private Item<Location, String> spinner_inicio = null;
    private Item<Location, String> spinner_destino = null;
    private ArrayList<Item<Location, String>> pontosInicio = new ArrayList<>();
    private ArrayList<Item<Location, String>> pontosFim = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_free_mode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.application = (CityFeels) getApplication();

        initiateSpinners();
        initiateButtons();

        BroadcastManager broadcastManager = this.application.getBroadcastManager();

        broadcastManager.registerOnNewInstructions(this, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TextView view = (TextView) findViewById(R.id.modoLivreDirecoes);
                view.setText(application.getLastInstructions());
            }
        });

        broadcastManager.registerOnConnectivityActive(this, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Button calcularButton = (Button) findViewById(R.id.calcularButton);
                calcularButton.setEnabled(true);
            }
        });

        broadcastManager.registerOnConnectivityInactive(this, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Button calcularButton = (Button) findViewById(R.id.calcularButton);
                calcularButton.setEnabled(false);
            }
        });

        OrientationModule module = this.application.getOrientationModule();
        if (!module.isActivated())
            module.askForActivation(this, getFragmentManager());
    }

    private void initiateButtons() {
        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FreeModeActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });

        Button button = (Button) findViewById(R.id.calcularButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FreeModeActivity.this, RouteActivity.class);
                String info = spinner_inicio.getValue().latitude + "," + spinner_inicio.getValue().longitude;
                info += ";" + spinner_destino.getValue().latitude + "," + spinner_destino.getValue().longitude;
                intent.putExtra("info", info);
                startActivity(intent);
            }
        });

        NetworkInfo info = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if(info != null && info.isConnected())
            button.setEnabled(true);
        else
            button.setEnabled(false);
    }

    private void initiateSpinners() {
        final AutoCompleteTextView iniciosTextView = (AutoCompleteTextView) findViewById(R.id.inicios);
        iniciosTextView.setEnabled(true);
        iniciosTextView.setOnItemClickListener(onItemClickListenerInicio);
        final AutoCompleteTextView destinosTextView = (AutoCompleteTextView) findViewById(R.id.destinos);
        destinosTextView.setEnabled(true);
        destinosTextView.setOnItemClickListener(onItemClickListenerDestino);
        iniciosTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                iniciosTextView.showDropDown();
            }
        });
        destinosTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                destinosTextView.showDropDown();
            }
        });

        populateSpinners();
    }

    private void populateSpinners() {
        pontosInicio = new ArrayList<>();
        pontosFim = new ArrayList<>();
        new AsyncTask<Void, Void, Void>() {


            @Override
            protected Void doInBackground(Void... params) {
                try {
                    pontosInicio = SIA.getStartPoints();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    pontosFim = SIA.getDestinations();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void results) {
                pontosInicio.add(0, new Item<Location, String>(null, "Nenhum"));
                pontosFim.add(0, new Item<Location, String>(null, "Nenhum"));

                ArrayAdapter<Item<Location, String>> iniciosAdapter = new ArrayAdapter<>(FreeModeActivity.this,
                        android.R.layout.simple_dropdown_item_1line, pontosInicio);

                ArrayAdapter<Item<Location, String>> finsAdapter = new ArrayAdapter<>(FreeModeActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, pontosFim);

                AutoCompleteTextView iniciosTextView = (AutoCompleteTextView) findViewById(R.id.inicios);
                AutoCompleteTextView destinosTextView = (AutoCompleteTextView) findViewById(R.id.destinos);

                iniciosTextView.setAdapter(iniciosAdapter);
                iniciosTextView.setEnabled(true);
                destinosTextView.setAdapter(finsAdapter);
                destinosTextView.setEnabled(true);
            }
        }.execute();

    }

    public AdapterView.OnItemClickListener onItemClickListenerInicio = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View selectedItemView, int position, long id) {
            spinner_inicio = (Item<Location, String>) parent.getItemAtPosition(position);
            Log.d("ItemClick", spinner_inicio.getKey());
            Log.d("ItemClick", spinner_inicio.getValue().toString());
        }
    };

    public AdapterView.OnItemClickListener onItemClickListenerDestino = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View selectedItemView, int position, long id) {
            spinner_destino = (Item<Location, String>) parent.getItemAtPosition(position);
            Log.d("ItemClick", spinner_destino.getKey());
            Log.d("ItemClick", spinner_destino.getValue().toString());
        }
    };


}
