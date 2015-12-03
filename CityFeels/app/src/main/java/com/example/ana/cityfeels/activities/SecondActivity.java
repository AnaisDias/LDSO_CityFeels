package com.example.ana.cityfeels.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.DataSource;
import com.example.ana.cityfeels.Item;
import com.example.ana.cityfeels.Location;
import com.example.ana.cityfeels.R;
import com.example.ana.cityfeels.models.Percurso;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity /*implements OnMapReadyCallback*/{

    private CityFeels application;
    private Object spinner_inicio;
    private Object spinner_destino;
    private static final String NULL_PONTO_INTERESSE_ERROR = "Não foi possível obter o ponto de interesse";
    private static final String NULL_PERCURSO_ERROR = "Não foi possível obter o percurso";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

        populateSpinners();
        /*MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
    }

   /* @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(41.1654034, -8.6085272))
                .title("Marker"));
    }*/

    /** Called when the user clicks the Calcular button */
    public void calcular(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void populateSpinners() {
        Spinner percursosSpinner = (Spinner) findViewById(R.id.percursos);

        ArrayList<Item> percursosItems = new ArrayList<>();
        percursosItems.add(new Item<Integer, String>(-1, "Inicio"));
        percursosItems.add(new Item<Integer, String>(3, "Bar-Parque"));

        ArrayAdapter<Item> percursosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, percursosItems);
        percursosSpinner.setAdapter(percursosAdapter);

        Spinner pontosSpinner = (Spinner) findViewById(R.id.pontos);

        ArrayList<Item> pontosItems = new ArrayList<>();
        pontosItems.add(new Item<Location, String>(new Location(41.1654249, -8.6082677), "Destino"));
        pontosItems.add(new Item<Location, String>(new Location(41.1654034, -8.6085272), "Escola Secundária"));
        pontosItems.add(new Item<Location, String>(new Location(41.1778791, -8.6001047), "Faculdade de Engenharia"));
        pontosItems.add(new Item<Location, String>(new Location(41.1776727, -8.5969448), "Bar de estudantes"));
        pontosItems.add(new Item<Location, String>(new Location(41.1777009, -8.595009), "Escadaria"));
        pontosItems.add(new Item<Location, String>(new Location(41.1776968, -8.5944819), "Parque de estacionamento"));

        ArrayAdapter<Item> pontosAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_spinner_dropdown_item, pontosItems);
        pontosSpinner.setAdapter(pontosAdapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.percursos)
        {
            spinner_inicio= parent.getItemAtPosition(pos);
        }
        else if(spinner.getId() == R.id.pontos)
        {
            spinner_destino= parent.getItemAtPosition(pos);
        }

    }

}
