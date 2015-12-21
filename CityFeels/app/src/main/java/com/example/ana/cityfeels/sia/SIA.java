package com.example.ana.cityfeels.sia;

import android.util.Log;

import com.example.ana.cityfeels.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SIA {

    private static final String WEB_SERVICE_URL = "https://database-sia.herokuapp.com";
    private static final String WEB_SERVICE_POI_URL = WEB_SERVICE_URL + "/pontos";
    private static final String WEB_SERVICE_PERCURSO_URL = WEB_SERVICE_URL + "/percursos";
    private static final String WEB_SERVICE_INICIOS_URL = WEB_SERVICE_URL + "/inicios";
    private static final String WEB_SERVICE_DESTINOS_URL = WEB_SERVICE_URL + "/destinos";

    private SIA() {
    }

    public static PontoInteresse[] getPontosInteresse() throws IOException, JSONException {
        String url = WEB_SERVICE_POI_URL;

        JSONArray result = JsonHttpRequest.getArray(url);
        ArrayList<PontoInteresse> pontos = new ArrayList<>();

        for (int i = 0; i < result.length(); i++) {
            JSONObject ponto = result.getJSONObject(i);

            int id = ponto.getInt("id");
            String nome = ponto.getString("nome");
            double latitude = ponto.getDouble("latitude");
            double longitude = ponto.getDouble("longitude");
            String about = ponto.getString("informacao");
            int orientation = ponto.getInt("orientacao");
            String detAbout = ponto.getString("infdetalhada");

            pontos.add(new PontoInteresse(id, nome, new Location(latitude, longitude), about, orientation, detAbout));
        }

        return pontos.toArray(new PontoInteresse[]{});
    }

    public static PontoInteresse getPontoInteresse(Location location) throws JSONException, IOException {
        String url = WEB_SERVICE_POI_URL;
        String queryString = String.format("?lat=%s&long=%s", location.latitude, location.longitude);

        JSONObject response = JsonHttpRequest.getObject(url + queryString);

        int id = response.getInt("id");
        String nome = response.getString("nome");
        String about = response.getString("informacao");
        int orientation = response.getInt("orientacao");
        String detAbout = response.getString("infdetalhada");

        return new PontoInteresse(id, nome, location, about, orientation, detAbout);
    }

    public static PontoInteresse getPontoInteresse(int id) throws JSONException, IOException {
        String url = WEB_SERVICE_POI_URL;
        String queryString = String.format("?id=%s", id);

        JSONObject response = JsonHttpRequest.getObject(url + queryString);

        String nome = response.getString("nome");
        String about = response.getString("informacao");
        int orientation = response.getInt("orientacao");
        String detAbout = response.getString("infdetalhada");
        double latitude = response.getDouble("latitude");
        double longitude = response.getDouble("longitude");

        return new PontoInteresse(id, nome, new Location(latitude, longitude), about, orientation, detAbout);
    }

    public static PontoInteresse[] getCloseByPontoInteresse(Location location, float distanceInMeters) throws IOException, JSONException {
        String url = WEB_SERVICE_POI_URL;
        String queryString = String.format("?lat=%s&long=%s", location.latitude, location.longitude);
        queryString += "&distance=" + distanceInMeters;

        JSONArray results = JsonHttpRequest.getArray(url + queryString);
        PontoInteresse[] closeByPoints = new PontoInteresse[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSONObject currentPoint = results.getJSONObject(i);
            int id = currentPoint.getInt("id");
            String nome = currentPoint.getString("nome");
            String infdetalhada = currentPoint.getString("infdetalhada");
            String informacao = currentPoint.getString("informacao");
            int orientacao = currentPoint.getInt("orientacao");
            float lat = (float)currentPoint.getDouble("latitude");
            float lon = (float)currentPoint.getDouble("longitude");

            closeByPoints[i] = new PontoInteresse(id, nome, new Location(lat, lon), informacao, orientacao, infdetalhada);
        }

        return closeByPoints;
    }

    public static Percurso[] getPercursos() throws IOException, JSONException {
        String url = WEB_SERVICE_PERCURSO_URL;

        JSONArray result = JsonHttpRequest.getArray(url);
        ArrayList<Percurso> percursos = new ArrayList<>();

        for (int i = 0; i < result.length(); i++) {
            JSONObject percursoJson = result.getJSONObject(i);
            JSONArray pontosJson = percursoJson.getJSONArray("pontos");

            int id = percursoJson.getInt("id");
            int[] pontos = new int[pontosJson.length()];

            for (int index = 0; index < pontosJson.length(); index++)
                pontos[index] = pontosJson.getInt(index);

            percursos.add(i, new Percurso(id, pontos));
        }

        return percursos.toArray(new Percurso[]{});
    }

    public static Percurso getPercurso(int id) throws IOException, JSONException {
        String url = WEB_SERVICE_PERCURSO_URL;
        String queryString = "?id=" + id;

        JSONObject result = JsonHttpRequest.getObject(url + queryString);
        JSONArray pontosArray = result.getJSONArray("pontos");

        int[] pontos = new int[pontosArray.length()];
        for (int index = 0; index < pontosArray.length(); index++)
            pontos[index] = pontosArray.getInt(index);

        return new Percurso(id, pontos);
    }

    public static Percurso getPercurso(Location start, Location end) throws IOException, JSONException {
        String url = WEB_SERVICE_PERCURSO_URL;
        String queryString = String.format("?startLat=%s&startLong=%s&endLat=%s&endLong=%s",
                start.latitude, start.longitude, end.latitude, end.longitude);

        JSONObject result = JsonHttpRequest.getObject(url + queryString);
        JSONArray pontosArray = result.getJSONArray("pontos");

        int id = result.getInt("id");
        int[] pontos = new int[pontosArray.length()];
        for (int index = 0; index < pontosArray.length(); index++)
            pontos[index] = pontosArray.getInt(index);

        return new Percurso(id, pontos);
    }

    public static Direction getPercursoDirections(int percursoId, int pontoId) throws IOException, JSONException {
        String url = WEB_SERVICE_PERCURSO_URL;
        String queryString = "?id=" + percursoId + "&pontoId=" + pontoId;

        JSONObject response = JsonHttpRequest.getObject(url + queryString);

        if (response == null)
            return null;
        else {
            String text = response.getString("informacao");
            int orientacao = response.getInt("orientacao");

            return new Direction(text, orientacao);
        }
    }

    public static PontoInteresse[] getStartPoints() throws IOException, JSONException {
        String url = WEB_SERVICE_INICIOS_URL;

        JSONArray result = JsonHttpRequest.getArray(url);
        ArrayList<PontoInteresse> pontos = new ArrayList<>();

        for (int i = 0; i < result.length(); i++) {
            JSONObject ponto = result.getJSONObject(i);

            int id = ponto.getInt("id");
            String nome = ponto.getString("nome");
            double latitude = ponto.getDouble("latitude");
            double longitude = ponto.getDouble("longitude");
            String about = ponto.getString("informacao");
            int orientation = ponto.getInt("orientacao");
            String detAbout = ponto.getString("infdetalhada");

            pontos.add(new PontoInteresse(id, nome, new Location(latitude, longitude), about, orientation, detAbout));
        }

        return pontos.toArray(new PontoInteresse[]{});
    }

    public static PontoInteresse[] getDestinations() throws IOException, JSONException {
        String url = WEB_SERVICE_DESTINOS_URL;

        JSONArray result = JsonHttpRequest.getArray(url);
        ArrayList<PontoInteresse> pontos = new ArrayList<>();

        for (int i = 0; i < result.length(); i++) {
            JSONObject ponto = result.getJSONObject(i);

            int id = ponto.getInt("id");
            String nome = ponto.getString("nome");
            double latitude = ponto.getDouble("latitude");
            double longitude = ponto.getDouble("longitude");
            String about = ponto.getString("informacao");
            int orientation = ponto.getInt("orientacao");
            String detAbout = ponto.getString("infdetalhada");

            pontos.add(new PontoInteresse(id, nome, new Location(latitude, longitude), about, orientation, detAbout));
        }

        return pontos.toArray(new PontoInteresse[]{});
    }

    /*
    public static ArrayList<Location> getPercursoCoords(Location start, Location end) throws IOException, JSONException {
        String url = WEB_SERVICE_PERCURSO_URL;
        String queryString = String.format("?slat=%s&slong=%s&elat=%s&elong=%s",
                start.latitude, start.longitude, end.latitude, end.longitude);
        Log.d("test", url + queryString);

        JSONArray response = JsonHttpRequest.getArray(url + queryString);

        if (response == null)
            return null;
        else
        {
            ArrayList<Location> list = new ArrayList<>();
            for(int i = 0; i < response.length(); i++)
            {
                JSONObject current = response.getJSONObject(i);
                double lat = current.getDouble("latitude");
                double lon = current.getDouble("longitude");
                list.add(new Location(lat, lon));
            }
            return list;
        }
    */
}
