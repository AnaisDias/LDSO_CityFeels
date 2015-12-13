package com.example.ana.cityfeels.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.EventDispatcher;
import com.example.ana.cityfeels.Item;
import com.example.ana.cityfeels.Location;
import com.example.ana.cityfeels.R;
import com.example.ana.cityfeels.sia.SIA;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    private CityFeels application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.application = (CityFeels) getApplication();

        Spinner iniciosSpinner = (Spinner) findViewById(R.id.pontosIniciaisSpinner);
        Spinner finsSpinner = (Spinner) findViewById(R.id.pontosFinaisSpinner);
        Spinner pontosSpinner = (Spinner) findViewById(R.id.pontoCorrenteSpinner);
        Button gerarButton = (Button)findViewById(R.id.generateButton);

        iniciosSpinner.setEnabled(false);
        finsSpinner.setEnabled(false);
        pontosSpinner.setEnabled(false);

        populateSpinners();
        setSpinnersListeners();
        setButtonListeners();
    }

    private void setButtonListeners() {
        Button gerarButton = (Button)findViewById(R.id.generateButton);
        final Spinner pontosSpinner = (Spinner) findViewById(R.id.pontoCorrenteSpinner);

        gerarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item<Location, String> currentPonto = (Item)pontosSpinner.getSelectedItem();
                EventDispatcher.fireNewLocation(currentPonto.getValue());
            }
        });
    }

    private void setSpinnersListeners() {
        Spinner iniciosSpinner = (Spinner) findViewById(R.id.pontosIniciaisSpinner);
        Spinner finsSpinner = (Spinner) findViewById(R.id.pontosFinaisSpinner);

    }

    private void populateSpinners() {
        new AsyncTask<Void, Void, Void>()
        {
            ArrayList<Item> pontosInicio = null;
            ArrayList<Item> pontosFim = null;
            ArrayList<Item> pontos = new ArrayList<Item>();

            @Override
            protected Void doInBackground(Void... params)
            {
                try {
                    pontosInicio = SIA.getStartPoints();
                } catch(Exception e)
                {
                    e.printStackTrace();
                }

                try {
                    pontosFim = SIA.getDestinations();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                try {
                    for(com.example.ana.cityfeels.sia.PontoInteresse pontoInteresse : SIA.getPontosInteresse()) {
                        pontos.add(new Item<Location, String>(pontoInteresse.posicao, pontoInteresse.nome));
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void results)
            {
                Item<Location, String> nullEntry = new Item<Location, String>(null, "Nenhum");

                pontosInicio.add(0, nullEntry);
                pontosFim.add(0, nullEntry);
                pontos.add(0, nullEntry);

                ArrayAdapter<Item> iniciosAdapter = new ArrayAdapter<>(TestActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        pontosInicio);

                ArrayAdapter<Item> finsAdapter = new ArrayAdapter<>(TestActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        pontosFim);

                ArrayAdapter<Item> pontosAdapter = new ArrayAdapter<>(TestActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        pontos);

                Spinner iniciosSpinner = (Spinner) findViewById(R.id.pontosIniciaisSpinner);
                Spinner finsSpinner = (Spinner) findViewById(R.id.pontosFinaisSpinner);
                Spinner pontosSpinner = (Spinner) findViewById(R.id.pontoCorrenteSpinner);

                iniciosSpinner.setAdapter(iniciosAdapter);
                finsSpinner.setAdapter(finsAdapter);
                pontosSpinner.setAdapter(pontosAdapter);
                iniciosSpinner.setEnabled(true);
                finsSpinner.setEnabled(true);
                pontosSpinner.setEnabled(true);
            }
        }.execute();

    }

}
