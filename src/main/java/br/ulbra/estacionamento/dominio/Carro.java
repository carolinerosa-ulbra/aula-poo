package br.ulbra.estacionamento.dominio;

public class Carro extends Veiculo {

    public static final String TIPO = "CARRO";

    public Carro(String placa) {
        super(placa);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }
}
