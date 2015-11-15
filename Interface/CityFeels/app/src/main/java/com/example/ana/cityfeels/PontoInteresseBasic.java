package com.example.ana.cityfeels;

import com.example.ana.cityfeels.sia.PontoInteresse;

public class PontoInteresseBasic implements IPontoInteresse {

    public Location posicao;
    public String informacao;

    public PontoInteresseBasic(Location local, String informacao)
    {
        this.informacao = informacao;
        this.posicao = local;
    }

    @Override
    public String getInformation() {
        return informacao;
    }

    public static PontoInteresseBasic fromPontoInteresse(PontoInteresse pontoInteresse)
    {
        return new PontoInteresseBasic(pontoInteresse.posicao, pontoInteresse.informacao);
    }
}
