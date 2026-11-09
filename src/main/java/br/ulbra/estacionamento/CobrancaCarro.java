package br.ulbra.estacionamento;

public class CobrancaCarro implements PoliticaDeCobranca {

    private static final double TARIFA_MINUTO = 0.10;

    @Override
    public double calcularValor(long minutosEstacionado) {
        return minutosEstacionado * TARIFA_MINUTO;
    }
}
