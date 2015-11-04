package com.example.ana.cityfeels;

import com.example.ana.cityfeels.sia.Etiqueta;

public class Database {

    private Database() {}

    public static Etiqueta getEtiquetaAtLocation(Location location)
    {
        if(location.latitude == 1)
            return new Etiqueta(1, 1f, 1f, "Vire à direita");
        else
            return new Etiqueta(2, 2f, 2f, "View à esquerda");
    }

}
