package com.example.ana.cityfeels.sia;


public class PontoInteresse {

    public int id;
    public String nome;
    public Location posicao;
    public String informacao;
    public int orientacao;
    public String infDetalhada;

    public PontoInteresse() {}

    public PontoInteresse(int id, String nome, Location posicao, String informacao, int orientacao, String infDetalhada)
    {
        this.id = id;
        this.nome = nome;
        this.informacao = informacao;
        this.posicao = posicao;
        this.orientacao = orientacao;
        this.infDetalhada = infDetalhada;
    }

}
