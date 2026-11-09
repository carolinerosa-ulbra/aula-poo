package br.ulbra.estacionamento.dominio;

public class TipoDeVeiculoInvalidoException extends EstacionamentoException {

    public TipoDeVeiculoInvalidoException(String tipo) {
        super("Tipo de veículo não aceito: \"" + tipo + "\". Use CARRO ou MOTO.");
    }
}
