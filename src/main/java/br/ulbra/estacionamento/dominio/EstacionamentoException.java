package br.ulbra.estacionamento.dominio;

/** Base de todas as violações de regra de negócio do estacionamento. */
public class EstacionamentoException extends RuntimeException {

    public EstacionamentoException(String mensagem) {
        super(mensagem);
    }
}
