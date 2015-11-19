package com.example.ana.cityfeels;

public class PontoInteresseDetailed extends PontoInteresseLocal  {

    public PontoInteresseDetailed(Location local, String informacao, int orientacao, PontoInteresseBasic[] arredores) {
        super(local, informacao, orientacao, arredores);
    }

    @Override
    public String getInformation() {

        StringBuilder information = new StringBuilder(super.getInformation());

        return information.toString();
    }
}
