package br.ulbra.estacionamento.dominio;

public class EstacionamentoLotadoException extends EstacionamentoException {

    public EstacionamentoLotadoException() {
        super("Não há vaga livre no momento.");
    }
}
