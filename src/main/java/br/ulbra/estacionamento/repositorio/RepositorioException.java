package br.ulbra.estacionamento.repositorio;

/** Erro TÉCNICO de armazenamento (disco, arquivo corrompido). Não é regra de negócio. */
public class RepositorioException extends RuntimeException {

    public RepositorioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public RepositorioException(String mensagem) {
        super(mensagem);
    }
}
