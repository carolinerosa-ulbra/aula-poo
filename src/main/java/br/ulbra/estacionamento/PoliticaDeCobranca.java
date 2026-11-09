package br.ulbra.estacionamento;

public interface PoliticaDeCobranca {

    double calcularValor(long minutosEstacionado);
}
