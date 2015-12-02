package com.example.ana.cityfeels.models;

import com.example.ana.cityfeels.Location;

public abstract class PontoInteresse {

    private int id;
    private Location posicao;
    private String informacao;
    private int orientacao;

    public PontoInteresse(int id, Location location, String informacao, int orientacao)
    {
        this.id = id;
        this.posicao = location;
        this.informacao = informacao;
        this.orientacao = orientacao;
    }

    public int getId() {
        return this.id;
    }

    public Location getPosicao() {
        return this.posicao;
    }

    public String getInformacao() {
        return this.informacao;
    }

    public int getOrientacao() {
        return this.orientacao;
    }

}
