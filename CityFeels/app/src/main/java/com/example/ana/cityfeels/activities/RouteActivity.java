package com.example.ana.cityfeels.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.DataSource;
import com.example.ana.cityfeels.Location;
import com.example.ana.cityfeels.R;
import com.example.ana.cityfeels.models.Percurso;
import com.example.ana.cityfeels.models.PontoInteresse;
import com.example.ana.cityfeels.modules.OrientationModule;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private CityFeels application;
    private Percurso percurso = null;
    private HashMap<Integer, PontoInteresse> pontosInteresse = new HashMap<Integer, PontoInteresse>();
    private ArrayList<Location> courseCoords;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.application = (CityFeels) getApplication();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setMyLocationEnabled(true);
        loadPercurso();

        /*
        if(courseCoords.size() != 0){
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(courseCoords.get(0).latitude, courseCoords.get(0).longitude))
                    .title("Start"));
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(courseCoords.get(courseCoords.size() - 1).latitude, courseCoords.get(courseCoords.size() - 1).
                            longitude))
                    .title("End"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(courseCoords.get(0).latitude, courseCoords.get(0).longitude), 14));
            for(int i = 1; i < courseCoords.size(); i++) {
                map.addPolyline(new PolylineOptions()
                        .add(new LatLng(courseCoords.get(i-1).latitude, courseCoords.get(i-1).longitude), new LatLng(courseCoords.get(i).latitude, courseCoords.get(i).longitude))
                        .width(5)
                        .color(Color.RED));
            }

        }
        else{

        }


        */
    }

    private void loadPercurso() {
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        String[] points = info.split(";");
        String[] coordsStart = points[0].split(",");
        String[] coordsEnd = points[1].split(",");
        Location start = new Location(Double.parseDouble(coordsStart[0]), Double.parseDouble(coordsStart[1]));
        Location end = new Location(Double.parseDouble(coordsEnd[0]), Double.parseDouble(coordsEnd[1]));

        try {
            this.percurso = DataSource.getPercurso(start, end);

            int[] pontos = percurso.getPointsOfInterestIds();
            Integer[] pontosReference = new Integer[pontos.length];

            for(int i = 0; i < pontos.length; i++)
                pontosReference[i] = new Integer(pontos[i]);

            loadPontosInteresse.execute(pontosReference);
        } catch(IOException e)
        {
            Toast.makeText(this, "Não foi possivel obter o percurso!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up primaryButton, so long
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

    /*
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
    */

    // TODO: Tratar do caso em que nem todos os pontos alcançados
    private AsyncTask<Integer, Void, Void> loadPontosInteresse = new AsyncTask<Integer, Void, Void>() {
        @Override
        protected Void doInBackground(Integer... ids) {
            for(Integer id : ids) {
                try {
                    PontoInteresse pontoInteresse = DataSource.getPontoInteresse(id.intValue(), DataSource.DataLayer.Basic);
                    pontosInteresse.put(id, pontoInteresse);
                } catch(IOException e) {
                    Toast.makeText(RouteActivity.this, "Não foi possivel obter o ponto de interesse!", Toast.LENGTH_LONG).show();
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void arg) {
            int startId = percurso.getStartingPointId();
            int endId = percurso.getEndingPointId();

            Location start = pontosInteresse.get(Integer.valueOf(startId)).getPosicao();
            Location end = pontosInteresse.get(Integer.valueOf(endId)).getPosicao();

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(start.latitude, start.longitude))
                    .title("Início"));
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(end.latitude, end.longitude))
                    .title("Fim"));

            int[] pontos = percurso.getPointsOfInterestIds();
            for(int i = 1; i < pontos.length; i++) {
                Location prevLocation = pontosInteresse.get(Integer.valueOf(pontos[i - 1])).getPosicao();
                Location currLocation = pontosInteresse.get(Integer.valueOf(pontos[i])).getPosicao();

                map.addPolyline(new PolylineOptions()
                        .add(new LatLng(prevLocation.latitude, prevLocation.longitude), new LatLng(currLocation.latitude, currLocation.longitude))
                        .width(5)
                        .color(Color.RED));
            }

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(start.latitude, start.longitude), 20));

            Toast.makeText(RouteActivity.this, "O percurso está carregado!", Toast.LENGTH_LONG).show();
        }
    };

}
