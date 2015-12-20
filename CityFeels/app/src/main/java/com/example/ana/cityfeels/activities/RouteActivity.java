package com.example.ana.cityfeels.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.Item;
import com.example.ana.cityfeels.Location;
import com.example.ana.cityfeels.R;
import com.example.ana.cityfeels.modules.OrientationModule;
import com.example.ana.cityfeels.sia.SIA;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.ana.cityfeels.sia.SIA.getStartPoints;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {
    private CityFeels application;
    ArrayList<Location> courseCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.application = (CityFeels) getApplication();

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        String[] points = info.split(";");
        String[] coordsStart = points[0].split(",");
        String[] coordsEnd = points[1].split(",");
        Location start = new Location(Double.parseDouble(coordsStart[0]), Double.parseDouble(coordsStart[1]));
        Location end = new Location(Double.parseDouble(coordsEnd[0]), Double.parseDouble(coordsEnd[1]));
        loadCourseCoords(start, end);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(41.1654034, -8.6085272))
                .title("Marker"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.1654034, -8.6085272), 14));

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

    @Override
    public void onResume() {
        super.onResume();
        OrientationModule module = this.application.getOrientationModule();
        if (!module.isActivated())
            module.askForActivation(this, getFragmentManager());
    }

    public void loadCourseCoords(final Location start, final Location end)
    {
        courseCoords = new ArrayList<>();
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    courseCoords = SIA.getPercursoCoords(start, end);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void results) {
                //Acho que aqui e que se deve iniciar o mapa mas n tenho a certeza
                for(int i = 0; i < courseCoords.size(); i++)
                {
                    Log.d("test", courseCoords.get(i).toString());
                }
            }
        }.execute();
    }

}
