package br.ulbra.estacionamento.dominio;

public class TicketNaoEncontradoException extends EstacionamentoException {

    public TicketNaoEncontradoException(String placa) {
        super("Não há ticket aberto para a placa " + placa + ".");
    }
}
