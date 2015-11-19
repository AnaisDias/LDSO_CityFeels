package com.example.ana.cityfeels;

import com.example.ana.cityfeels.sia.PontoInteresse;
import com.example.ana.cityfeels.sia.Percurso;

import org.json.JSONException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DataSource {

    private DataSource() {}

    public enum DataLayer {
        Basic, Local, Detailed, Another
    }

    private final static int MAX_DISTANCE_BETWEEN_POI = 50;
    private static IPontoInteresse LAST_POINT_OF_INTEREST = null;

    public static IPontoInteresse getPointOfInterest(Location location, DataLayer layer) throws IOException {
        return DataSource.getPointOfInterest(location, layer, null);
    }

    public static IPontoInteresse getPointOfInterest(Location location, DataLayer layer, Percurso percurso) throws IOException {
        IPontoInteresse pontoInteresse = null;

        try {
            PontoInteresse pi = SIA.getPointOfInterest(location);

            pontoInteresse = new PontoInteresseBasic(pi.posicao, pi.informacao, pi.orientacao);

            if(layer != DataLayer.Basic)
            {
                pontoInteresse = toLocal((PontoInteresseBasic) pontoInteresse);

                if(layer == DataLayer.Local)
                    return pontoInteresse;

                pontoInteresse = toDetailed(pi);

                if(layer == DataLayer.Detailed)
                    return pontoInteresse;
            }

            return pontoInteresse;
        } catch (JSONException e) {
            return null;
        } finally {
            if(pontoInteresse != null)
                LAST_POINT_OF_INTEREST = pontoInteresse;
        }
    }

    public static IPontoInteresse getLastPointOfInterest() {
        return LAST_POINT_OF_INTEREST;
    }

    public static PontoInteresseLocal toLocal(PontoInteresseBasic pontoInteresseBasic) throws IOException, JSONException {
        List<PontoInteresseBasic> arredoresList = new LinkedList<PontoInteresseBasic>();

        for(PontoInteresse ponto : SIA.getCloseByPointsOfInterest(pontoInteresseBasic.posicao, MAX_DISTANCE_BETWEEN_POI))
            arredoresList.add(PontoInteresseBasic.fromPontoInteresse(ponto));

        Location posicao = pontoInteresseBasic.posicao;
        String informacao = pontoInteresseBasic.informacao;
        int orientacao = pontoInteresseBasic.orientacao;
        PontoInteresseBasic[] arredoresArray = arredoresList.toArray(new PontoInteresseBasic[0]);

        return new PontoInteresseLocal(posicao, informacao, orientacao, arredoresArray);
    }

    public static PontoInteresseDetailed toDetailed(PontoInteresse pontoInteresse)throws IOException, JSONException
    {
        List<PontoInteresseBasic> arredoresList = new LinkedList<PontoInteresseBasic>();

        for(PontoInteresse ponto : SIA.getCloseByPointsOfInterest(pontoInteresse.posicao, MAX_DISTANCE_BETWEEN_POI))
            arredoresList.add(PontoInteresseBasic.fromPontoInteresse(ponto));

        Location posicao = pontoInteresse.posicao;
        int orientacao = pontoInteresse.orientacao;
        String informacao = pontoInteresse.infDetalhada;
        PontoInteresseBasic[] arredoresArray = arredoresList.toArray(new PontoInteresseBasic[0]);

        return new PontoInteresseDetailed(posicao, informacao, orientacao, arredoresArray);
    }

}
