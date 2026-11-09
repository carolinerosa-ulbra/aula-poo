package br.ulbra.estacionamento.dominio;

public class VeiculoJaEstacionadoException extends EstacionamentoException {

    public VeiculoJaEstacionadoException(String placa) {
        super("O veículo de placa " + placa + " já está estacionado.");
    }
}
