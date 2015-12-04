package com.example.ana.cityfeels.sia;

import com.example.ana.cityfeels.Item;
import com.example.ana.cityfeels.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SIA
{

	private static final String WEB_SERVICE_URL = "https://database-sia.herokuapp.com";
	private static final String WEB_SERVICE_POI_URL = WEB_SERVICE_URL + "/pontos";
	private static final String WEB_SERVICE_PERCURSO_URL = WEB_SERVICE_URL + "/percursos";
	private static final String WEB_SERVICE_INICIOS_URL = WEB_SERVICE_URL + "/inicios";
	private static final String WEB_SERVICE_DESTINOS_URL = WEB_SERVICE_URL + "/destinos";

	private SIA()
	{
	}

	public static PontoInteresse getPontoInteresse(
			Location location) throws JSONException, IOException
	{
		String url = WEB_SERVICE_POI_URL;
		String queryString = String.format("?lat=%s&long=%s", location.latitude,
										   location.longitude);

		JSONObject response = JsonHttpRequest.getObject(url + queryString);

		if(response == null)
			return null;
		else
		{
			int id = response.getInt("id");
			String about = response.getString("informacao");
			int orientation = response.getInt("orientacao");
			String detAbout = response.getString("infdetalhada");
			return new PontoInteresse(id, location, about, orientation, detAbout);
		}
	}

	public static PontoInteresse[] getCloseByPontoInteresse(Location location,
															float distanceInMeters) throws IOException, JSONException
	{
		String url = WEB_SERVICE_POI_URL;
		String queryString = String.format("?lat=%s&long=%s", location.latitude,
										   location.longitude);
		queryString += "&distance=" + distanceInMeters;

		JSONArray results = JsonHttpRequest.getArray(url + queryString);
		if(results == null)
			return null;
		else
		{
			PontoInteresse[] closeByPoints = new PontoInteresse[results.length()];
			for(int i = 0; i < results.length(); i++)
			{
				JSONObject currentPoint = results.getJSONObject(i);
				int id = currentPoint.getInt("id");
				String infdetalhada = currentPoint.getString("infdetalhada");
				String informacao = currentPoint.getString("informacao");
				int orientacao = currentPoint.getInt("orientacao");
				double latitude = currentPoint.getDouble("latitude");
				double longitude = currentPoint.getDouble("longitude");
				closeByPoints[i] = new PontoInteresse(id, new Location(latitude, longitude),
													  informacao, orientacao, infdetalhada);
			}

			return closeByPoints;
		}
	}

	public static Percurso getPercurso(int id) throws IOException, JSONException
	{
		String url = WEB_SERVICE_PERCURSO_URL;
		String queryString = "?id=" + id;

		JSONObject result = JsonHttpRequest.getObject(url + queryString);
		if(result == null)
			return null;
		else
		{
			JSONArray pontosArray = result.getJSONArray("pontos");

			int[] pontos = new int[pontosArray.length()];
			for(int index = 0; index < pontosArray.length(); index++)
				pontos[index] = pontosArray.getInt(index);

			return new Percurso(id, pontos);
		}
	}

	public static Direction getPercursoDirections(int percursoId,
												  int pontoId) throws IOException, JSONException
	{
		String url = WEB_SERVICE_PERCURSO_URL;
		String queryString = "?id=" + percursoId + "&pontoId=" + pontoId;

		JSONObject response = JsonHttpRequest.getObject(url + queryString);

		if(response == null)
			return null;
		else
		{
			String text = response.getString("informacao");
			int orientacao = response.getInt("orientacao");

			return new Direction(text, orientacao);
		}
	}

	public static ArrayList<Item> getStartPoints() throws IOException, JSONException
	{
		String url = WEB_SERVICE_INICIOS_URL;

		JSONArray response = JsonHttpRequest.getArray(url);

		if(response == null)
			return null;
		else
		{
			ArrayList<Item> list = new ArrayList<>();
			for(int i = 0; i < response.length(); i++)
			{
				JSONObject current = response.getJSONObject(i);
				double lat = current.getDouble("latitude");
				double lon = current.getDouble("longitude");
				String name = current.getString("nome");
				list.add(new Item<>(new Location(lat, lon), name));
			}
			return list;
		}
	}
	public static ArrayList<Item> getStartPoints(Location location) throws IOException, JSONException
	{
		String url = WEB_SERVICE_INICIOS_URL;
		String queryString = String.format("?lat=%s&long=%s", location.latitude,
										   location.longitude);

		JSONArray response = JsonHttpRequest.getArray(url + queryString);

		if(response == null)
			return null;
		else
		{
			ArrayList<Item> list = new ArrayList<>();
			for(int i = 0; i < response.length(); i++)
			{
				JSONObject current = response.getJSONObject(i);
				double lat = current.getDouble("latitude");
				double lon = current.getDouble("longitude");
				String name = current.getString("nome");
				list.add(new Item<>(new Location(lat, lon), name));
			}
			return list;
		}
	}
	public static ArrayList<Item> getDestinations() throws IOException, JSONException
	{
		String url = WEB_SERVICE_DESTINOS_URL;

		JSONArray response = JsonHttpRequest.getArray(url);

		if(response == null)
			return null;
		else
		{
			ArrayList<Item> list = new ArrayList<>();
			for(int i = 0; i < response.length(); i++)
			{
				JSONObject current = response.getJSONObject(i);
				double lat = current.getDouble("latitude");
				double lon = current.getDouble("longitude");
				String name = current.getString("nome");
				list.add(new Item<>(new Location(lat, lon), name));
			}
			return list;
		}
	}
	public static ArrayList<Item> getDestinations(Location location) throws IOException, JSONException
	{
		String url = WEB_SERVICE_DESTINOS_URL;
		String queryString = String.format("?lat=%s&long=%s", location.latitude,
										   location.longitude);

		JSONArray response = JsonHttpRequest.getArray(url + queryString);

		if(response == null)
			return null;
		else
		{
			ArrayList<Item> list = new ArrayList<>();
			for(int i = 0; i < response.length(); i++)
			{
				JSONObject current = response.getJSONObject(i);
				double lat = current.getDouble("latitude");
				double lon = current.getDouble("longitude");
				String name = current.getString("nome");
				list.add(new Item<>(new Location(lat, lon), name));
			}
			return list;
		}
	}
}
