package com.example.ana.cityfeels.sia;

public class Etiqueta {

    public int id;
    public float latitude;
    public float longitude;
    public String informacao;

    public Etiqueta() {}

    public Etiqueta(int id, float latitude, float longitude, String informacao)
    {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.informacao = informacao;
    }

}
