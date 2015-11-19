package com.example.ana.cityfeels;

public class PontoInteresseDetailed implements IPontoInteresse  {

    public Location posicao;
    public PontoInteresseBasic[] arredores;
    public String informacao;

    public PontoInteresseDetailed(Location local, PontoInteresseBasic[] arredores, String informacao)
    {
        this.informacao = informacao;
        this.posicao = local;
        this.arredores = arredores;
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
