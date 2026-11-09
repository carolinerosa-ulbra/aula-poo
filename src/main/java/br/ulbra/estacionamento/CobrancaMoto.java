package br.ulbra.estacionamento;

public class CobrancaMoto implements PoliticaDeCobranca {

    private static final double TARIFA_MINUTO = 0.06;

    @Override
    public double calcularValor(long minutosEstacionado) {
        return minutosEstacionado * TARIFA_MINUTO;
    }
}
