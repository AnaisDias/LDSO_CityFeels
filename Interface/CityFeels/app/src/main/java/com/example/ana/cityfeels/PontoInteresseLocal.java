package com.example.ana.cityfeels;

public class PontoInteresseLocal extends PontoInteresseBasic {

    public PontoInteresseBasic[] arredores;

    public PontoInteresseLocal(Location posicao, String informacao, int orientacao, PontoInteresseBasic[] arredores) {
        super(posicao, informacao, orientacao);
        this.arredores = arredores;
    }

    @Override
    public String getInformation()
    {
        StringBuilder information = new StringBuilder(super.getInformation());

        if(arredores.length > 0)
        {
            information.append(".\nPontos de interesse em redor:\n ");

            for(PontoInteresseBasic ponto : arredores)
                information.append(ponto.getInformation() + ".\n");
        }

        return information.toString();
    }

}
