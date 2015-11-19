package com.example.ana.cityfeels;

public class PontoInteresseDetailed extends PontoInteresseLocal  {

    public PontoInteresseDetailed(Location local, String informacao, int orientacao, PontoInteresseBasic[] arredores) {
        super(local, informacao, orientacao, arredores);
    }

    @Override
    public String getInformation() {

        StringBuilder information = new StringBuilder(super.getInformation());

        information.append(informacao);
        
        if(arredores.length > 0)
        {
            information.append(".\nPontos de interesse em redor:\n ");

            for(PontoInteresseBasic ponto : arredores)
                information.append(ponto.getInformation() + ".\n");
        }

        return information.toString();
    }
}
