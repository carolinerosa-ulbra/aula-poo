package br.ulbra.estacionamento.dominio;

public class PlacaInvalidaException extends EstacionamentoException {

    public PlacaInvalidaException(String placa) {
        super("Placa inválida: \"" + placa + "\". Use o formato ABC1D23.");
    }
}
