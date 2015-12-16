package com.example.ana.cityfeels.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.DataSource;
import com.example.ana.cityfeels.Location;
import com.example.ana.cityfeels.R;
import com.example.ana.cityfeels.modules.OrientationModule;
import com.example.ana.cityfeels.modules.TextToSpeechModule;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback
{

	private static final String NULL_PONTO_INTERESSE_ERROR = "Não foi possível obter o ponto de interesse";
	private static final String NULL_PERCURSO_ERROR = "Não foi possível obter o percurso";

	private static Location[] TEST_LOCATIONS;
	private static String[] INFO_ORIENTATION;
	private static String INFO_ORIENTATION_DEFAULT;

	private CityFeels application;
	private TextToSpeechModule textToSpeech;

	static
	{
		INFO_ORIENTATION_DEFAULT = "Perto de si ";
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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.application = (CityFeels) getApplication();
		this.textToSpeech = this.application.getTextToSpeechModule();

		setContentView(R.layout.activity_route);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		setLayerButtonsClickListeners();
		setRepeatInstructionsButtonListener();
		setMapButtonListeners();
		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		View basicLayerButton = findViewById(R.id.button1);
		basicLayerButton.setPressed(true);
	}

	@Override
	public void onMapReady(GoogleMap map)
	{
		map.addMarker(new MarkerOptions()
							  .position(new LatLng(41.1654034, -8.6085272))
							  .title("Marker"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.1654034, -8.6085272), 14));

	}

	private void setMapButtonListeners()
	{
		/*
		Button mapViewButton = (Button) findViewById(R.id.map_view_button);
		mapViewButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(view.getContext(), MapsActivity.class);
				startActivity(intent);
			}
		});
		*/
	}

	private void setLayerButtonsClickListeners()
	{
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
		detailedLayerButton.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
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

	private void setRepeatInstructionsButtonListener()
	{
		ImageButton repeatInstructionsButton = (ImageButton) findViewById(R.id.repeatButtonImage);
		repeatInstructionsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				textToSpeech.repeat();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up buttonprincipal, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if(id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		OrientationModule module = this.application.getOrientationModule();
		if(!module.isActivated())
			module.askForActivation(this, getFragmentManager());
	}
}
