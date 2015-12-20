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

        final Spinner pontosSpinner = (Spinner) findViewById(R.id.pontoCorrenteSpinner);
        pontosSpinner.setEnabled(false);

        populateSpinners();

        Button gerarButton = (Button)findViewById(R.id.generateButton);
        gerarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item<Location, String> currentPonto = (Item) pontosSpinner.getSelectedItem();
                application.setLocation(currentPonto.getValue());
            }
        });

    }

    private void populateSpinners() {
        new AsyncTask<Void, Void, Void>()
        {
            ArrayList<Item> pontos = new ArrayList<Item>();

            @Override
            protected Void doInBackground(Void... params)
            {
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
                pontos.add(0, new Item<Location, String>(null, "Nenhum"));

                ArrayAdapter<Item> pontosAdapter = new ArrayAdapter<>(TestActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        pontos);

                Spinner pontosSpinner = (Spinner) findViewById(R.id.pontoCorrenteSpinner);

                pontosSpinner.setAdapter(pontosAdapter);
                pontosSpinner.setEnabled(true);
            }
        }.execute();

    }

}
