package br.ulbra.estacionamento.dominio;

public abstract class Veiculo {

    private final String placa;

    protected Veiculo(String placa) {
        this.placa = placa;
    }

    public String getPlaca() {
        return placa;
    }

    // Cada subclasse diz o seu tipo. É essa informação que a entrada do
    // estacionamento usa para escolher a política de cobrança.
    public abstract String getTipo();
}
