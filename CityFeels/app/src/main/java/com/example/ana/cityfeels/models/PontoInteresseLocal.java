package com.example.ana.cityfeels.models;

import com.example.ana.cityfeels.Location;

public class PontoInteresseLocal extends PontoInteresseBasic {

    public PontoInteresseBasic[] arredores;

    public PontoInteresseLocal(int id, String nome, Location posicao, String informacao, int orientacao, PontoInteresseBasic[] arredores) {
        super(id, nome, posicao, informacao, orientacao);
        this.arredores = arredores;
    }

    @Override
    public String getInformacao()
    {
        StringBuilder information = new StringBuilder(super.getInformacao());

        if(arredores.length > 0)
        {
            information.append(".\nPontos de interesse em redor:\n ");

            for(PontoInteresseBasic ponto : arredores)
                information.append(ponto.getInformacao() + ".\n");
        }

        return information.toString();
    }

}
