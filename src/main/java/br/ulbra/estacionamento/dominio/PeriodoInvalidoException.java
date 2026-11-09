package br.ulbra.estacionamento.dominio;

public class PeriodoInvalidoException extends EstacionamentoException {

    public PeriodoInvalidoException() {
        super("A saída não pode ser anterior à entrada.");
    }
}
