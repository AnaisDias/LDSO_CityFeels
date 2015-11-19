package com.example.ana.cityfeels.sia;

import com.example.ana.cityfeels.Location;

public class PontoInteresse {

    public Location posicao;
    public String informacao;
    public String infDetalhada;

    public PontoInteresse() {}

    public PontoInteresse(Location local, String informacao, String infDetalhada)
    {
        this.informacao = informacao;
        this.posicao = local;
        this.infDetalhada = infDetalhada;
    }

}
