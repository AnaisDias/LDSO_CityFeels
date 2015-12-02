package com.example.ana.cityfeels;

import com.example.ana.cityfeels.models.PontoInteresse;
import com.example.ana.cityfeels.models.Percurso;
import com.example.ana.cityfeels.models.PontoInteresseBasic;
import com.example.ana.cityfeels.models.PontoInteresseDetailed;
import com.example.ana.cityfeels.models.PontoInteresseLocal;
import com.example.ana.cityfeels.sia.*;

import org.json.JSONException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DataSource {

    private DataSource() {}

    public enum DataLayer {
        Basic, Local, Detailed, Another
    }

    private final static int MAX_METERS_BETWEEN_POI = 50;
    private static PontoInteresse LAST_PONTO_INTERESSE = null;

    public static PontoInteresse getPontoInteresse(Location location, DataLayer layer) throws IOException {
        PontoInteresse pontoInteresse = null;

        try {
            com.example.ana.cityfeels.sia.PontoInteresse pi = SIA.getPontoInteresse(location);

            pontoInteresse = new PontoInteresseBasic(pi.id, pi.posicao, pi.informacao, pi.orientacao);

            if(layer != DataLayer.Basic)
            {
                pontoInteresse = toLocal((PontoInteresseBasic) pontoInteresse);

                if(layer == DataLayer.Local)
                    return pontoInteresse;

                pontoInteresse = toDetailed((PontoInteresseLocal) pontoInteresse, pi.infDetalhada);

                if(layer == DataLayer.Detailed)
                    return pontoInteresse;
            }

            return pontoInteresse;
        } catch (JSONException e) {
            return null;
        } finally {
            if(pontoInteresse != null)
                LAST_PONTO_INTERESSE = pontoInteresse;
        }
    }

    public static PontoInteresse getLastPontoInteresse() {
        return LAST_PONTO_INTERESSE;
    }

    public static Percurso getPercurso(int id) throws IOException {
        try {
            com.example.ana.cityfeels.sia.Percurso siaPercurso = SIA.getPercurso(id);

            if(siaPercurso == null)
                return null;

            return new Percurso(siaPercurso.id, siaPercurso.pontos);
        } catch (JSONException e) {
            return null;
        }
    }

    public static Direction getPercursoDirections(int percursoId, int pontoInteresseId) throws IOException {
        try {
            com.example.ana.cityfeels.sia.Direction siaDirection = SIA.getPercursoDirections(percursoId, pontoInteresseId);

            if(siaDirection == null)
                return null;

            return new Direction(siaDirection.text, siaDirection.orientacao);
        } catch (JSONException e) {
            return null;
        }
    }

    public static PontoInteresseLocal toLocal(PontoInteresseBasic pontoInteresseBasic) throws IOException, JSONException {
        List<PontoInteresseBasic> arredoresList = new LinkedList<PontoInteresseBasic>();

        for(com.example.ana.cityfeels.sia.PontoInteresse ponto : SIA.getCloseByPontoInteresse(pontoInteresseBasic.getPosicao(), MAX_METERS_BETWEEN_POI))
            arredoresList.add(PontoInteresseBasic.fromPontoInteresse(ponto));

        int id = pontoInteresseBasic.getId();
        Location posicao = pontoInteresseBasic.getPosicao();
        String informacao = pontoInteresseBasic.getInformacao();
        int orientacao = pontoInteresseBasic.getOrientacao();
        PontoInteresseBasic[] arredoresArray = arredoresList.toArray(new PontoInteresseBasic[0]);

        return new PontoInteresseLocal(id, posicao, informacao, orientacao, arredoresArray);
    }

    public static PontoInteresseDetailed toDetailed(PontoInteresseLocal pontoInteresseLocal, String infDetalhada) throws IOException, JSONException
    {
        List<PontoInteresseBasic> arredoresList = new LinkedList<PontoInteresseBasic>();

        for(com.example.ana.cityfeels.sia.PontoInteresse ponto : SIA.getCloseByPontoInteresse(pontoInteresseLocal.getPosicao(), MAX_METERS_BETWEEN_POI))
            arredoresList.add(PontoInteresseBasic.fromPontoInteresse(ponto));

        int id = pontoInteresseLocal.getId();
        Location posicao = pontoInteresseLocal.getPosicao();
        int orientacao = pontoInteresseLocal.getOrientacao();
        String informacao = infDetalhada;
        PontoInteresseBasic[] arredoresArray = arredoresList.toArray(new PontoInteresseBasic[0]);

        return new PontoInteresseDetailed(id, posicao, informacao, orientacao, arredoresArray);
    }

}
