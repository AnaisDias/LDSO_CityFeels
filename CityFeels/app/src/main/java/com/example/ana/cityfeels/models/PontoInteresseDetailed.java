package com.example.ana.cityfeels.models;

import com.example.ana.cityfeels.Location;

public class PontoInteresseDetailed extends PontoInteresseLocal  {

    public PontoInteresseDetailed(int id, String nome, Location local, String informacao, int orientacao, PontoInteresseBasic[] arredores) {
        super(id, nome, local, informacao, orientacao, arredores);
    }

    @Override
    public String getInformacao() {

        StringBuilder information = new StringBuilder(super.getInformacao());

        return information.toString();
    }
}
