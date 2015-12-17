package com.example.ana.cityfeels.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.R;
import com.example.ana.cityfeels.modules.OrientationModule;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {
	private CityFeels application;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		this.application = (CityFeels) getApplication();

		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap map)
	{
		map.addMarker(new MarkerOptions()
				.position(new LatLng(41.1654034, -8.6085272))
				.title("Marker"));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.1654034, -8.6085272), 14));

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
