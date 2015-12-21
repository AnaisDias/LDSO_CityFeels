package com.example.ana.cityfeels;

import android.util.Log;

import com.example.ana.cityfeels.models.PontoInteresse;
import com.example.ana.cityfeels.models.Percurso;
import com.example.ana.cityfeels.models.PontoInteresseBasic;
import com.example.ana.cityfeels.models.PontoInteresseDetailed;
import com.example.ana.cityfeels.models.PontoInteresseLocal;
import com.example.ana.cityfeels.sia.*;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DataSource
{

	private DataSource()
	{
	}

	public enum DataLayer
	{
		Basic, Local, Detailed, Another
	}

	private final static int MAX_METERS_BETWEEN_POI = 50;

	public static PontoInteresse getPontoInteresse(Location location,
												   DataLayer layer) throws IOException {
		try
		{
			com.example.ana.cityfeels.sia.Location siaLocation = new com.example.ana.cityfeels.sia.Location(location.latitude, location.longitude);
			com.example.ana.cityfeels.sia.PontoInteresse pi = SIA.getPontoInteresse(siaLocation);

			return fromPontoInteresse(pi, layer);
		} catch(JSONException e)
		{
			Log.e("JSON", e.getMessage());
			return null;
		}
	}

	public static PontoInteresse getPontoInteresse(int id, DataLayer layer) throws IOException {
		try
		{
			com.example.ana.cityfeels.sia.PontoInteresse pi = SIA.getPontoInteresse(id);

			return fromPontoInteresse(pi, layer);
		} catch(JSONException e)
		{
			Log.e("JSON", e.getMessage());
			return null;
		}
	}

	public static PontoInteresse[] getPontosInteresse() throws IOException {
		try {
			com.example.ana.cityfeels.sia.PontoInteresse[] pontos = SIA.getPontosInteresse();
			ArrayList<PontoInteresse> pontosInteresse = new ArrayList<>();

			for(com.example.ana.cityfeels.sia.PontoInteresse ponto : pontos) {
				Location location = fromSiaLocation(ponto.posicao);
				pontosInteresse.add(new PontoInteresseBasic(ponto.id, ponto.nome, location, ponto.informacao, ponto.orientacao));
			}

			return pontosInteresse.toArray(new PontoInteresse[]{});
		} catch (JSONException e) {
			Log.e("JSON", e.getMessage());
			return new PontoInteresse[0];
		}
	}

	public static PontoInteresse[] getStartPontosInteresse(Location end) throws IOException {
		com.example.ana.cityfeels.sia.PontoInteresse[] pontos;

		try {
			if(end == null)
				pontos = SIA.getStartPoints();
			else
				pontos = SIA.getStartPoints();

			ArrayList<PontoInteresse> pontosReference = new ArrayList<>();
			for(com.example.ana.cityfeels.sia.PontoInteresse ponto : pontos) {
				Location location = fromSiaLocation(ponto.posicao);

				pontosReference.add(new PontoInteresseBasic(ponto.id, ponto.nome, location, ponto.informacao, ponto.orientacao));
			}

			return pontosReference.toArray(new PontoInteresse[] {});
		} catch(JSONException e)
		{
			Log.e("JSON", e.getMessage());
			return new PontoInteresse[0];
		}
	}

	public static PontoInteresse[] getStartPontosInteresse() throws IOException {
		return getStartPontosInteresse(null);
	}

	public static PontoInteresse[] getEndPontosInteresse(Location start) throws IOException {
		com.example.ana.cityfeels.sia.PontoInteresse[] pontos;

		try {
			if(start == null)
				pontos = SIA.getDestinations();
			else
				pontos = SIA.getDestinations();

			ArrayList<PontoInteresse> pontosReference = new ArrayList<>();
			for(com.example.ana.cityfeels.sia.PontoInteresse ponto : pontos) {
				Location location = fromSiaLocation(ponto.posicao);

				pontosReference.add(new PontoInteresseBasic(ponto.id, ponto.nome, location, ponto.informacao, ponto.orientacao));
			}

			return pontosReference.toArray(new PontoInteresse[] {});
		} catch(JSONException e)
		{
			Log.e("JSON", e.getMessage());
			return new PontoInteresse[0];
		}
	}

	public static PontoInteresse[] getEndPontosInteresse() throws IOException {
		return getEndPontosInteresse(null);
	}

	public static Percurso getPercurso(int id) throws IOException {
		try
		{
			com.example.ana.cityfeels.sia.Percurso siaPercurso = SIA.getPercurso(id);

			if(siaPercurso == null)
				return null;

			return new Percurso(siaPercurso.id, siaPercurso.pontos);
		} catch(JSONException e)
		{
			Log.e("JSON", e.getMessage());
			return null;
		}
	}

	public static Percurso getPercurso(Location start, Location end) throws IOException {
		try
		{
			com.example.ana.cityfeels.sia.Location siaLocationStart = toSiaLocation(start);
			com.example.ana.cityfeels.sia.Location siaLocationEnd = toSiaLocation(end);

			com.example.ana.cityfeels.sia.Percurso siaPercurso = SIA.getPercurso(siaLocationStart, siaLocationEnd);

			if(siaPercurso == null)
				return null;

			return new Percurso(siaPercurso.id, siaPercurso.pontos);
		} catch(JSONException e)
		{
			Log.e("JSON", e.getMessage());
			return null;
		}
	}

	public static Instructions getPercursoDirections(int percursoId,
												  int pontoInteresseId) throws IOException {
		try
		{
			com.example.ana.cityfeels.sia.Direction siaDirection = SIA.getPercursoDirections(
					percursoId, pontoInteresseId);

			if(siaDirection == null)
				return null;

			return new Instructions(siaDirection.text, siaDirection.orientacao);
		} catch(JSONException e)
		{
			Log.e("JSON", e.getMessage());
			return null;
		}
	}


	public static PontoInteresseLocal toLocal(PontoInteresseBasic pontoInteresseBasic) throws IOException, JSONException {
		List<PontoInteresseBasic> arredoresList = new LinkedList<PontoInteresseBasic>();

		com.example.ana.cityfeels.sia.Location siaLocation = toSiaLocation(pontoInteresseBasic.getPosicao());
		for(com.example.ana.cityfeels.sia.PontoInteresse ponto : SIA.getCloseByPontoInteresse(siaLocation, MAX_METERS_BETWEEN_POI))
			arredoresList.add(fromPontoInteresse(ponto));

		int id = pontoInteresseBasic.getId();
		String nome = pontoInteresseBasic.getNome();
		Location posicao = pontoInteresseBasic.getPosicao();
		String informacao = pontoInteresseBasic.getInformacao();
		int orientacao = pontoInteresseBasic.getOrientacao();
		PontoInteresseBasic[] arredoresArray = arredoresList.toArray(new PontoInteresseBasic[0]);

		return new PontoInteresseLocal(id, nome, posicao, informacao, orientacao, arredoresArray);
	}

	public static PontoInteresseDetailed toDetailed(PontoInteresseLocal pontoInteresseLocal,
													String infDetalhada) throws IOException, JSONException {
		List<PontoInteresseBasic> arredoresList = new LinkedList<PontoInteresseBasic>();

		com.example.ana.cityfeels.sia.Location siaLocation = toSiaLocation(pontoInteresseLocal.getPosicao());
		for(com.example.ana.cityfeels.sia.PontoInteresse ponto : SIA.getCloseByPontoInteresse(siaLocation, MAX_METERS_BETWEEN_POI))
			arredoresList.add(fromPontoInteresse(ponto));

		int id = pontoInteresseLocal.getId();
		String nome = pontoInteresseLocal.getNome();
		Location posicao = pontoInteresseLocal.getPosicao();
		int orientacao = pontoInteresseLocal.getOrientacao();
		String informacao = infDetalhada;
		PontoInteresseBasic[] arredoresArray = arredoresList.toArray(new PontoInteresseBasic[0]);

		return new PontoInteresseDetailed(id, nome, posicao, informacao, orientacao, arredoresArray);
	}

	private static PontoInteresseBasic fromPontoInteresse(com.example.ana.cityfeels.sia.PontoInteresse pontoInteresse) {
		com.example.ana.cityfeels.sia.Location siaLocation = pontoInteresse.posicao;
		Location location = new Location(siaLocation.latitude, siaLocation.longitude);
		return new PontoInteresseBasic(pontoInteresse.id, pontoInteresse.nome, location, pontoInteresse.informacao, pontoInteresse.orientacao);
	}

	private static PontoInteresse fromPontoInteresse(com.example.ana.cityfeels.sia.PontoInteresse pontoInteresse, DataLayer layer) throws IOException, JSONException {
		com.example.ana.cityfeels.sia.Location siaLocation = pontoInteresse.posicao;
		Location location = new Location(siaLocation.latitude, siaLocation.longitude);

		PontoInteresse pi = fromPontoInteresse(pontoInteresse);
		if(layer != DataLayer.Basic)
		{
			pi = toLocal((PontoInteresseBasic) pi);

			if(layer == DataLayer.Local)
				return pi;

			pi = toDetailed((PontoInteresseLocal) pi, pontoInteresse.infDetalhada);

			if(layer == DataLayer.Detailed)
				return pi;
		}

		return new PontoInteresseBasic(pontoInteresse.id, pontoInteresse.nome, location, pontoInteresse.informacao, pontoInteresse.orientacao);
	}

	private static Location fromSiaLocation(com.example.ana.cityfeels.sia.Location location) {
		return new Location(location.latitude, location.longitude);
	}

	private static com.example.ana.cityfeels.sia.Location toSiaLocation(Location location) {
		return new com.example.ana.cityfeels.sia.Location(location.latitude, location.longitude);
	}

}
