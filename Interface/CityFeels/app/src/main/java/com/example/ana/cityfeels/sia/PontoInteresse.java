package com.example.ana.cityfeels.sia;

import com.example.ana.cityfeels.Location;

public class PontoInteresse {

    public Location posicao;
    public String informacao;
    public int orientacao;
    public String infDetalhada;

    public PontoInteresse() {}

    public PontoInteresse(Location local, String informacao, int orientacao, String infDetalhada)
    {
        this.informacao = informacao;
        this.posicao = local;
        this.orientacao = orientacao;
        this.infDetalhada = infDetalhada;
    }

}
