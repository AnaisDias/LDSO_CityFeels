package com.example.ana.cityfeels;

import com.example.ana.cityfeels.sia.PontoInteresse;

public class PontoInteresseBasic implements IPontoInteresse {

    public Location posicao;
    public String informacao;
    public int orientacao;

    public PontoInteresseBasic(Location local, String informacao, int orientacao)
    {
        this.informacao = informacao;
        this.posicao = local;
        this.orientacao = orientacao;
    }

    @Override
    public String getInformation() {
        return informacao;
    }

    @Override
    public int getOrientation() {
        return orientacao;
    }

    public static PontoInteresseBasic fromPontoInteresse(PontoInteresse pontoInteresse)
    {
        return new PontoInteresseBasic(pontoInteresse.posicao, pontoInteresse.informacao, pontoInteresse.orientacao);
    }
}
