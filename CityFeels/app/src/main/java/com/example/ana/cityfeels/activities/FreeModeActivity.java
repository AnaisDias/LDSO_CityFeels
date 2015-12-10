package com.example.ana.cityfeels.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.Item;
import com.example.ana.cityfeels.Location;
import com.example.ana.cityfeels.R;
import com.example.ana.cityfeels.sia.SIA;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class FreeModeActivity extends AppCompatActivity /*implements OnMapReadyCallback*/
{
	private CityFeels application;
	private Item<Location, String> spinner_inicio;
	private Item<Location, String> spinner_destino;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_free_mode);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Spinner iniciosSpinner = (Spinner) findViewById(R.id.inicios);
		Spinner destinosSpinner = (Spinner) findViewById(R.id.destinos);
		iniciosSpinner.setEnabled(false);
		destinosSpinner.setEnabled(false);
		iniciosSpinner.setOnItemSelectedListener(onItemSelectedListener);
		destinosSpinner.setOnItemSelectedListener(onItemSelectedListener);

		populateSpinners();
	}

	/**
	 * Called when the user clicks the Calcular button
	 */
	public void calcular(View view)
	{
		Intent intent = new Intent(this, RouteActivity.class);
		startActivity(intent);
	}

	private void populateSpinners()
	{
		new AsyncTask<Void, Void, Void>()
		{
			ArrayList<Item> pontosInicio = null;
			ArrayList<Item> pontosFim = null;

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

				return null;
			}

			@Override
			protected void onPostExecute(Void results)
			{
				pontosInicio.add(0, new Item<Location, String>(null, "Nenhum"));
				pontosFim.add(0, new Item<Location, String>(null, "Nenhum"));

				ArrayAdapter<Item> iniciosAdapter = new ArrayAdapter<>(FreeModeActivity.this,
																	   android.R.layout.simple_spinner_dropdown_item,
																	   pontosInicio);

				ArrayAdapter<Item> finsAdapter = new ArrayAdapter<>(FreeModeActivity.this,
																	android.R.layout.simple_spinner_dropdown_item,
																	pontosFim);

				Spinner iniciosSpinner = (Spinner) findViewById(R.id.inicios);
				Spinner finsSpinner = (Spinner) findViewById(R.id.destinos);

				iniciosSpinner.setAdapter(iniciosAdapter);
				finsSpinner.setAdapter(finsAdapter);
				iniciosSpinner.setEnabled(true);
				finsSpinner.setEnabled(true);
			}
		}.execute();

	}

	public AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener()
	{
		@Override
		public void onItemSelected(AdapterView<?> parent, View selectedItemView,
								   int position, long id)
		{
			Spinner spinner = (Spinner) parent;
			Item<Location, String> selectedItem = (Item<Location, String>)spinner.getSelectedItem();

			if(spinner.getId() == R.id.inicios)
			{
				spinner_inicio = selectedItem;
			}
			else if(spinner.getId() == R.id.destinos)
			{
				spinner_destino = selectedItem;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parentView)
		{
			// your code here
		}
	};

}
