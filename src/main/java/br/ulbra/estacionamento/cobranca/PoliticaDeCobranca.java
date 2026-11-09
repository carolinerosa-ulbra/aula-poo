package br.ulbra.estacionamento.cobranca;

public interface PoliticaDeCobranca {

    double calcularValor(long minutosEstacionado);
}
