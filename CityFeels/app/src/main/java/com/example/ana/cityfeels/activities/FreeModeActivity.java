package com.example.ana.cityfeels.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.DataSource;
import com.example.ana.cityfeels.EventDispatcher;
import com.example.ana.cityfeels.Item;
import com.example.ana.cityfeels.Location;
import com.example.ana.cityfeels.R;
import com.example.ana.cityfeels.sia.SIA;

import java.util.ArrayList;

public class FreeModeActivity extends AppCompatActivity implements EventDispatcher.OnNewInstructionsEventListener
{
	private CityFeels application;
	private Item<Location, String> spinner_inicio = null;
	private Item<Location, String> spinner_destino = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_free_mode);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
        this.application = (CityFeels) getApplication();

		initiateSpinners();
		initiateButtons();

        this.setState(this.application);
		EventDispatcher.registerOnNewInstructions(this);
	}

	@Override
	public void onNewInstructions(String newInstructions) {
		TextView view = (TextView) findViewById(R.id.modoLivreDirecoes);
        view.setText(newInstructions);
	}

	@Override
	public void onResume() {
		super.onResume();
        setState(this.application);
		EventDispatcher.registerOnNewInstructions(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EventDispatcher.removeOnNewInstructions(this);
	}

    private void setState(CityFeels application) {
        TextView instructions = (TextView) findViewById(R.id.modoLivreDirecoes);
        instructions.setText(application.getLastInstructions());

        DataSource.DataLayer layer = this.application.getCurrentDataLayer();
        switch(layer) {
            case Another:
                findViewById(R.id.button4).setPressed(true);
                break;

            case Detailed:
                findViewById(R.id.button3).setPressed(true);
                break;

            case Local:
                findViewById(R.id.button2).setPressed(true);
                break;

            default:
                findViewById(R.id.button1).setPressed(true);
        }
    }

	private void setLayerButtonsClickListeners() {
		final Button basicLayerButton = (Button) findViewById(R.id.button1);
		final Button localLayerButton = (Button) findViewById(R.id.button2);
		final Button detailedLayerButton = (Button) findViewById(R.id.button3);
		final Button anotherLayerButton = (Button) findViewById(R.id.button4);

		basicLayerButton.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View view, MotionEvent event)
			{
				view.setPressed(true);
				localLayerButton.setPressed(false);
				detailedLayerButton.setPressed(false);
				anotherLayerButton.setPressed(false);
				application.setCurrentDataLayer(DataSource.DataLayer.Basic);
				return true;
			}
		});
		localLayerButton.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
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

	private void initiateButtons() {
        setLayerButtonsClickListeners();

		Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(FreeModeActivity.this, TestActivity.class);
                  startActivity(intent);
			  }
               });
	}

	private void initiateSpinners() {
		Spinner iniciosSpinner = (Spinner) findViewById(R.id.inicios);
		Spinner destinosSpinner = (Spinner) findViewById(R.id.destinos);
		iniciosSpinner.setEnabled(false);
		destinosSpinner.setEnabled(false);
		iniciosSpinner.setOnItemSelectedListener(onItemSelectedListener);
		destinosSpinner.setOnItemSelectedListener(onItemSelectedListener);

		populateSpinners();
	}

	private void populateSpinners() {
		new AsyncTask<Void, Void, Void>()
		{
			ArrayList<Item> pontosInicio = new ArrayList<Item>();
			ArrayList<Item> pontosFim = new ArrayList<Item>();

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

	/**
	 * Called when the user clicks the Calcular button
	 */
	public void calcular(View view) {
		Intent intent = new Intent(this, RouteActivity.class);
		startActivity(intent);
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
