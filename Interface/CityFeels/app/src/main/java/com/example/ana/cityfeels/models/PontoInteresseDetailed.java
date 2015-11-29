package com.example.ana.cityfeels.models;

import com.example.ana.cityfeels.Location;

public class PontoInteresseDetailed extends PontoInteresseLocal  {

    public PontoInteresseDetailed(int id, Location local, String informacao, int orientacao, PontoInteresseBasic[] arredores) {
        super(id, local, informacao, orientacao, arredores);
    }

    @Override
    public String getInformacao() {

        StringBuilder information = new StringBuilder(super.getInformacao());

        return information.toString();
    }
}
