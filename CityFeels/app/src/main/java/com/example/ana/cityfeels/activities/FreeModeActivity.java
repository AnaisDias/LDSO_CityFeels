package com.example.ana.cityfeels.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

	private static final String EXTRA_ROUTE = "com.ldso.cityfeels.ROUTE";
	private CityFeels application;
	private Item<Location, String> spinner_inicio;
	private Item<Location, String> spinner_destino;
	private static final String NULL_PONTO_INTERESSE_ERROR = "Não foi possível obter o ponto de interesse";
	private static final String NULL_PERCURSO_ERROR = "Não foi possível obter o percurso";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Spinner iniciosSpinner = (Spinner) findViewById(R.id.inicios);
		Spinner destinosSpinner = (Spinner) findViewById(R.id.destinos);
		iniciosSpinner.setOnItemSelectedListener(onItemSelectedListener);
		destinosSpinner.setOnItemSelectedListener(onItemSelectedListener);

		populateSpinners(this);
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

	/**
	 * Called when the user clicks the Calcular button
	 */
	public void calcular(View view)
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private void populateSpinners(final Activity activity)
	{
		new AsyncTask<Void, Void, ArrayList<Item>>()
		{
			Spinner iniciosSpinner;

			@Override
			protected void onPreExecute()
			{
				iniciosSpinner = (Spinner) findViewById(R.id.inicios);
			}

			@Override
			protected ArrayList<Item> doInBackground(Void... params)
			{
				ArrayList<Item> iniciosItems = null;
				try
				{
					iniciosItems = SIA.getStartPoints();
				} catch(IOException e)
				{
					e.printStackTrace();
				} catch(JSONException e)
				{
					e.printStackTrace();
				}
				return iniciosItems;
			}

			@Override
			protected void onPostExecute(ArrayList<Item> list)
			{
				ArrayAdapter<Item> iniciosAdapter = new ArrayAdapter<>(activity,
																	   android.R.layout.simple_spinner_dropdown_item,
																	   list);
				iniciosSpinner.setAdapter(iniciosAdapter);
			}
		}.execute();

		new AsyncTask<Void, Void, ArrayList<Item>>()
		{
			Spinner destinosSpinner;

			@Override
			protected void onPreExecute()
			{
				destinosSpinner = (Spinner) findViewById(R.id.destinos);
			}

			@Override
			protected ArrayList<Item> doInBackground(Void... params)
			{
				ArrayList<Item> destinosItems = null;
				try
				{
					destinosItems = SIA.getDestinations();
				} catch(IOException e)
				{
					e.printStackTrace();
				} catch(JSONException e)
				{
					e.printStackTrace();
				}
				return destinosItems;
			}

			@Override
			protected void onPostExecute(ArrayList<Item> list)
			{
				ArrayAdapter<Item> destinosAdapter = new ArrayAdapter<>(activity,
																		android.R.layout.simple_spinner_dropdown_item,
																		list);
				destinosSpinner.setAdapter(destinosAdapter);
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
			if(spinner.getId() == R.id.inicios)
			{
				spinner_inicio = (Item<Location, String>) parent.getSelectedItem();
			}
			else if(spinner.getId() == R.id.destinos)
			{
				spinner_destino = (Item<Location, String>) parent.getSelectedItem();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parentView)
		{
			// your code here
		}
	};

}
