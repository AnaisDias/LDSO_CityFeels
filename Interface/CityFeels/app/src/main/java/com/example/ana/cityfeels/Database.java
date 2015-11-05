package com.example.ana.cityfeels;

import com.example.ana.cityfeels.sia.Etiqueta;

public class Database {

    private static Etiqueta LAST_ETIQUETA = null;

    private Database() {}

    public static Etiqueta getEtiquetaAtLocation(Location location)
    {
        if(location.latitude == 1)
            return LAST_ETIQUETA = new Etiqueta(1, 1f, 1f, "Vire à direita");
        else
            return LAST_ETIQUETA = new Etiqueta(2, 2f, 2f, "Vire à esquerda");
    }

    public static Etiqueta getLastEtiqueta()
    {
        return LAST_ETIQUETA;
    }

}
