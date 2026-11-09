package br.ulbra.estacionamento;

public class Vaga {

    private final int numero;
    private Veiculo veiculo;

    // Construtor sem 'public': só quem está no pacote (o Estacionamento) cria vagas.
    Vaga(int numero) {
        this.numero = numero;
    }

    public int getNumero() {
        return numero;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public boolean isOcupada() {
        return veiculo != null;
    }

    void ocupar(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    void liberar() {
        this.veiculo = null;
    }
}
