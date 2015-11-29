package com.example.ana.cityfeels.models;

import com.example.ana.cityfeels.Location;

public class PontoInteresseBasic extends PontoInteresse {

    public PontoInteresseBasic(int id, Location posicao, String informacao, int orientacao) {
        super(id, posicao, informacao, orientacao);
    }

    public static PontoInteresseBasic fromPontoInteresse(com.example.ana.cityfeels.sia.PontoInteresse pontoInteresse)
    {
        return new PontoInteresseBasic(pontoInteresse.id, pontoInteresse.posicao, pontoInteresse.informacao, pontoInteresse.orientacao);
    }
}
